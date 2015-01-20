package com.crazyfish.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.asynctask.HttpGetTask;
import com.crazyfish.demo.R;
import com.crazyfish.useradapter.RecommendattionAdapter;
import com.crazyfish.util.FileUtils;
import com.crazyfish.util.GETThread;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kamal on 2015/1/14.
 */
public class RecommendationActivity extends Activity implements AbsListView.OnScrollListener{
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
    private TextView oneRecNum;
    private TextView oneGoodNum;
    private Handler handler;
    private int lastVisibleIndex;
    private Button bt;
    private ProgressBar pg;
    private View moreView;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalVariable.HANDLER_GET_CODE:
                    Bundle bundle = msg.getData();
                    result = bundle.getString("result");
                    String rtype = bundle.getString("type");
                    String url1 = GlobalVariable.URLHEAD + "/gagreply/rec/recsize/"+gid;
                    String url2 = GlobalVariable.URLHEAD + "/gagreply/rec/"+gid+"/1";
                    Log.i("handler", result + rtype.equals(url1));
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
                    if (rtype.equals(url1)) {
                        try {
                            size = Integer.parseInt(result);
                            Log.i("recsizes", "" + size);
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
                        bt.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
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
        handler = new Handler();
        image = (Bitmap)getIntent().getParcelableExtra("image");
        gid = (String)getIntent().getStringExtra("gid");
        String name = (String)getIntent().getStringExtra("name");
        String content = (String)getIntent().getStringExtra("content");
        String recNum = (String)getIntent().getStringExtra("recnum");
        String goodNum = (String)getIntent().getStringExtra("goodnum");

        tvOneGag = (TextView)findViewById(R.id.tvTop);
        tvOneGag.setText("一条吐槽");
        header = getLayoutInflater().inflate(R.layout.recheader,null);
        moreView = getLayoutInflater().inflate(
                R.layout.moredata, null);
        bt = (Button)moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar)moreView.findViewById(R.id.pg);
        bt.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        ivPicRec = (ImageView)header.findViewById(R.id.ivPicRec);
        if( image != null)
            ivPicRec.setImageBitmap(image);
        else ivPicRec.setVisibility(View.GONE);
        recHeadName = (TextView)header.findViewById(R.id.recHeadName);
        recHeadName.setText(name);
        recHeadContent = (TextView)header.findViewById(R.id.recHeadContent);
        recHeadContent.setText(content);
        oneRecNum = (TextView)header.findViewById(R.id.oneRecNum);
        oneRecNum.setText(recNum+"评");
        oneGoodNum = (TextView)header.findViewById(R.id.oneGoodNum);
        oneGoodNum.setText(goodNum+"赞");

        lmap = new ArrayList<Map<String, Object>>();
        final ListView lv = (ListView) findViewById(R.id.lvOneAllRec);
        String url1 = GlobalVariable.URLHEAD + "/gagreply/rec/recsize/"+gid;//对gid的评论个数
        HttpGetTask task1 = new HttpGetTask(h);
        task1.execute(url1);
        String url = GlobalVariable.URLHEAD + "/gagreply/rec/"+gid+"/1";//gid评论的第一页
        HttpGetTask task = new HttpGetTask(h);
        task.execute(url);
        recAdapter = new RecommendattionAdapter(RecommendationActivity.this, lmap);
        lv.addHeaderView(header);
        lv.setAdapter(recAdapter);
        lv.addFooterView(moreView);
        lv.setItemsCanFocus(true);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnScrollListener(this);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pg.setVisibility(View.VISIBLE);
                bt.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String rlt = loadMoreData();
                        Toast.makeText(RecommendationActivity.this,rlt,Toast.LENGTH_LONG).show();
                        bt.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        if ("success".equals(rlt)) {
                            Log.i("suc",rlt);
                            recAdapter.notifyDataSetChanged();
                        } else {
                            bt.setText("加载失败");
                        }
                    }
                }, 2000);
            }
        });
    }
    private String loadMoreData() {
        int count = recAdapter.getCount();
        Log.i("currrentsize", (count + GlobalVariable.GAG_PAGE_SIZE) +"xx" + (count+2));
        if (count + GlobalVariable.GAG_PAGE_SIZE <= (size + 2)) {
            int currentPage = count / GlobalVariable.GAG_PAGE_SIZE + 1;
            Log.i("currentpage", "" + currentPage);
            String url = GlobalVariable.URLHEAD + "/gagreply/rec/"+gid+"/"
                    + currentPage;
            GETThread th = new GETThread(url);
            th.startServiceThread();
            String xx = th.getResultData();
            boolean s = false;
            if (xx != null && !xx.equals("timeout")) {
                String type = "grContent,gId,customer,grTime,grId";
                // json decode
                List<Map<String, Object>> tmp = new ArrayList<Map<String, Object>>();
                tmp = JsonCodec.deJson(xx, type);
                lmap.addAll(tmp);
            } else {// request failure
                return "failure";
            }
        }
        return "success";
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        //if (size == 0) {
          //  size = -1;
        //}
        Log.i("ifsame", "" + totalItemCount + (size + 2));
        if (totalItemCount == size + 2) {
            // lv.removeFooterView(moreView);
            if( size != 0) {
                bt.setText("没有更多了");
                bt.setEnabled(false);
            }
        }
        if (size == 1) {
            // lv.removeFooterView(moreView);
            bt.setText("没有更多了");
            bt.setEnabled(false);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == recAdapter.getCount()) {
            // pg.setVisibility(View.VISIBLE);
            // bt.setVisibility(View.GONE);
            // handler.postDelayed(new Runnable() {
            //
            // @Override
            // public void run() {
            // loadMoreDate();
            // bt.setVisibility(View.VISIBLE);
            // pg.setVisibility(View.GONE);
            // mSimpleAdapter.notifyDataSetChanged();
            // }
            //
            // }, 2000);
        }
    }
}
