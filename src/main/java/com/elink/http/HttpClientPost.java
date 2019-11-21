package com.elink.http;

import android.util.Log;

import com.elink.log.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


public class HttpClientPost {
    private static final int REQUEST_TIMEOUT = 10 * 1000;//设置请求超时10秒钟
    private static final int SO_TIMEOUT = 10 * 1000;  //设置等待数据超时时间10秒钟
    static HttpClient httpClient;

    /**
     * @param path
     * @return String
     */
    public static String ThreadSendData(String path) {
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(path);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                // 获得 HttpEntity 对象
                HttpEntity httpEntity = httpResponse.getEntity();
                // 获得输入流
                InputStream inputStream = httpEntity.getContent();
                String result = StringtoolsUtils.readStrem(inputStream);
                return result;
            } else {
                return "FAILURE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRORCODE";

        }
    }

    /**
     * 发送数据
     *
     * @param path
     * @param list
     * @return
     */
    public static String sendData(String path, List<NameValuePair> list) {
        try {
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(new UrlEncodedFormEntity(list, Charset.defaultCharset().toString()));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                // 获得 HttpEntity 对象
                HttpEntity httpEntity = httpResponse.getEntity();
                // 获得输入流
                InputStream inputStream = httpEntity.getContent();
                String result = StringtoolsUtils.readStrem(inputStream);
                return result;
            } else {
                return "FAILURE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRORCODE";
        }
    }

    public static String httpSendData(final String path, final String data) {
        try {
            // 创建 URL 实例
            URL url = new URL(path);
            // 获取 HTTPURLConnection 对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置超时时间
            connection.setConnectTimeout(REQUEST_TIMEOUT);
            connection.setReadTimeout(SO_TIMEOUT);
            // 不使用缓存
            connection.setUseCaches(false);
            // 指定请求方式
            connection.setRequestMethod("POST");
            // 准备数据，将参数编码
            // String dataJson = "name=" + URLEncoder.encode(data, "UTF-8");
            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length() + "");
            // 将数据写给服务器
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 得到输出流
            OutputStream os = connection.getOutputStream();
            // 将数据写入流中
            os.write(data.getBytes());
            // 获取服务器返回的状态码
            int code = connection.getResponseCode();
            if (code == 200) {
                // 得到服务器返回的输入流
                InputStream is = connection.getInputStream();
                // 将输入流转换成字符串
                String result = StringtoolsUtils.readStrem(is);
                return result;
            } else {
                return "FAILURE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERRORCODE";
        }
    }
}
