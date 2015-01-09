package com.crazyfish.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.POSTThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamal on 2015/1/9.
 */
public class TextPostActivity extends Activity{
    private Button btnText = null;
    private EditText etContent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.textpost);
        btnText = (Button)findViewById(R.id.btnPost);
        btnText.setOnClickListener(listener);
    }
    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            etContent = (EditText)findViewById(R.id.etPostContent);
            String content = etContent.getText().toString();
            List<NameValuePair> np = new ArrayList<NameValuePair>();
            np.add(new BasicNameValuePair("content",content));
            np.add(new BasicNameValuePair("uri","n"));
            String link = GlobalVariable.URLHEAD + "/article/post";
            POSTThread pth = new POSTThread(link,np);
            pth.startServiceThread();
            String rlt = pth.getResultData();
            String su  = "\"success\"";
            if( rlt.equals(su)){
                Toast.makeText(TextPostActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(TextPostActivity.this, MainActivity.class);
                setResult(GlobalVariable.RESULT_CODE,intent);
                finish();
            }
            else{
                Toast.makeText(TextPostActivity.this, "提交失败", Toast.LENGTH_LONG).show();
            }
        }
    };
}
