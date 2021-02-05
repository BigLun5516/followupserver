package com.epic.followup.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 */
public class SumianHttpsUtils extends HTTPsUtils {

    /*
     * true 成功， false 失败
     */
    public static boolean postUserId(String address,
                                     Map<String, String> headerParameters, Map<String, String> bodyParameters){
        return proxySumianHttpRequest(address, "POST", headerParameters,
                getRequestBody(bodyParameters));

    }

    public static String proxyStringSumianHttpRequest(String address, String method,
                                                        Map<String, String> headerParameters, String body) {
        String result = null;
        HttpURLConnection httpConnection = null;

        try {
            httpConnection = HTTPsUtils.createConnection(address, method,
                    headerParameters, body);

            String encoding = "UTF-8";

            if (httpConnection.getResponseCode() != 200){
                return null;
            }else {
                if (httpConnection.getContentType() != null
                        && httpConnection.getContentType().indexOf("charset=") >= 0) {
                    encoding = httpConnection.getContentType()
                            .substring(
                                    httpConnection.getContentType().indexOf(
                                            "charset=") + 8);
                }
                result = inputStream2String(httpConnection.getInputStream(),
                        encoding);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    /**
     * HTTP请求
     *
     * @param address
     *            地址
     * @param method
     *            方法
     * @param headerParameters
     *            头信息
     * @param body
     *            请求内容
     * @return
     * @throws Exception
     */
    public static Boolean proxySumianHttpRequest(String address, String method,
                                          Map<String, String> headerParameters, String body) {
        String result = null;
        HttpURLConnection httpConnection = null;

        try {
            httpConnection = SumianHttpsUtils.createConnection(address, method,
                    headerParameters, body);

            String encoding = "UTF-8";

            if (httpConnection.getResponseCode() == 204){
                return true;
            }else {
                if (httpConnection.getContentType() != null
                        && httpConnection.getContentType().indexOf("charset=") >= 0) {
                    encoding = httpConnection.getContentType()
                            .substring(
                                    httpConnection.getContentType().indexOf(
                                            "charset=") + 8);
                }
                result = inputStream2String(httpConnection.getInputStream(),
                        encoding);
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    /**
     * 创建HTTP连接
     *
     * @param url
     *            地址
     * @param method
     *            方法
     * @param headerParameters
     *            头信息
     * @param body
     *            请求内容
     * @return
     * @throws Exception
     */
    protected static HttpURLConnection createConnection(String url,
                                                        String method, Map<String, String> headerParameters, String body)
            throws Exception {
        URL Url = new URL(url);
        trustAllHttpsCertificates();
        HttpURLConnection httpConnection = (HttpURLConnection) Url
                .openConnection();
        // 设置请求时间
        httpConnection.setConnectTimeout(TIMEOUT);
        // 设置 header
        if (headerParameters != null) {
            Iterator<String> iteratorHeader = headerParameters.keySet()
                    .iterator();
            while (iteratorHeader.hasNext()) {
                String key = iteratorHeader.next();
                httpConnection.setRequestProperty(key,
                        headerParameters.get(key));
            }
        }
        httpConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + ENCODING);

        // 设置请求方法
        httpConnection.setRequestMethod(method);
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        // 写query数据流
        if (!(body.trim().equals("") || body == null)) {
            OutputStream writer = httpConnection.getOutputStream();
            try {
                writer.write(body.getBytes(ENCODING));
            } finally {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }
        }

        // 请求结果
        int responseCode = httpConnection.getResponseCode();
        if (responseCode != 204) {
            throw new Exception(responseCode
                    + ":"
                    + inputStream2String(httpConnection.getErrorStream(),
                    ENCODING));
        }

        return httpConnection;
    }
}
