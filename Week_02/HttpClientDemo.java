package io.github.kimmking.gateway.server;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.*;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import java.io.IOException;


public class HttpClientDemo {

    public final static void main(String[] args) throws Exception {
        String url = "http://localhost:8088/test";
//        invokeByDefaultConfig(url);
        invoke(url);
    }

    private static void invoke(String url) throws IOException {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();
        HttpGet httpGet = new HttpGet(url);
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(10000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();

        // 将上面的配置信息 运用到这个Get请求里
        httpGet.setConfig(requestConfig);

        httpGet.addHeader("Content-Type", "text/html");

        HttpContext context = new BasicHttpContext();
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                byte[] bytes = EntityUtils.toByteArray(entity);
                System.out.println("Return data length is " + bytes.length);
                System.out.println("Return data content is " + new String(bytes));
            }
        } finally {
            httpClient.close();
        }

    }


    /**
     * HttpClient  默认配置
     * @param url
     * @throws Exception
     */
    private static void invokeByDefaultConfig (String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(org.apache.http.HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    if (status == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Invoke failed, status code is : " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
    }

}
