package com.crazyfish.useradapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.asynctask.BitmapWorkerTask;
import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;
import com.crazyfish.util.POSTThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllArticleAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater listContainer;
    private EditText etInput;
    private LinearLayout llUserPost;
    private LinearLayout bottomList;
    private Button btUserRec;
    private String gid;
    private Handler h;
    public final class AllArticleView {
        public TextView title;
        public TextView content;
        public Button btnCollect;
        public Button btnGood;
        public Button btnRec;
        public ImageView ivGap;
        public TextView userName;
        public TextView signature;
        public ImageView userUpload;
        //public EditText etInput;
        // public ProgressBar picLoad;
    }

    public AllArticleAdapter(Context context, List<Map<String, Object>> list,EditText etInput,LinearLayout llUserPost,
                             LinearLayout bottomList,Button btPostRec,Handler h) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = list;
        this.etInput = etInput;
        this.llUserPost = llUserPost;
        this.bottomList = bottomList;
        this.btUserRec = btPostRec;
        this.h = h;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (listItems == null) return 0;
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "123.JPG");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

    private void collectGag(String gid) {
        final String g = gid;
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("收藏").setMessage("是否收藏?");
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                SharedPreferences loginInfo = context.getSharedPreferences("loginInfo", 0);
                String cid = loginInfo.getString("customerId", null);
                String url = GlobalVariable.URLHEAD + "/collection/addGagCollection";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("uid",cid));
                params.add(new BasicNameValuePair("gid",g));
                POSTThread coll = new POSTThread(url, params);
                coll.startServiceThread();
                String result = coll.getResultData();
                Log.i("result",result);
                if( result.equals("\"success\"")){
                    Message m = new Message();
                    m.what = GlobalVariable.HANDLER_COLLECTION_CODE;
                    Bundle b = new Bundle();
                    b.putInt("sf",GlobalVariable.SUCCESS);
                    b.putString("gid",g);
                    m.setData(b);
                    h.sendMessage(m);
                }
                if( result.equals("\"failure\"")){
                    Message m = new Message();
                    m.what = GlobalVariable.HANDLER_COLLECTION_CODE;
                    Bundle b = new Bundle();
                    b.putInt("sf",GlobalVariable.FAILURE);
                    m.setData(b);
                    h.sendMessage(m);
                }
            }
        });
        builder.setNegativeButton("取消", null)
                .show();
    }
    private void goodGag(String gid){

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final int selectID = position;
        AllArticleView view = null;

        if (convertView == null) {
            view = new AllArticleView();
            convertView = listContainer.inflate(R.layout.all_article, null);
            view.title = (TextView) convertView.findViewById(R.id.title);
            view.content = (TextView) convertView.findViewById(R.id.content);
            view.btnCollect = (Button) convertView
                    .findViewById(R.id.btnCollect);
            view.btnGood = (Button) convertView.findViewById(R.id.btnGood);
            view.btnRec = (Button) convertView.findViewById(R.id.btnRec);
            view.ivGap = (ImageView) convertView.findViewById(R.id.ivGap);
            view.userName = (TextView) convertView
                    .findViewById(R.id.tvUserName);
            view.signature = (TextView) convertView
                    .findViewById(R.id.tvSignature);
            view.userUpload = (ImageView) convertView
                    .findViewById(R.id.ivUserUpload);
            //view.etInput = (EditText)convertView.findViewById(R.id.etInput);
            // view.picLoad = (ProgressBar) convertView
            // .findViewById(R.id.pbPicLoad);
            convertView.setTag(view);
        } else {
            view = (AllArticleView) convertView.getTag();
        }
        if (listItems != null) {
            // set user name
            String user = "["
                    + String.valueOf(listItems.get(position).get("customer"))
                    + "]";
            String types = "cId,cName,cNickname,cPurl,cSignature";
            List<Map<String, Object>> ulist = JsonCodec.deJson(user, types);
            view.userName.setText(ulist.get(0).get("cName").toString());
            view.signature.setText(ulist.get(0).get("cSignature").toString());
            // test
            view.userUpload.setTag(listItems.get(position).get("gPic")
                    .toString());
            view.ivGap.setTag(ulist.get(0).get("cPurl").toString());

            BitmapWorkerTask asyncTask = new BitmapWorkerTask(view.userUpload,
                    view.ivGap, context);
            asyncTask.execute(listItems.get(position).get("gPic").toString(),
                    ulist.get(0).get("cPurl").toString());

            // set school name
            String data = "["
                    + String.valueOf(listItems.get(position).get("school"))
                    + "]";
            String paraType = "sId,sName,sCount";
            List<Map<String, Object>> list = JsonCodec.deJson(data, paraType);
            view.title.setText(list.get(0).get("sName").toString());
            view.content.setText((String) listItems.get(position).get(
                    "gContent"));
            view.btnGood.setText(String.valueOf(listItems.get(position).get(
                    "gtGoodcount"))
                    + "赞");
            view.btnRec.setText(String.valueOf(listItems.get(position).get(
                    "gtReccount"))
                    + "评");
            final int p = position;
            view.btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectGag(String.valueOf(listItems.get(p).get("gId")));
                }
            });
            view.btnGood.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    goodGag(String.valueOf(listItems.get(p).get("gId")));
                }
            });
            final AllArticleView finalView = view;
            view.btnRec.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //commentPop.dismiss();
                    etInput.setVisibility(View.VISIBLE);
                    llUserPost.setVisibility(View.VISIBLE);
                    gid = String.valueOf(listItems.get(p).get("gId"));
                    btUserRec.setOnClickListener(RecListener);
                }
            });
            view.btnGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("woshi","hh");
                    gid = String.valueOf(listItems.get(p).get("gId"));
                    SharedPreferences sp = context.getSharedPreferences("loginInfo",0);
                    String uid = sp.getString("customerId",null);
                    String url = GlobalVariable.URLHEAD + "/gagnausea/addGagNausea";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("type","1"));
                    params.add(new BasicNameValuePair("gid",gid));
                    params.add(new BasicNameValuePair("uid",uid));
                    POSTThread pt = new POSTThread(url,params);
                    pt.startServiceThread();
                    String result = pt.getResultData();
                    Log.i("result",result);
                    if( result.equals("\"success\"")){
                        int goodc = Integer.valueOf(listItems.get(p).get("gtGoodcount").toString());
                        Message m = new Message();
                        m.what = GlobalVariable.HANDLER_GOOD_CODE;
                        Bundle b = new Bundle();
                        b.putInt("sf", GlobalVariable.SUCCESS);
                        b.putInt("goodc", goodc + 1);
                        b.putString("gid", gid);
                        m.setData(b);
                        h.sendMessage(m);
                    }
                    if( result.equals("\"failure\"")){
                        Message m = new Message();
                        m.what = GlobalVariable.HANDLER_GOOD_CODE;
                        Bundle b = new Bundle();
                        b.putInt("sf", GlobalVariable.FAILURE);
                        m.setData(b);
                        h.sendMessage(m);
                    }
                }
            });
        }
        return convertView;
    }
    private View.OnClickListener RecListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String info = etInput.getText().toString();
            SharedPreferences sp = context.getSharedPreferences("loginInfo",0);
            String uid = sp.getString("customerId",null);
            String url = GlobalVariable.URLHEAD + "/gagreply/addGagReply";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("gid",gid));
            params.add(new BasicNameValuePair("uid",uid));
            params.add(new BasicNameValuePair("content",info));
            POSTThread pt = new POSTThread(url,params);
            pt.startServiceThread();
            String result = pt.getResultData();
            Log.i("result",result);
            if( result.equals("\"success\"")){
                Message m = new Message();
                m.what = GlobalVariable.HANDLER_REC_CODE;
                Bundle b = new Bundle();
                b.putInt("sf",GlobalVariable.SUCCESS);
                b.putString("gid",gid);
                m.setData(b);
                h.sendMessage(m);
            }
            if( result.equals("\"failure\"")){
                Message m = new Message();
                m.what = GlobalVariable.HANDLER_REC_CODE;
                Bundle b = new Bundle();
                b.putInt("sf",GlobalVariable.FAILURE);
                m.setData(b);
                h.sendMessage(m);
            }
        }
    };
}
