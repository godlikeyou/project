package com.crazyfish.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.asynctask.HttpGetTask;
import com.crazyfish.demo.R;
import com.crazyfish.useradapter.RecommendattionAdapter;
import com.crazyfish.util.FileUtils;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kamal on 2015/1/14.
 */
public class RecommendationActivity extends Activity{
    private Bitmap image;
    private ImageView ivPicRec;
    private TextView tvOneGag;
    private List<Map<String,Object>> lmap;
    private RecommendattionAdapter recAdapter;
    private int size;//
    private String result;
    private String gid;
    private View header;
    private TextView recHeadName;
    private TextView recHeadContent;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalVariable.HANDLER_GET_CODE:
                    Bundle bundle = msg.getData();
                    result = bundle.getString("result");
                    String rtype = bundle.getString("type");
                    String url1 = GlobalVariable.URLHEAD + "/article/gagsize";
                    String url = GlobalVariable.URLHEAD + "/article/allarticle/1";
                    String url2 = GlobalVariable.URLHEAD + "/gagreply/rec/"+gid+"/1";
                    Log.i("handler", result + rtype);
                    if (result.equals("timeout")) {
                        Toast.makeText(getApplication(), "加载失败，为您加载了历史浏览信息，请检查网络",
                                Toast.LENGTH_LONG).show();
                        String link = GlobalVariable.FILE_CACHE_LOCATION + File.separator
                                + "allgag";
                        String str = FileUtils.readCacheFile(link);
                        Log.i("ifdata", str);
                        String type = "grContent,gId,customer,grTime,grId";
                        // json decode
                        lmap = JsonCodec.deJson(str, type);
                        final ListView lv = (ListView)findViewById(R.id.lvOneAllRec);
                        recAdapter = new RecommendattionAdapter(getApplication(), lmap);
                        lv.setAdapter(recAdapter);
                        break;
                    }
                    if (rtype.equals(url1)||rtype.equals(url)) {
                        try {
                            size = Integer.parseInt(result);
                            Log.i("hhwwyy", "" + size);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else if (rtype.equals(url2)) {
                        if (FileUtils.fileCache("allgag", result) == null) {
                        }
                        String type = "grContent,gId,customer,grTime,grId";
                        // json decode
                        List<Map<String, Object>> tmp = JsonCodec.deJson(result,
                                type);
                        Log.i("size", "" + tmp.size());
                        lmap.clear();
                        lmap.addAll(tmp);
                        recAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recommendation);

        image = (Bitmap)getIntent().getParcelableExtra("image");
        gid = (String)getIntent().getStringExtra("gid");
        String name = (String)getIntent().getStringExtra("name");
        String content = (String)getIntent().getStringExtra("content");

        tvOneGag = (TextView)findViewById(R.id.tvTop);
        tvOneGag.setText("一条吐槽");
        header = getLayoutInflater().inflate(R.layout.recheader,null);
        ivPicRec = (ImageView)header.findViewById(R.id.ivPicRec);
        ivPicRec.setImageBitmap(image);
        recHeadName = (TextView)header.findViewById(R.id.recHeadName);
        recHeadName.setText(name);
        recHeadContent = (TextView)header.findViewById(R.id.recHeadContent);
        recHeadContent.setText(content);

        lmap = new ArrayList<Map<String, Object>>();
        final ListView lv = (ListView) findViewById(R.id.lvOneAllRec);
        //String url1 = GlobalVariable.URLHEAD + "/addGagReply/recsize/"+gid;//对gid的评论个数
        //HttpGetTask task1 = new HttpGetTask(h);
        //task1.execute(url1);
        String url = GlobalVariable.URLHEAD + "/gagreply/rec/"+gid+"/1";//gid评论的第一页
        HttpGetTask task = new HttpGetTask(h);
        task.execute(url);
        recAdapter = new RecommendattionAdapter(RecommendationActivity.this, lmap);
        lv.addHeaderView(header);
        lv.setAdapter(recAdapter);
    }
}
