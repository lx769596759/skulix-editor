package utils;

import beans.LidarPoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathUtil {

    private static final String PYTHON_PATH = System.getProperty("user.dir") + "\\python\\venv\\Scripts\\python.exe ";
    private static final String FILE_PATH = System.getProperty("user.dir") + "\\python\\main.py ";


    /*
    https://blog.csdn.net/Jacky_Ponder/article/details/70314919
     */
    public static Map<String, Double> leastSquare(List<LidarPoint> pointList) {
        if (pointList.size() < 3) {
            return null;
        }

        int i = 0;

        double X1 = 0d;
        double Y1 = 0d;
        double X2 = 0d;
        double Y2 = 0d;
        double X3 = 0d;
        double Y3 = 0d;
        double X1Y1 = 0d;
        double X1Y2 = 0d;
        double X2Y1 = 0d;

        for (i = 0; i < pointList.size(); i++) {
            double x = pointList.get(i).getX();
            double y = pointList.get(i).getY();
            X1 = X1 + x;
            Y1 = Y1 + y;
            X2 = X2 + x * x;
            Y2 = Y2 + y * y;
            X3 = X3 + x * x * x;
            Y3 = Y3 + y * y * y;
            X1Y1 = X1Y1 + x * y;
            X1Y2 = X1Y2 + x * y * y;
            X2Y1 = X2Y1 + x * x * y;
        }

        double C, D, E, G, H, N;
        double a, b, c;
        N = pointList.size();
        C = N * X2 - X1 * X1;
        D = N * X1Y1 - X1 * Y1;
        E = N * X3 + N * X1Y2 - (X2 + Y2) * X1;
        G = N * Y2 - Y1 * Y1;
        H = N * X2Y1 + N * Y3 - (X2 + Y2) * Y1;
        a = (H * D - E * G) / (C * G - D * D);
        b = (H * C - E * D) / (D * D - G * C);
        c = -(a * X1 + b * Y1 + X2 + Y2) / N;

        double A, B, R;
        A = a / (-2);
        B = b / (-2);
        R = Math.sqrt(a * a + b * b - 4 * c) / 2;

        Map<String, Double> result = new HashMap<>();
        result.put("X", A);
        result.put("Y", B);
        result.put("R", R);
        System.out.println(String.format("中心点---[%s, %s]\n半径---%s", String.valueOf(A), String.valueOf(B), String.valueOf(R)));
        return result;
    }

    public static void showPlot(List<LidarPoint> pointList) {
        new Thread(() -> {
            List<String> xAxis = new ArrayList<>();
            List<String> yAxis = new ArrayList<>();
            for (LidarPoint point : pointList) {
                xAxis.add(String.valueOf(point.getX()));
                yAxis.add(String.valueOf(point.getY()));
            }
            String xAxisStr = String.join(",", xAxis);
            String yAxisStr = String.join(",", yAxis);
            Map<String, Double> result = leastSquare(pointList);
            double x = result.get("X");
            double y = result.get("Y");
            double r = result.get("R");
            try {
                Process process = Runtime.getRuntime().exec(
                        PYTHON_PATH + FILE_PATH + xAxisStr + " " + yAxisStr + " " + x + " " + y + " " + r);
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();
                int re = process.waitFor();
                System.out.println(re == 1 ? "----状态码1----运行失败" : "----状态码0----运行成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        List<LidarPoint> list = new ArrayList<>();
        LidarPoint p1 = new LidarPoint(36.1114, 14.1547);
        LidarPoint p2 = new LidarPoint(36.2315, 10.8926);
        LidarPoint p3 = new LidarPoint(19.3333, 28.1256);
        LidarPoint p4 = new LidarPoint(18.9861, 31.9554);
        LidarPoint p5 = new LidarPoint(33.2326, 18.4169);
        LidarPoint p6 = new LidarPoint(26.1469, 26.6391);
        LidarPoint p7 = new LidarPoint(10.1469, 16.6391);
        LidarPoint p8 = new LidarPoint(12.4569, 22.5251);
        LidarPoint p9 = new LidarPoint(15.1469, 27.6531);
        LidarPoint p10 = new LidarPoint(19.1469, 31.1421);
        LidarPoint p11 = new LidarPoint(24.1469, 28.6391);
        LidarPoint p12 = new LidarPoint(8.1469, 19.6391);
        LidarPoint p13 = new LidarPoint(13.1469, 20.6391);

//        LidarPoint p1 = new LidarPoint(8, 7);
//        LidarPoint p2 = new LidarPoint(2, -1);
//        LidarPoint p3 = new LidarPoint(9, 6);
//        LidarPoint p4 = new LidarPoint(1, 0);
//        LidarPoint p5 = new LidarPoint(10, 3);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        list.add(p7);
        list.add(p8);
        list.add(p9);
        list.add(p10);
        list.add(p11);
        list.add(p12);
        list.add(p13);
        showPlot(list);
    }
}
