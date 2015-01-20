package com.crazyfish.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.activity.fragment.MeFragment;
import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;
import com.crazyfish.util.NetUtil;
import com.crazyfish.util.POSTThread;

public class LoginActivity extends Activity {
	private TextView tv = null;
	private Button btnLogin;
	private EditText customerName;
	private EditText customerPasswd;
	private String url = "http://192.168.0.108:8080/ssm_demo/customer/1.0/customerLogin";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		if( !NetUtil.checkNet(getApplicationContext())){
			Toast.makeText(getApplicationContext(), "连接网络失败", Toast.LENGTH_LONG).show();
		}
		tv = (TextView)findViewById(R.id.otherLogin);
		customerName = (EditText) findViewById(R.id.name);
		customerPasswd = (EditText) findViewById(R.id.passwd);
		
		btnLogin = (Button) findViewById(R.id.Login);
		btnLogin.setOnClickListener(listener);

	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("customer",customerName.getText().toString()));
			params.add(new BasicNameValuePair("passwd",customerPasswd.getText().toString()));
			POSTThread th = new POSTThread(url,params);
			th.startServiceThread();
			String returnResult = th.getResultData();
            Log.v("returnresult==",returnResult);
			try {
				JSONArray ja = new JSONArray(returnResult);
				JSONObject jsonb = ja.getJSONObject(0);
				String result = jsonb.getString("result");
				int fansCount,postCount,collectionCount,focusCount;
				String customername,customerPic;
				if("001".equals(result)){
					fansCount = Integer.valueOf(jsonb.getString("fansCount"));
					postCount = Integer.valueOf(jsonb.getString("postCount"));
					collectionCount = Integer.valueOf(jsonb.getString("collectionCount"));
					focusCount = Integer.valueOf(jsonb.getString("focusCount"));
					customername = jsonb.getString("customerName");
					customerPic = jsonb.getString("customerPic");
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
					editor.putString("customerSignature",jsonb.getString("customerSignature"));
                    editor.putString("customerId",jsonb.getString("customerId"));
                    editor.putInt("login", 001);
					editor.commit();
					finish();
				}else{
					Toast.makeText(getApplicationContext(),
							"登录成功!", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public void goRegister(View v) {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RegisterActivity.class);
		startActivityForResult(intent, GlobalVariable.REQUEST_CODE);
	}

	public void goOtherLogin(View v) {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, OtherLoginActivity.class);
		startActivityForResult(intent, GlobalVariable.REQUEST_CODE);
	}
}
