package com.crazyfish.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.POSTThread;
import com.crazyfish.util.PicHandler;

public class PostActivity extends Activity{
	private ImageView image = null;
	private Bitmap b = null;
	private String uri = null;
	private Button btnPost = null;
	private EditText etContent = null;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Bitmap b = (Bitmap)getIntent().getParcelableExtra("image");
		setContentView(R.layout.post);
		uri = (String)getIntent().getStringExtra("uri");
		image = (ImageView)findViewById(R.id.image);
		b = (Bitmap)getIntent().getParcelableExtra("image");
        image.setImageBitmap(b);
        btnPost = (Button)findViewById(R.id.btnPost);
        btnPost.setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			etContent = (EditText)findViewById(R.id.etPostContent);
			String content = etContent.getText().toString();
			List<NameValuePair> np = new ArrayList<NameValuePair>();
			np.add(new BasicNameValuePair("content",content));
			np.add(new BasicNameValuePair("uri",uri));
			String link = GlobalVariable.URLHEAD + "/article/post";
			POSTThread pth = new POSTThread(link,np);
			pth.startServiceThread();
			String rlt = pth.getResultData();
			String su  = "\"success\"";
			if( rlt.equals(su)){
				Toast.makeText(PostActivity.this, "提交成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(PostActivity.this, MainActivity.class);
				setResult(GlobalVariable.RESULT_CODE,intent);
				finish();
			}
			else{
				Toast.makeText(PostActivity.this, "提交失败", Toast.LENGTH_LONG).show();
			}
		}
	};
}
