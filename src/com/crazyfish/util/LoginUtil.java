package com.crazyfish.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.crazyfish.activity.LoginActivity;

/**
 * Created by kamal on 2015/1/16.
 */
public class LoginUtil {
    public static SharedPreferences testLogin(final Context context){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", 0);
        String cid = sp.getString("customerId",null);
        if( cid == null ) {
            Log.i("zheli","ri");
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("请登录").setMessage("请登录");
            builder.setPositiveButton("登录",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                    Intent intent = new Intent();
                    intent.setClass(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("取消",null).show();
            return null;
        }
        return sp;
    }
}
