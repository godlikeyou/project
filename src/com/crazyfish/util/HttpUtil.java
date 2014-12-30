package com.crazyfish.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.BitmapDrawable;

public class HttpUtil {
	public static HttpResponse getHttpResponse(String url)
            throws ClientProtocolException, IOException {

        // post请求
        HttpPost post = new HttpPost(url);
        // 请求客户
        DefaultHttpClient client = new DefaultHttpClient();
        return client.execute(post);
    }

    /***
     * 获取logo
     * 
     * @param url
     * @return
     */
    public static BitmapDrawable getImageFromUrl(String action) {
        BitmapDrawable icon = null;
        try {

            URL url = new URL("http://img.159song.com/picture/area/wanda.gif");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            icon = new BitmapDrawable(hc.getInputStream());
            hc.disconnect();
        } catch (Exception e) {
        }
        return icon;
    }
}
