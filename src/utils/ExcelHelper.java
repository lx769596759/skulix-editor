package utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    private static Logger logger = Logger.getLogger(ExcelHelper.class);

    /**
          *  读数据量少于1千行数据
     */
    public static List<Object> simpleReadListString(String filePath , int sheetNo , int headLineMun) {
        InputStream inputStream = null;
        List<Object> data = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            data = EasyExcelFactory.read(inputStream, new Sheet(sheetNo, headLineMun));
        } catch (IOException e) {
            logger.error("读取Excel失败",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭流失败",e);
            }
        }
        return data;
    }


    /**
          * 读数据量少于1千行数据,自动转成javamodel
     */
    public static List<Object> simpleReadJavaModel(String filePath , int sheetNo , int headLineMun , Class<? extends BaseRowModel> clazz) {

        InputStream inputStream = null;
        List<Object> data = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            data = EasyExcelFactory.read(inputStream, new Sheet(sheetNo, headLineMun, clazz));
        } catch (IOException e) {
            logger.error("读取Excel失败",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭流失败",e);
            }
        }
        return data;
    }


    /**
          * 读数据量大于1千行
     */
    public static List<Object> saxReadListString(String filePath , int sheetNo , int headLineMun) {

        InputStream inputStream = null;
        List<Object> data = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            ExcelListener excelListener = new ExcelHelper().new ExcelListener();
            EasyExcelFactory.readBySax(inputStream, new Sheet(sheetNo, headLineMun), excelListener);
            data = excelListener.getDatas();
        } catch (IOException e) {
            logger.error("读取Excel失败",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭流失败",e);
            }
        }
        return data;
    }


    /**
          * 读数据量大于1千行，自动转成javamodel
     */
    public static List<Object> saxReadJavaModel(String filePath , int sheetNo , int headLineMun , Class<? extends BaseRowModel> clazz) throws IOException {

        InputStream inputStream = null;
        List<Object> data = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            ExcelListener excelListener = new ExcelHelper().new ExcelListener();
            EasyExcelFactory.readBySax(inputStream, new Sheet(sheetNo, headLineMun, clazz), excelListener);
            data = excelListener.getDatas();
        } catch (IOException e) {
            logger.error("读取Excel失败",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭流失败",e);
            }
        }
        return data;
    }

    class ExcelListener extends AnalysisEventListener<Object> {

        //自定义用于暂时存储data。
        //可以通过实例获取该值
        private List<Object> datas = new ArrayList<Object>();

        public void invoke(Object object, AnalysisContext context) {
            datas.add(object);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
            doSomething(object);//根据自己业务做处理
        }
        private void doSomething(Object object) {
            //1、入库调用接口
        }
        public void doAfterAllAnalysed(AnalysisContext context) {
            // datas.clear();//解析结束销毁不用的资源
        }
        public List<Object> getDatas() {
            return datas;
        }
        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }

}
