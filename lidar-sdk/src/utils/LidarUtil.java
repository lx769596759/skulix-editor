package utils;

import gnu.io.*;
import javafx.util.Pair;
import handle.LidarDataHandle;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LidarUtil {

    private static SerialPort serialPort;
    private static final byte[] START_SCAN = new byte[]{(byte) 0xA5, 0x20};  // 开始扫描
    private static final byte[] STOP_SCAN = new byte[]{(byte) 0xA5, 0x25};   // 停止扫描
    private static final byte[] STOP_MOTOR = new byte[]{(byte) 0xA5, (byte) 0xF0, 0x02, 0x00, 0x00, 0x57}; // 关闭电机转动命令
    private static final byte[] START_MOTOR_DEFAULT = new byte[]{(byte) 0xA5, (byte) 0xF0, 0x02, (byte) 0xb8, 0x01, (byte) 0xee}; // 电机转动,速度：440转/分
    private static final byte[] START_MOTOR_420 = new byte[]{(byte) 0xA5, (byte) 0xF0, 0x02, (byte) 0xa4, 0x01, (byte) 0xf2}; // 电机转动,速度：420转/分
    private static final byte[] START_MOTOR_400 = new byte[]{(byte) 0xA5, (byte) 0xF0, 0x02, (byte) 0x90, 0x01, (byte) 0xc6}; // 电机转动,速度：400转/分
    private static boolean readFlag = false;
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);


    public static void setReadFlag(boolean readFlag) {
        readFlag = readFlag;
    }

    /**
     * 初始化雷达
     * @param serialPortName
     */
    public static void init(String serialPortName) {
        try {
            if (serialPort == null) {
                serialPort = SerialPortUtil.openSerialPort(serialPortName);
                // 设置或清除DTR位
                serialPort.setDTR(false);
                stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始扫描
     */
    public static void start() {
        setReadFlag(true);
        SerialPortUtil.sendData(serialPort, START_MOTOR_DEFAULT);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SerialPortUtil.sendData(serialPort, START_SCAN);
        System.out.println("开始扫描");
    }

    /**
     * 停止扫描
     */
    public static void stop() {
        try(InputStream inputStream = serialPort.getInputStream()) {
            setReadFlag(false);
            SerialPortUtil.sendData(serialPort, STOP_SCAN);
            Thread.sleep(100);
            SerialPortUtil.sendData(serialPort, STOP_MOTOR);
            Thread.sleep(1000);
            // 清空串口的数据
            byte[] b1 = new byte[10240];
            int temp;
            while ((temp = inputStream.read(b1)) > 0) {
                b1 = new byte[10240];
            }
            b1 = null;
            System.out.println("停止扫描");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止扫描，并关闭串口
     */
    public static void close() {
        stop();
        SerialPortUtil.closeSerialPort(serialPort);
    }

    /**
     * 获取雷达数据
     */
    private static void read() {
        cachedThreadPool.execute(() -> {
            boolean isBegin = true;
            // 循环接收来自串口的数据
            try (InputStream inputStream = serialPort.getInputStream()) {
                out:
                while (readFlag) {
                    // 刚开始的12个字节为无用字节，先读取
                    while (isBegin) {
                        int count = 12;
                        byte[] b = new byte[count];
                        int readCount = 0; // 已经成功读取的字节的个数
                        while (readCount < count) {
                            if (!readFlag) {
                                break out;
                            }
                            readCount += inputStream.read(b, readCount, count - readCount);
                        }
                        isBegin = false;
                    }
                    System.out.println("开始收集数据");
                    List<String> list = new ArrayList<>();
                    boolean isRound = false;
                    while (true) {
                        int count = 5; // 总共要取的字节的个数
                        int readCount = 0; // 已经成功读取的字节的个数
                        byte[] b = new byte[count];
                        while (readCount < count) {
                            if (!readFlag) {
                                break out;
                            }
                            readCount += inputStream.read(b, readCount, count - readCount);
                        }
                        StringBuilder buf = new StringBuilder(b.length * 2);
                        StringBuilder result = new StringBuilder();
                        // 最后一位为1，代表新的一圈360°扫描的开始
                        if ((b[0] & 0x1) == 1) {
                            if (!isRound) {
                                isRound = true;
                            }
                            if (!list.isEmpty()) {
                                List<String> dataList = new ArrayList<>(list);
                                cachedThreadPool.execute(new LidarDataHandle(dataList));
                            }
                            list.clear(); // 清空list
                            for (byte bb : b) { // 使用String的format方法进行转换
                                result.append(Long.toString(bb & 0xff, 2) + ",");
                                buf.append(String.format("%02x", new Integer(bb & 0xff)));
                            }
                            if (b[3] != 0 || b[4] != 0) {
                                list.add(buf.toString()); // 将5个16进制的字符加入到list
                                // System.out.println(result.toString().substring(0,
                                // result.length()-1));
                                // System.out.println(buf);
                            }
                        } else {
                            if (!isRound ) {
                                continue; // 如果不是在新的一周里，则放弃数据，保证取到完整一周的数据
                            } else {
                                for (byte bb : b) { // 使用String的format方法进行转换
                                    result.append(Long.toString(bb & 0xff, 2) + ",");
                                    buf.append(String.format("%02x", new Integer(bb & 0xff)));
                                }
                                if (b[3] != 0 || b[4] != 0) {
                                    list.add(buf.toString());
                                    // System.out.println(result.toString().substring(0,
                                    // result.length()-1));
                                    // System.out.println(buf);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void readWithTime(String serialPortName, Long seconds ) {
        init(serialPortName);
        start();
        read();
        scheduledThreadPool.schedule(() -> stop(), seconds, TimeUnit.SECONDS);
    }

    /**
     * 解析数据
     * @param data
     * @return angle--角度; distance--距离
     */
    public static Pair parse(String data) {
        DecimalFormat df = new DecimalFormat("#.#####");
        String angleStr = data.substring(4, 6) + data.substring(2, 4);
        String distanceStr = data.substring(8, 10) + data.substring(6, 8);
        String angle = df.format((Integer.parseInt(angleStr, 16) >> 1) / 64);  //十六进制转换成十进制后右移一位再除以64
        String distance= df.format((Integer.parseInt(distanceStr, 16)) / 4);   //十六进制转换成十进制后再除以4
        return new Pair(angle, distance);
    }
}
