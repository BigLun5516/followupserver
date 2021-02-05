package com.epic.followup.util;


import java.io.*;
import java.io.IOException;

public class CsvToJsonUtil {

    /**
     * 根据文件路径读取CSV文件 返回String字符串
     *
     * @param filePath  CSV文件路径
     * @param separator CSV文件内容分隔符-默认是逗号分隔
     * @return String 字符串
     * @throws IOException IO异常
     */
    public static String getJSONFromFile(String filePath, String separator) throws IOException {

        byte[] bytes = null;

        bytes = readFileByBytes(filePath);

        String csv = new String(bytes, "GBK");
//        System.out.println(csv);
        return getJSON(csv, separator).replace("\r", "").replace("\n", "").replace("\b", "").replaceAll("\\s*", "");
    }

    /**
     * 读取CSV文件内容
     *
     * @param content   文件
     * @param separator 分隔符
     * @return String 字符串
     */
    private static String getJSON(String content, String separator) {

        StringBuilder sb = new StringBuilder("[\n");

        String csv = content;
        String[] csvValues = csv.split("\n");
        String[] header = csvValues[0].split(separator);

        for (int i = 1; i < csvValues.length; i++) {
            sb.append("\t").append("{").append("\n");
            String[] tmp = csvValues[i].split(separator);

            for (int j = 0; j < tmp.length; j++) {

                sb.append("\t").append("\t\"").append(header[j].replaceAll("\\s*", "").replace("\"", "")).append("\":\"")
                        .append(tmp[j].replaceAll("\\s*", "").replace("\"", "")).append("\"");
                if (j < tmp.length - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
            if (i < csvValues.length - 1) {
                sb.append("\t},\n");
            } else {
                sb.append("\t}\n");
            }
        }

        sb.append("]");

        return sb.toString();
    }


    /**
     * 根据文件路径读取byte[] 数组
     */
    private static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }
                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
                bos.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(getJSONFromFile("./src/main/resources/ncov/3.csv", "\\,"));
    }
}
