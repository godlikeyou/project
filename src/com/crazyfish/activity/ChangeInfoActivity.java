package com.crazyfish.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.crazyfish.demo.R;

/**
 * Created by Administrator on 2015/1/5.
 */
public class ChangeInfoActivity extends Activity{
    TextView username;
    TextView nickname;
    TextView phone;
    TextView email;
    TextView tvTop;
    TextView usersiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_info);
        tvTop = (TextView)this.findViewById(R.id.tvTop);
        tvTop.setText("资料完善");
        username = (TextView)this.findViewById(R.id.name);
        nickname = (TextView)this.findViewById(R.id.nickname);
        phone = (TextView)this.findViewById(R.id.phone);
        email = (TextView)this.findViewById(R.id.email);
        usersiguration = (TextView)this.findViewById(R.id.siguration);
        SharedPreferences preferences;
        preferences = this.getSharedPreferences("loginInfo",0);
        String name = preferences.getString("customerName","");
        String nickName = preferences.getString("customerNickname","");
        String phone1 = preferences.getString("customerPhone","");
        String email1 = preferences.getString("customerEmail","");
        String siguration = preferences.getString("customerSignature","");
        Log.v("xxxxx",name+"cc"+nickname+"bb"+phone+"aa"+email);
        if(name == null || "".equals(name)){
            username.setText("暂无");
        }else
            username.setText(name);
        if(nickName == null || "".equals(nickName)) {
            Log.v("cccc","mm"+nickname);
            nickname.setText("暂无昵称");
        }
        else
            nickname.setText(nickName);
        if(phone1 == null || "".equals(phone1))
            phone.setText("未绑定");
        else
            phone.setText(phone1);
        if(email1 == null || "".equals(email1))
            email.setText("未绑定");
        else
            email.setText(email1);
        if(siguration == null || "".equals(siguration))
            usersiguration.setText("前往设置");
        else
            usersiguration.setText(siguration);

    }
}
