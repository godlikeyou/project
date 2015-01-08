package com.crazyfish.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazyfish.demo.R;
import com.crazyfish.util.GETThread;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;
import com.crazyfish.util.POSTThread;
import com.crazyfish.util.PicHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/1/7.
 */
public class MyArticleActivity extends Activity{
    TextView toptv;
    ImageView myPic;
    Bitmap bitmap;
    TextView username;
    TextView postdate;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x9527) {
                myPic.setImageBitmap(bitmap);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_article);
        toptv = (TextView) findViewById(R.id.tvTop);
        username = (TextView) findViewById(R.id.username);
        postdate = (TextView) findViewById(R.id.postdate);
        toptv.setText("我的帖子");
        SharedPreferences preferences;
        preferences = getSharedPreferences("loginInfo", 0);
        String cid = preferences.getString("customerId","");
        String customerPic = preferences.getString("customerPic","");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("customerId",cid));
        params.add(new BasicNameValuePair("currentPage","1"));
        String url = GlobalVariable.URLHEAD + "/article/myarticle";
        myPic = (ImageView)findViewById(R.id.ivGap);
        if (customerPic == null || "".equals(customerPic))
            customerPic = "http://p.qq181.com/cms/1210/2012100413195471481.jpg";
        bitmap = PicHandler.getBitmap(customerPic,MyArticleActivity.this);
        bitmap = PicHandler.toRoundCorner(bitmap, 100);
        handler.sendEmptyMessage(0x9527);
        POSTThread th = new POSTThread(url,params);
        th.startServiceThread();
        String returnResult = th.getResultData();
        Log.v("xxx",returnResult);
        String type = "gContent,gId,gtReccount,gtGoodcount";
        List<Map<String, Object>> tmp = new ArrayList<Map<String, Object>>();
        tmp = JsonCodec.deJson(returnResult, type);
        Log.v("cccccccc",String.valueOf(tmp.size()));
    }
}
