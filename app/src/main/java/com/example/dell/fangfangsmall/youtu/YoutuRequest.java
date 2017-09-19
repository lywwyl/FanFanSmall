package com.example.dell.fangfangsmall.youtu;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class YoutuRequest {


    public final static String API_YOUTU_BASE = "http://api.youtu.qq.com/youtu/";


    /**
     * https
     * @param postData
     * @param mothod
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject sendHttpsRequest(JSONObject postData, String mothod, String sign) throws NoSuchAlgorithmException, KeyManagementException,
            IOException, JSONException {

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        URL url = new URL(API_YOUTU_BASE + mothod);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sc.getSocketFactory());
        connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
        // set header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("user-agent", "youtu-android-sdk");
        connection.setRequestProperty("Authorization", sign);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "text/json");
        connection.connect();

        // POST请求
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        postData.put("app_id", YoutuManager.APP_ID);
        out.write(postData.toString().getBytes("utf-8"));
        // 刷新、关闭
        out.flush();
        out.close();
        // 读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer resposeBuffer = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            resposeBuffer.append(lines);
        }
        reader.close();
        connection.disconnect();
        JSONObject respose = new JSONObject(resposeBuffer.toString());
        return respose;
    }

    /**
     * http
     * @param postData
     * @param mothod
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static JSONObject sendHttpRequest(JSONObject postData, String mothod, String sign) throws IOException, JSONException,
            KeyManagementException, NoSuchAlgorithmException {

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");
        URL url = new URL(API_YOUTU_BASE + mothod);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("user-agent", "youtu-android-sdk");
        connection.setRequestProperty("Authorization", sign);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "text/json");
        connection.connect();
        // POST请求
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        postData.put("app_id", YoutuManager.APP_ID);
        out.write(postData.toString().getBytes("utf-8"));
        out.flush();
        out.close();
        // 读取响应
        InputStream isss = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(isss));
        String lines;
        StringBuffer resposeBuffer = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            resposeBuffer.append(lines);
        }
        reader.close();
        connection.disconnect();
        JSONObject respose = new JSONObject(resposeBuffer.toString());
        Log.e("youtu", "-----------------------------------------------------------------");
        Log.e("youtu", respose.toString());
        Log.e("youtu", "-----------------------------------------------------------------");
        return respose;

    }

}
