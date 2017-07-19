package com.wingsglory.foru.server.common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hezhujun on 2017/6/14.
 */
public class HttpUtil {

    public static final String CHARSET_NAME = "UTF-8";

    public static InputStream execute(URL url, String method) throws IOException {
        return execute(url, null, null, method);
    }

    public static InputStream execute(URL url, Header headers, String method) throws IOException {
        return execute(url, headers, null, method);
    }

    public static InputStream execute(URL url, Map<String, String> params, String method) throws IOException {
        return execute(url, null, params, method);
    }

    public static InputStream execute(URL url, Header headers, Map<String, String> params, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry :
                    headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (params != null && params.size() > 0) {
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                pw.write(entry.getKey() + "&" + entry.getValue());
                if (iterator.hasNext()) {
                    pw.write("&");
                }
            }
            pw.close();
            os.close();
        }
        return connection.getInputStream();
    }

    public static InputStream get(URL url) throws IOException {
        return execute(url, "GET");
    }

    public static InputStream get(URL url, Header header) throws IOException {
        return execute(url, header,"GET");
    }

    public static String getString(URL url) throws IOException {
        InputStream is = get(url);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, CHARSET_NAME));
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        while (br.read(buffer, 0, 1024) != -1) {
            builder.append(buffer);
        }
        br.close();
        is.close();
        return builder.toString();
    }


    class Header extends HashMap<String, String> {

    }

}
