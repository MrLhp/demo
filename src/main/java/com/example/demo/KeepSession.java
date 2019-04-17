package com.example.demo;

import com.google.common.cache.Cache;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class KeepSession {
    private static final String loginUrl = "https://gjdyzjb.cn/w/login";
    private static final String validUrl = "https://gjdyzjb.cn/validPic?v=";

    private static HttpClientContext httpClientContext = HttpClientContext.create();
    private static CookieStore cookieStore = new BasicCookieStore();
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(6000).
                    setConnectTimeout(6000).
                    setSocketTimeout(6000).
                    build();
    private static  CloseableHttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            // 配置超时回调机制
            .setRetryHandler((exception, executionCount, context) -> {
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return true;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }).setConnectionManagerShared(true).build();

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public static HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    static {
        httpClientContext.setCookieStore(cookieStore);
    }

    public static void main(String[] args) {

        try {
            downloadFile(validUrl+new Random().nextInt(),"/home/pc-yx/yzm.png");
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入验证码:");
            String captcha = scanner.nextLine();
            JSONObject json = new JSONObject();
            json.put("username", "admin");
            json.put("password", "Admin@ld@20190318");
            json.put("captcha",captcha);
            HttpPost post = new HttpPost(loginUrl);
            HttpEntity entity = new StringEntity(json.toString(),"text/html", "utf-8");
            post.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(post, httpClientContext);
            System.out.println("=================================================================");
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("=================================================================");

            List<String> fileList = FileDataReader.getFileContentArrary("/home/pc-yx/liuhp.csv");

            Thread [] threads = new Thread[fileList.size() / 50000 + 1];
            for(int i = 0; i < threads.length; i++){
                threads[i] = new Good(50000 * i, fileList.subList(i * 50000, fileList.size() < ((i + 1) * 50000)? fileList.size() : (i + 1) * 50000));
                threads[i].start();
            }

            for(int i = 0; i < threads.length; i++){
                threads[i].join();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(String url, String localPath) throws IOException {
        try {
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpget,httpClientContext);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                if (entity != null) {
                    InputStream in = entity.getContent();
                    try {
                        // do something useful with the response
                        byte[] buffer = new byte[1024];
                        BufferedInputStream bufferedIn = new BufferedInputStream(in);
                        int len = 0;

                        FileOutputStream fileOutStream = new FileOutputStream(new File(localPath));
                        BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOutStream);

                        while ((len = bufferedIn.read(buffer, 0, 1024)) != -1) {
                            bufferedOut.write(buffer, 0, len);
                        }
                        bufferedOut.flush();
                        bufferedOut.close();
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        in.close();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
