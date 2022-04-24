package com.example.booksys.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil extends Thread {

    String data;
    String servletUrl = "";
    String response = "";

    String transferMethod;

    Bitmap bitmap;
    String url = "";

    HttpURLConnection connection;

    public String getResponse() {
        return response;
    }


    public Bitmap getImage() {
        return bitmap;
    }

    public NetUtil(String data, String servletUrl, String transferMethod) {
        this.data = data;
        this.servletUrl = servletUrl;
        this.transferMethod = transferMethod;
    }

    public NetUtil(String Url) {
        this.url = Url;
    }


    @Override
    public void run() {
        if ("POST".equals(transferMethod)) {
            post();
        } else if ("GET".equals(transferMethod)) {
            get();
        } else {
            getImageRes();
        }

    }

    /**
     * 访问网络
     **/
    public void get() {

        try {
            URL url = new URL(NetEnum.serverUrl + servletUrl +"?" + data);   //创建HttpUrlConnection对象
            //URL url = new URL("http://10.0.2.2:8080//"+urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");                 //使用传输方法
            connection.setConnectTimeout(3000);                 //设置超时信息
            connection.setReadTimeout(3000);
            connection.connect();

            if (200 == connection.getResponseCode()) {
                InputStream is = connection.getInputStream();
                response = getStringFromInputStream(is);
                //Log.i("response",response);
                System.out.println(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * POST访问网络
     **/
    public void post() {

        try {
            URL url = new URL(NetEnum.serverUrl + servletUrl);                  //创建HttpUrlConnection对象
            //URL url = new URL("http://10.0.2.2:8080//"+urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);                                    //设置允许输入
            connection.setDoOutput(true);                                   //设置允许输出
            connection.setUseCaches(false);                                 //post方式不能设置缓存，需手动设置为false
            connection.setRequestMethod("POST");                            //使用传输方法
            connection.setUseCaches(false);                                 //忽略使用缓存
            connection.setConnectTimeout(3000);                             //设置超时信息
            connection.setReadTimeout(3000);
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Connection", "Keep-Alive");      //维持长连接

            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            if (200 == connection.getResponseCode()) {
                InputStream is = connection.getInputStream();
                response = getStringFromInputStream(is);
                //Log.i("response",response);
                System.out.println(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过get访问网络获取图片资源
     */


    public void getImageRes() {

        try {
            //把传过来的路径转成URL
            URL Url = new URL(NetEnum.serverUrl + url);
            //获取连接
            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            //使用GET方法访问网络
            connection.setRequestMethod("GET");
            //超时时间为10秒
            connection.setConnectTimeout(5000);
            //获取返回码
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream inputStream = connection.getInputStream();
                //网络的输入流生产Bitmap
                bitmap = BitmapFactory.decodeStream(inputStream);
                //System.out.println(bitmap);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    @NonNull
    private String getStringFromInputStream(InputStream is) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = is.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        is.close();
        String html = baos.toString();

        baos.close();
        is.close();

        return html;
    }
}
