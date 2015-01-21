package com.crazyfish.useradapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazyfish.asynctask.BitmapWorkerTask;
import com.crazyfish.demo.R;
import com.crazyfish.model.UserRec;
import com.crazyfish.util.GlobalVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> list;
    private LayoutInflater layoutInflater;
    private Context context;
    public GridViewAdapter(Context context,List<Map<String, Object>> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    /**
     * 数据总数
     */
    @Override
    public int getCount() {

        return list.size();
    }

    /**
     * 获取当前数据
     */
    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (layoutInflater != null) {
            view = layoutInflater
                    .inflate(R.layout.gridview_item, null);
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.ivRecHead);
            TextView textView = (TextView) view.findViewById(R.id.tvRecUserName);
            //获取自定义的类实例
            //tempGridViewItem = list.get(position).get("item");
            imageView.setTag(list.get(position).get("cPurl").toString());
            BitmapWorkerTask asyncTask = new BitmapWorkerTask(imageView,context, GlobalVariable.PIC_USER_HEAD);
            asyncTask.execute(list.get(position).get("cPurl").toString());
            textView.setText(list.get(position).get("cName").toString());
        }
        return view;
    }

}