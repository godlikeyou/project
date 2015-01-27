package com.crazyfish.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crazyfish.demo.R;
import com.crazyfish.util.POSTThread;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private Button registerButton;
	private EditText custName;
	private EditText passwd;
	private EditText repasswd;
	private String url = "http://192.168.0.162:8080/ssm_demo/customer/1.0/customerRegister";
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
        ((TextView)findViewById(R.id.tvTop)).setText("用户注册");
		registerButton = (Button) findViewById(R.id.Register);
		registerButton.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			passwd = (EditText) findViewById(R.id.passwd);
			repasswd = (EditText) findViewById(R.id.rePasswd);
			if(!passwd.getText().toString().equals(repasswd.getText().toString())){
				Toast.makeText(getApplicationContext(),
						"注册!", Toast.LENGTH_LONG).show();
			}else{
				custName = (EditText) findViewById(R.id.name);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("customer",custName.getText().toString()));
				params.add(new BasicNameValuePair("passwd",passwd.getText().toString()));
				POSTThread th = new POSTThread(url,params);
				th.startServiceThread();
				String returnResult = th.getResultData();
				try {
					JSONArray ja = new JSONArray(returnResult);
					JSONObject jsonb = ja.getJSONObject(0);
					String result = jsonb.getString("result");
                    Log.v("rsulll===",result);
                    int fansCount,postCount,collectionCount,focusCount;
					String customername,customerPic;
					if("001".equals(result)){
						fansCount = Integer.valueOf(jsonb.getString("fansCount"));
						postCount = Integer.valueOf(jsonb.getString("postCount"));
						collectionCount = Integer.valueOf(jsonb.getString("collectionCount"));
						focusCount = Integer.valueOf(jsonb.getString("focusCount"));
						customername = jsonb.getString("customerName");
						customerPic = jsonb.getString("customerPic");
						Intent intent = new Intent();
						SharedPreferences mySharedPreferences= getSharedPreferences("loginInfo", 
								Activity.MODE_PRIVATE);
						Editor editor = mySharedPreferences.edit();
						editor.putString("customerName", customername);
						editor.putInt("fansCount", fansCount);
						editor.putInt("postCount", postCount);
						editor.putInt("collectionCount", collectionCount);
						editor.putInt("focusCount", focusCount);
						editor.putString("customerPic", customerPic);
                        editor.putString("customerNickname", jsonb.getString("customerNickname"));
                        editor.putString("customerPhone", jsonb.getString("customerPhone"));
                        editor.putString("customerEmail", jsonb.getString("customerEmail"));
						editor.putInt("login", 001);
						editor.commit();
						intent.setClass(RegisterActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}else if("002".equals(result)){
						Toast.makeText(getApplicationContext(),
								"成功!", Toast.LENGTH_LONG).show();
					}else if("003".equals(result)){
						Toast.makeText(getApplicationContext(),
								"失败!", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
}
