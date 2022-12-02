package com.cramin.springbootdemo.util.str;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class StringUtil {

    /**
     *
     * @param httpUrl url
     * @param end end index of substring
     * @param len length of substring
     * @return substring
     */
    public static String readLen(String httpUrl, int end, int len) throws IOException {
        return readInput(httpUrl).substring(end - len + 1, end + 1);
    }
    public static String readInput(String httpUrl) throws IOException {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
            }
        } finally {
            assert br != null;
            br.close();
            is.close();
            connection.disconnect();
        }
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readLen("https://promo.hafoo.com.cn/html/fzwx/js/index.chunkhash=d59e1a3622.js", 100, 40));
    }
}
