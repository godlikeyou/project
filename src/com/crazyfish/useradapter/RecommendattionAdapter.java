package com.crazyfish.useradapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecommendattionAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater listContainer;
    private EditText etInput;
    private LinearLayout llUserPost;
    private LinearLayout bottomList;
    private Button btUserRec;
    private String gid;
    private Handler h;

    public final class RecommendattionView {
        public ImageView ivGap;
        public TextView userName;
        public TextView userRec;
        public TextView recTime;
    }

    public RecommendattionAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("wochacha","xxxxxxxxxxxxxxxx");
        // TODO Auto-generated method stub
        final int selectID = position;
        RecommendattionView view = null;

        if (convertView == null) {
            view = new RecommendattionView();
            convertView = listContainer.inflate(R.layout.onegag_recommendation, null);
            view.ivGap = (ImageView) convertView.findViewById(R.id.userHead);
            view.userName = (TextView) convertView
                    .findViewById(R.id.oneUserName);
            view.userRec = (TextView)convertView.findViewById(R.id.oneUserRec);
            view.recTime = (TextView)convertView.findViewById(R.id.oneRecTime);
            convertView.setTag(view);
        } else {
            view = (RecommendattionView) convertView.getTag();
        }
        Log.i("items",""+listItems.size());
        if (listItems != null) {
            // set user name
            String user = "["
                    + String.valueOf(listItems.get(position).get("customer"))
                    + "]";
            String types = "cId,cName,cNickname,cPurl,cSignature";
            List<Map<String, Object>> ulist = JsonCodec.deJson(user, types);
            view.userName.setText(ulist.get(0).get("cName").toString());
            view.userRec.setText(listItems.get(position).get("grContent").toString());

            String time = "["
                    + listItems.get(position).get("grTime").toString()
                    + "]";
            String types1 = "time";
            List<Map<String, Object>> t = JsonCodec.deJson(time, types1);
            //view.recTime.setText(listItems.get(position).get("grTime").toString());
            String dateStr = t.get(0).get("time").toString();
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long timex=new Long(Long.valueOf(dateStr));
            String d = format.format(timex);

            view.recTime.setText(d);
            Log.i("hhhhh",t.get(0).get("time").toString());
            view.ivGap.setTag(ulist.get(0).get("cPurl").toString());
            if (ulist.get(0).get("cPurl").toString().equals("") || ulist.get(0).get("cPurl").toString().equals(null)) {
                Resources resources = context.getResources();
                Drawable pic = resources.getDrawable(R.drawable.ano);
                view.ivGap.setImageDrawable(pic);
            } else {
                BitmapWorkerTask asyncTask = new BitmapWorkerTask(view.ivGap, context, GlobalVariable.PIC_USER_HEAD);
                asyncTask.execute(ulist.get(0).get("cPurl").toString());
            }
        }
        return convertView;
    }
}
