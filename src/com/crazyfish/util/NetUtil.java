package com.crazyfish.util;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetUtil {
	public static boolean checkNet(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo info = conn.getActiveNetworkInfo();
            
            if(info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }       
        }
        Toast.makeText(context, "请检查您的网络连接", Toast.LENGTH_LONG).show();
        return false;
    }
}
