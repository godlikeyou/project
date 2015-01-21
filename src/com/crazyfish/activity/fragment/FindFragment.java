package com.crazyfish.activity.fragment;

import com.crazyfish.asynctask.HttpGetTask;
import com.crazyfish.demo.R;
import com.crazyfish.extension.EListView;
import com.crazyfish.model.UserRec;
import com.crazyfish.useradapter.AllArticleAdapter;
import com.crazyfish.useradapter.GridViewAdapter;
import com.crazyfish.util.FileUtils;
import com.crazyfish.util.GlobalVariable;
import com.crazyfish.util.JsonCodec;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindFragment extends Fragment{
    private Resources resources;
    private List<Map<String, Object>> lmap;
    private View view;
    private Handler handler;
    private int size;//
    private String result;
    private GridView gridView;
    private GridViewAdapter userAdapter;
    private TextView recTitle;
    private TextView hotTitle;
    private LinearLayout recTitleLine;
    private LinearLayout hotTitleLine;
    private TextView findSome;
    private LinearLayout findSomeLine;
    private ProgressBar userRecPro;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        view = inflater.inflate(R.layout.find_fragment, container,false);
		return view;
	}
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalVariable.HANDLER_GET_CODE:
                    Bundle bundle = msg.getData();
                    result = bundle.getString("result");
                    String rtype = bundle.getString("type");
                    String url1 = GlobalVariable.URLHEAD + "/customer/customersize";
                    String url = GlobalVariable.URLHEAD + "/customer/user/alluser";
                    Log.i("handler", result + rtype);
                    if (result.equals("timeout")) {
                        Toast.makeText(getActivity(), "加载失败，为您加载了历史浏览信息，请检查网络",
                                Toast.LENGTH_LONG).show();
                        String link = GlobalVariable.FILE_CACHE_LOCATION + File.separator
                                + "usertmp";
                        String str = FileUtils.readCacheFile(link);
                        Log.i("ifdata", str);
                        String type = "cId,cName,cNickname,cPurl,cSignature";
                        // json decode
                        lmap = JsonCodec.deJson(str, type);
                        final EListView lv = (EListView) view.findViewById(R.id.allArticle);
                        userAdapter = new GridViewAdapter(getActivity(), lmap);
                        lv.setAdapter(userAdapter);
                        break;
                    }
                    if (rtype.equals(url1)) {
                        try {
                            size = Integer.parseInt(result);
                            Log.i("xxxxxxxxxxx", "" + size);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else if (rtype.equals(url)) {
                        if (FileUtils.fileCache("usertmp", result) == null) {
                        }
                        String type = "cId,cName,cNickname,cPurl,cSignature";
                        // json decode
                        List<Map<String, Object>> tmp = JsonCodec.deJson(result,
                                type);
                        Log.i("size", "" + tmp.size());
                        lmap.clear();
                        lmap.addAll(tmp);
                        userRecPro.setVisibility(View.GONE);
                        userAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        resources = getResources();
        ImageView ibHome = (ImageView)getActivity().findViewById(R.id.rbHome);
        ibHome.setBackgroundDrawable(resources.getDrawable(R.drawable.first));
        TextView tvHomeText = (TextView)getActivity().findViewById(R.id.rbHomeText);
        tvHomeText.setTextColor(resources.getColor(R.color.content));
        //ibHome.setColor(resources.getColor(R.color.head));
        ImageView ibFind = (ImageView)getActivity().findViewById(R.id.rbFind);
        ibFind.setBackgroundDrawable(resources.getDrawable(R.drawable.atfind));
        TextView tvFindText = (TextView)getActivity().findViewById(R.id.rbFindText);
        tvFindText.setTextColor(resources.getColor(R.color.bottomfocus));
        ImageView ibPost = (ImageView)getActivity().findViewById(R.id.rbTakePhoto);
        ibPost.setBackgroundDrawable(resources.getDrawable(R.drawable.post));
        TextView tvPostText = (TextView)getActivity().findViewById(R.id.rbTakePhotoText);
        tvPostText.setTextColor(resources.getColor(R.color.content));
        ImageView ibMe = (ImageView)getActivity().findViewById(R.id.rbMe);
        ibMe.setBackgroundDrawable(resources.getDrawable(R.drawable.me));
        TextView tvMeText = (TextView)getActivity().findViewById(R.id.rbMeText);
        tvMeText.setTextColor(resources.getColor(R.color.content));
        ImageView ibSetting = (ImageView)getActivity().findViewById(R.id.rbSetting);
        ibSetting.setBackgroundDrawable(resources.getDrawable(R.drawable.setting));
        TextView tvSettingText = (TextView)getActivity().findViewById(R.id.rbSettingText);
        tvSettingText.setTextColor(resources.getColor(R.color.content));

        recTitle = (TextView)getActivity().findViewById(R.id.recTitle);
        recTitleLine = (LinearLayout)getActivity().findViewById(R.id.recTitleLine);
        hotTitle = (TextView)getActivity().findViewById(R.id.hotTitle);
        hotTitleLine = (LinearLayout)getActivity().findViewById(R.id.hotTitleLine);
        findSome = (TextView)getActivity().findViewById(R.id.findSome);
        findSomeLine = (LinearLayout)getActivity().findViewById(R.id.findSomeLine);
        recTitle.setTextColor(resources.getColor(R.color.head));
        recTitleLine.setBackgroundColor(resources.getColor(R.color.head));
        recTitle.setOnClickListener(recUser);
        hotTitle.setOnClickListener(hotGag);
        findSome.setOnClickListener(findSomething);

        userRecPro = (ProgressBar)getActivity().findViewById(R.id.userRecPro);

        gridView = (GridView) getActivity().findViewById(R.id.recPerson);
        //String url1 = GlobalVariable.URLHEAD + "/customer/customersize";
        //HttpGetTask task1 = new HttpGetTask(h);
        //task1.execute(url1);
        String url = GlobalVariable.URLHEAD + "/customer/user/alluser";
        HttpGetTask task = new HttpGetTask(h);
        task.execute(url);
        lmap = new ArrayList<Map<String, Object>>();

        userAdapter = new GridViewAdapter(getActivity(), lmap);
        gridView.setAdapter(userAdapter);
	}
    public View.OnClickListener hotGag =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/puretext";
//            HttpGetTask task1 = new HttpGetTask(h);
//            task1.execute(url3);
//            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
//            HttpGetTask task = new HttpGetTask(h);
//            task.execute(url4);

            recTitle.setTextColor(resources.getColor(R.color.content));
            recTitleLine.setBackgroundColor(resources.getColor(R.color.white));

            hotTitle.setTextColor(resources.getColor(R.color.head));
            hotTitleLine.setBackgroundColor(resources.getColor(R.color.head));

            findSome.setTextColor(resources.getColor(R.color.content));
            findSomeLine.setBackgroundColor(resources.getColor(R.color.white));
        }
    };
    public View.OnClickListener recUser =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/puretext";
