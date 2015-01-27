package com.crazyfish.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazyfish.asynctask.BitmapWorkerTask;
import com.crazyfish.demo.R;
import com.crazyfish.model.SerializableMap;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.PicHandler;

/**
 * Created by kamal on 2015/1/22.
 */
public class UserHomeActivity extends Activity {
    private TextView tvUserName;
    private ImageView ivUserHeadHome;
    private TextView tvSigNatureHome;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_home);
        SerializableMap<String,String> userinfo = (SerializableMap<String,String>)getIntent().getExtras().get("userinfo");
        ((TextView)findViewById(R.id.tvTop)).setText(userinfo.getMap().get("cName")+"的主页");
        tvUserName = (TextView)findViewById(R.id.tvUserNameHome);
        tvUserName.setText(userinfo.getMap().get("cName"));
        ivUserHeadHome = (ImageView)findViewById(R.id.ivUserHeadHome);
        final Bitmap head = PicHandler.toRoundCorner(PicHandler.getBitmap(userinfo.getMap().get("cPurl").toString(),this),100);
        ivUserHeadHome.setImageBitmap(head);
//        ivUserHeadHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(UserHomeActivity.this,ImageShower.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("image",head);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
        tvSigNatureHome = (TextView)findViewById(R.id.tvSignatureHome);
        tvSigNatureHome.setText(userinfo.getMap().get("cSignature"));
    }
}
