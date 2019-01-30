package com.tk.eventanalysis.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

@Component
public class HttpServiceUtil {

    private static Logger log = LoggerFactory.getLogger(HttpServiceUtil.class);

    @Value("${thirdMeditor.Protocol:http}")
    private String PROTOCOL;
    @Value("${thirdMeditor.Host:127.0.0.1}")
    private String HOST;
    @Value("${thirdMeditor.Post:8080}")
    private int POST;

    public String execute(String url, String param1) throws IOException, InterruptedException {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(50000);

        // 使用POST方法
        PostMethod method = new PostMethod(url);
        RequestEntity requestEntity = new StringRequestEntity(param1, "text/xml", "UTF-8");
        method.setRequestEntity(requestEntity);
        int statusCode = 0;
        statusCode = client.executeMethod(method);
        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
            // 从头中取出转向的地址
            Header locationHeader = (Header) method.getResponseHeader("location");
            String location = null;
            if (locationHeader != null) {
                location = locationHeader.getValue();
                return execute(location, param1);// 用跳转后的页面重新请求。
            } else {
                // 释放连接
                method.releaseConnection();
            }
        } else {
            // 打印服务器返回的状态
            // 打印返回的信息
            InputStream stream = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuffer buf = new StringBuffer();
            String line;
            while (null != (line = br.readLine())) {
                buf.append(line).append("\n");
            }
            // 释放连接
            method.releaseConnection();
            return buf.toString();
        }
        return "";
    }

    public String executeDelete(String uri) {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(HOST, POST, PROTOCOL);
        DeleteMethod method = new DeleteMethod(uri);
        method.setRequestHeader("ContentType", "application/json");
        String response = "";
        try {
            client.executeMethod(method);

            response = method.getResponseBodyAsString();

            response = response.replace(":null", ":\"\"");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public String executeGet(String uri) {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(HOST, POST, PROTOCOL);
        GetMethod method = new GetMethod(uri);
        method.setRequestHeader("ContentType", "application/json");
        String response = "";
        try {
            client.executeMethod(method);

            response = method.getResponseBodyAsString();

            response = response.replace(":null", ":\"\"");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public String executePost(String uri, String jsonParameters){
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(HOST, POST, PROTOCOL);
        PostMethod method = new UTF8PostMethod(uri);
        RequestEntity requestEntity = null;
        try {
            requestEntity = new StringRequestEntity(jsonParameters, "application/json", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        method.setRequestEntity(requestEntity);
        try {
            client.executeMethod(method);
//			String response = new String(method.getResponseBodyAsString()
//					.getBytes("ISO8859-1"), "UTF-8");
            String response = method.getResponseBodyAsString();

            response = response.replace(":null", ":\"\"");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            method.releaseConnection();
        }
    }

    public static void checkParams(String uri, NameValuePair[] parameters) throws Exception {
        if (parameters == null || parameters.length == 0) {
            throw new Exception("参数没有传递给接口！");
        }
        StringBuffer str = new StringBuffer();
        for (NameValuePair p : parameters) {
            String name = p.getName();
            String value = p.getValue();
            if (!StringUtils.isEmpty(p.getValue())) {
                str.append("&" + name + "=" + value);
            }

        }
        if (StringUtils.isEmpty(str.toString())) {
            throw new Exception("参数没有传递给接口！没有获取到参数！");
        }
    }

    /**
     * 所有的参数转字符串
     *
     * @param params
     */
    public String printParam(Map<String, Object> params) {
        Iterator keys = params.keySet().iterator();
        StringBuffer paramsStr = new StringBuffer();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            String val = params.get(key) == null ? "" : params.get(key).toString();
            paramsStr.append("&" + key + "=" + val);
        }
        return paramsStr.toString();
    }

    private static class UTF8PostMethod extends PostMethod {
        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            // return super.getRequestCharSet();
            return "UTF-8";
        }

    }

}