//            HttpGetTask task1 = new HttpGetTask(h);
//            task1.execute(url3);
//            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
//            HttpGetTask task = new HttpGetTask(h);
//            task.execute(url4);

            recTitle.setTextColor(resources.getColor(R.color.head));
            recTitleLine.setBackgroundColor(resources.getColor(R.color.head));

            hotTitle.setTextColor(resources.getColor(R.color.content));
            hotTitleLine.setBackgroundColor(resources.getColor(R.color.white));

            findSome.setTextColor(resources.getColor(R.color.content));
            findSomeLine.setBackgroundColor(resources.getColor(R.color.white));
        }
    };
    public View.OnClickListener findSomething =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url3 = GlobalVariable.URLHEAD
                    + "/article/gagsize/puretext";
//            HttpGetTask task1 = new HttpGetTask(h);
//            task1.execute(url3);
//            String url4 = GlobalVariable.URLHEAD + "/article/allarticle/puretext/1";
//            HttpGetTask task = new HttpGetTask(h);
//            task.execute(url4);

            recTitle.setTextColor(resources.getColor(R.color.content));
            recTitleLine.setBackgroundColor(resources.getColor(R.color.white));

            hotTitle.setTextColor(resources.getColor(R.color.content));
            hotTitleLine.setBackgroundColor(resources.getColor(R.color.white));

            findSome.setTextColor(resources.getColor(R.color.head));
            findSomeLine.setBackgroundColor(resources.getColor(R.color.head));
        }
    };
}
