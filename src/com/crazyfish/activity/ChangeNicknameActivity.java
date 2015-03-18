package com.crazyfish.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.POSTThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

/**
 * Created by Administrator on 2015/1/6.
 */
public class ChangeNicknameActivity extends Activity{
    EditText editnickname;
    TextView toptv;
    Button sureButton;
    String cid;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_customer_nickname);
        toptv = (TextView) findViewById(R.id.tvTop);
        toptv.setText("修改用户昵称");
        editnickname = (EditText)findViewById(R.id.editnickname);
        preferences = this.getSharedPreferences("loginInfo", 0);
        String nickname = preferences.getString("customerNickname", "");
        cid = preferences.getString("customerId","0");
        editnickname.setText(nickname);
        editnickname.setSelection(nickname.length());
        sureButton = (Button) findViewById(R.id.surebutton);
        sureButton.setOnClickListener(listener);
        ImageView v = (ImageView)findViewById(R.id.ivRefresh);
        v.setVisibility(View.INVISIBLE);
    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String url = GlobalVariable.URLHEAD+"/customer/1.0/update/nickname";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nickname",editnickname.getText().toString()));
            params.add(new BasicNameValuePair("cid",cid));
            POSTThread th = new POSTThread(url,params);
            th.startServiceThread();
            int returnResult = Integer.valueOf(th.getResultData());
            if(returnResult == 0){
                Toast.makeText(getApplicationContext(),
                        "修改昵称成功!", Toast.LENGTH_LONG).show();
                SharedPreferences mySharedPreferences= getSharedPreferences("loginInfo",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("customerNickname",editnickname.getText().toString());
                editor.commit();
                Intent intent = new Intent(ChangeNicknameActivity.this,ChangeInfoActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),
                        "服务器繁忙，请一会重试!", Toast.LENGTH_LONG).show();
            }
        }
    };
}
