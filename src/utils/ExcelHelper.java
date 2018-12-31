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
          *  ������������1ǧ������
     */
    public static List<Object> simpleReadListString(String filePath , int sheetNo , int headLineMun) {
        InputStream inputStream = null;
        List<Object> data = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            data = EasyExcelFactory.read(inputStream, new Sheet(sheetNo, headLineMun));
        } catch (IOException e) {
            logger.error("��ȡExcelʧ��",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("�ر���ʧ��",e);
            }
        }
        return data;
    }


    /**
          * ������������1ǧ������,�Զ�ת��javamodel
     */
    public static List<Object> simpleReadJavaModel(String filePath , int sheetNo , int headLineMun , Class<? extends BaseRowModel> clazz) {

        InputStream inputStream = null;
        List<Object> data = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            data = EasyExcelFactory.read(inputStream, new Sheet(sheetNo, headLineMun, clazz));
        } catch (IOException e) {
            logger.error("��ȡExcelʧ��",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("�ر���ʧ��",e);
            }
        }
        return data;
    }


    /**
          * ������������1ǧ��
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
            logger.error("��ȡExcelʧ��",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("�ر���ʧ��",e);
            }
        }
        return data;
    }


    /**
          * ������������1ǧ�У��Զ�ת��javamodel
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
            logger.error("��ȡExcelʧ��",e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("�ر���ʧ��",e);
            }
        }
        return data;
    }

    class ExcelListener extends AnalysisEventListener<Object> {

        //�Զ���������ʱ�洢data��
        //����ͨ��ʵ����ȡ��ֵ
        private List<Object> datas = new ArrayList<Object>();

        public void invoke(Object object, AnalysisContext context) {
            datas.add(object);//���ݴ洢��list������������������Լ�ҵ���߼�����
            doSomething(object);//�����Լ�ҵ��������
        }
        private void doSomething(Object object) {
            //1�������ýӿ�
        }
        public void doAfterAllAnalysed(AnalysisContext context) {
            // datas.clear();//�����������ٲ��õ���Դ
        }
        public List<Object> getDatas() {
            return datas;
        }
        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }

}
