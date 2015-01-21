package com.crazyfish.useradapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.crazyfish.activity.RecommendationActivity;
import com.crazyfish.asynctask.BitmapWorkerTask;
import com.crazyfish.demo.R;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;
import com.crazyfish.util.LoginUtil;
import com.crazyfish.util.POSTThread;
import com.crazyfish.util.PicHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

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
        public Button btnMore;
        public TextView tvCollNum;
        public TextView tvRecNum;
        public TextView tvGoodNum;
        //public EditText etInput;
        // public ProgressBar picLoad;
    }

    public AllArticleAdapter(Context context, List<Map<String, Object>> list, EditText etInput, LinearLayout llUserPost,
                             LinearLayout bottomList, Button btPostRec, Handler h) {
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
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences loginInfo = LoginUtil.testLogin(context);
                if(loginInfo == null)return;
                String cid = loginInfo.getString("customerId", null);
                String url = GlobalVariable.URLHEAD + "/collection/addGagCollection";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("uid", cid));
                params.add(new BasicNameValuePair("gid", g));
                POSTThread coll = new POSTThread(url, params);
                coll.startServiceThread();
                String result = coll.getResultData();
                Log.i("result", result);
                if (result.equals("\"success\"")) {
                    Message m = new Message();
                    m.what = GlobalVariable.HANDLER_COLLECTION_CODE;
                    Bundle b = new Bundle();
                    b.putInt("sf", GlobalVariable.SUCCESS);
                    b.putString("gid", g);
                    m.setData(b);
                    h.sendMessage(m);
                }
                if (result.equals("\"failure\"")) {
                    Message m = new Message();
                    m.what = GlobalVariable.HANDLER_COLLECTION_CODE;
                    Bundle b = new Bundle();
                    b.putInt("sf", GlobalVariable.FAILURE);
                    m.setData(b);
                    h.sendMessage(m);
                }
            }
        });
        builder.setNegativeButton("取消", null)
                .show();
    }

    private void goodGag(String gid) {

    }
    /***
     * 按钮被按下
    */
    private final static float[] BUTTON_PRESSED = new float[] {
            2.0f, 0, 0, 0, -50,
            0, 2.0f, 0, 0, -50,
            0, 0, 2.0f, 0, -50,
            0, 0, 0, 5, 0 };

    /**
     * 按钮恢复原状
     */
    private final static float[] BUTTON_RELEASED = new float[] {
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0 };

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
            view.btnMore = (Button)convertView.findViewById(R.id.btnMore);
            view.tvCollNum = (TextView)convertView.findViewById(R.id.tvCollNum);
            view.tvGoodNum = (TextView)convertView.findViewById(R.id.tvGoodNum);
            view.tvRecNum= (TextView)convertView.findViewById(R.id.tvRecNum);
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
            final List<Map<String, Object>> ulist = JsonCodec.deJson(user, types);
            view.userName.setText(ulist.get(0).get("cName").toString());
            view.signature.setText(ulist.get(0).get("cSignature").toString());
            // test
            view.userUpload.setTag(listItems.get(position).get("gPic")
                    .toString());
            view.ivGap.setTag(ulist.get(0).get("cPurl").toString());
            if( listItems.get(position).get("gPic").equals("n")){
                view.userUpload.setVisibility(View.GONE);
            }else{
                view.userUpload.setVisibility(View.VISIBLE);
                BitmapWorkerTask asyncTask = new BitmapWorkerTask(view.userUpload,context,GlobalVariable.PIC_USER_UPLOAD);
                asyncTask.execute(listItems.get(position).get("gPic").toString());
            }
            if( ulist.get(0).get("cPurl").toString().equals("")||ulist.get(0).get("cPurl").toString().equals(null)){
                Resources resources = context.getResources();
                Drawable pic = resources.getDrawable(R.drawable.ano);
                view.ivGap.setImageDrawable(pic);
            }else {
                BitmapWorkerTask asyncTask = new BitmapWorkerTask(view.ivGap, context,GlobalVariable.PIC_USER_HEAD);
                asyncTask.execute(ulist.get(0).get("cPurl").toString());
            }

            // set school name
            String data = "["
                    + String.valueOf(listItems.get(position).get("school"))
                    + "]";
            String paraType = "sId,sName,sCount";
            List<Map<String, Object>> list = JsonCodec.deJson(data, paraType);
            view.title.setText(list.get(0).get("sName").toString());
            view.content.setText((String) listItems.get(position).get(
                    "gContent"));
            view.tvGoodNum.setText(String.valueOf(listItems.get(position).get(
                    "gtGoodcount"))
                    + "赞");
            view.tvRecNum.setText(String.valueOf(listItems.get(position).get(
                    "gtReccount"))
                    + "评");

            final int p = position;
            view.btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectGag(String.valueOf(listItems.get(p).get("gId")));
                }
            });
            view.tvRecNum.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setClass(context, RecommendationActivity.class);
                    Bundle bundle = new Bundle();
                    Bitmap image = PicHandler.getBitmap(listItems.get(p).get("gPic").toString(),context);
                    bundle.putParcelable("image",image);
                    bundle.putString("gid",listItems.get(p).get("gId").toString());
                    bundle.putString("content",listItems.get(p).get("gContent").toString());
                    bundle.putString("name",ulist.get(0).get("cName").toString());
                    bundle.putString("recnum",listItems.get(p).get("gtReccount").toString());
                    bundle.putString("goodnum",listItems.get(p).get("gtGoodcount").toString());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            final AllArticleView finalView = view;
            view.btnRec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //commentPop.dismiss();
                    etInput.setVisibility(View.VISIBLE);
                    llUserPost.setVisibility(View.VISIBLE);
                    gid = String.valueOf(listItems.get(p).get("gId"));
                    btUserRec.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String info = etInput.getText().toString();
                            SharedPreferences sp = LoginUtil.testLogin(context);
                            if( sp == null)return;
                            String uid = sp.getString("customerId", null);
                            String url = GlobalVariable.URLHEAD + "/gagreply/addGagReply";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("gid", gid));
                            params.add(new BasicNameValuePair("uid", uid));
                            params.add(new BasicNameValuePair("content", info));
                            POSTThread pt = new POSTThread(url, params);
                            pt.startServiceThread();
                            String result = pt.getResultData();
                            Log.i("result", result);
                            if (result.equals("\"success\"")) {
                                int recc = Integer.valueOf(listItems.get(p).get("gtReccount").toString());
                                Message m = new Message();
                                m.what = GlobalVariable.HANDLER_REC_CODE;
                                Bundle b = new Bundle();
                                b.putInt("sf", GlobalVariable.SUCCESS);
                                b.putString("gid", gid);
                                b.putInt("recc",recc + 1);
                                m.setData(b);
                                h.sendMessage(m);
                            }
                            if (result.equals("\"failure\"")) {
                                Message m = new Message();
                                m.what = GlobalVariable.HANDLER_REC_CODE;
                                Bundle b = new Bundle();
                                b.putInt("sf", GlobalVariable.FAILURE);
                                m.setData(b);
                                h.sendMessage(m);
                            }
                        }
                    });
                }
            });
            view.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setItems(context.getResources().getStringArray(R.array.more), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            // TODO 自动生成的方法存根
                            System.out.println(arg1);
                            if (arg1 == 0)
                            {
                                AlertDialog.Builder builder2=new AlertDialog.Builder(context);
                                builder2.setTitle("分享到新浪微博");
                                builder2.setMessage("确定分享到新浪微博");
                                builder2.setPositiveButton("确定",new DialogInterface.OnClickListener(){

                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO 自动生成的方法存根
                                        dialog.dismiss();
                                    }
                                });
                                builder2.show();
                            }
                            arg0.dismiss();
                        }
                    });
                    builder.show();
                }
            });
//            view.btnGood.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                        v.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_PRESSED));
//                        v.setBackgroundDrawable(v.getBackground());
//                    }else if(event.getAction() == MotionEvent.ACTION_UP) {
//                        v.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_RELEASED));
//                        v.setBackgroundDrawable(v.getBackground());
//                    }
//                    return false;
//                }
//            });
            view.btnGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("woshi", "hh");
                    gid = String.valueOf(listItems.get(p).get("gId"));
                    SharedPreferences sp = LoginUtil.testLogin(context);
                    if( sp == null) {
                        return;
                    }
                    String uid = sp.getString("customerId", null);
                    String url = GlobalVariable.URLHEAD + "/gagnausea/addGagNausea";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("type", "1"));
                    params.add(new BasicNameValuePair("gid", gid));
                    params.add(new BasicNameValuePair("uid", uid));
                    POSTThread pt = new POSTThread(url, params);
                    pt.startServiceThread();
                    String result = pt.getResultData();
                    Log.i("result", result);
                    if (result.equals("\"success\"")) {
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
                    if (result.equals("\"failure\"")) {
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
}
