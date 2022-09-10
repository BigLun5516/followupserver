package com.epic.followup.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.epic.followup.controller.managementSys.scale.ScaleController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static void main(String[] args) {

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("BankName", "BankName");
        dataMap.put("Addr", "Addr");
        dataMap.put("Phone", "Phone");
        List<Map> list = new ArrayList<Map>();
        list.add(dataMap);
        writeExcel(list, 3, "D:/writeExcel.xlsx");

    }

    public static long timeDuration(String time1, String time2) {
        time1 = time1.replaceAll(" ", "T");
        time2 = time2.replaceAll(" ", "T");
        System.out.println(time1);
        System.out.println(time2);
        LocalDateTime start = LocalDateTime.parse(time1);
        LocalDateTime end = LocalDateTime.parse(time2);
        Duration dur = Duration.between(start, end);

        return dur.toMillis() / 1000;
    }

    public static void copyFileByChannel(String sourcePath, String targetPath) {
        FileChannel outChannel = null;
        FileChannel inChannel = null;

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(sourcePath);
            fos = new FileOutputStream(targetPath);

            //获取通道
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            //分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            while (inChannel.read(buf) != -1) {
                //转换为读取数据模式
                buf.flip();
                //写入到磁盘
                outChannel.write(buf);
                //清空缓冲区
                buf.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
                if (inChannel != null) {
                    inChannel.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeExcel(String path, JSONArray answer) {
        OutputStream out = null;
        try {
            // 读取Excel文档
            File finalXlsxFile = new File(path);
            Workbook workBook = getWorkbok(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);

            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算

            //如果说数量超了，换下一个excel文档
            if (rowNumber >= 5000 && path.contains("SQ")) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String outputPath = "/home/followup/static/SchoolSurvey/data/SQ-" + timestamp.getTime() + ".xlsx";

                copyFileByChannel(ScaleController.SQTemplate_PATH, outputPath);

                ScaleController.SQ_PATH = outputPath;
                path = outputPath;
                rowNumber = 2;
                finalXlsxFile = new File(path);
                workBook = getWorkbok(finalXlsxFile);
                sheet = workBook.getSheetAt(0);

            } else if (rowNumber >= 5000 && path.contains("TQ")) {

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String outputPath = "/home/followup/static/SchoolSurvey/data/TQ-" + timestamp.getTime() + ".xlsx";

                copyFileByChannel(ScaleController.TQTemplate_PATH, outputPath);

                ScaleController.TQ_PATH = outputPath;
                path = outputPath;
                rowNumber = 2;
                finalXlsxFile = new File(path);
                workBook = getWorkbok(finalXlsxFile);
                sheet = workBook.getSheetAt(0);
            }


            System.out.println("原始数据总行数，除属性列：" + rowNumber);

            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(path);
            workBook.write(out);

            /**
             * 往Excel中写新数据
             */
            Row row = sheet.createRow(rowNumber + 1);
            answer.add(0, rowNumber - 1);
            System.out.println("数据大小：" + answer.size());
            for (int k = 0; k < answer.size(); k++) {
                // 在一行内循环
                Cell cell = row.createCell(k);
                cell.setCellValue(answer.getString(k));
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(path);
            workBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void writeExcel(List<Map> dataList, int cloumnCount, String finalXlsxPath) {
        OutputStream out = null;
        try {
            // 获取总列数
            int columnNumCount = cloumnCount;
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbok(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            /**
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
                // 得到要插入的每一条记录
                Map dataMap = dataList.get(j);
                String name = dataMap.get("BankName").toString();
                String address = dataMap.get("Addr").toString();
                String phone = dataMap.get("Phone").toString();
                for (int k = 0; k <= columnNumCount; k++) {
                    // 在一行内循环
                    Cell first = row.createCell(0);
                    first.setCellValue(name);

                    Cell second = row.createCell(1);
                    second.setCellValue(address);

                    Cell third = row.createCell(2);
                    third.setCellValue(phone);
                }
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param in
     * @param filename
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {     //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}