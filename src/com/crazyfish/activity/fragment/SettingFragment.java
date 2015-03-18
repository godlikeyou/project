package com.crazyfish.activity.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crazyfish.demo.R;

public class SettingFragment extends Fragment{
	private View view = null;
    private Resources resources;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		view =  inflater.inflate(R.layout.setting_fragment, container, false);
        ((TextView)view.findViewById(R.id.tvTop)).setText("设置");
        ImageView v = (ImageView)view.findViewById(R.id.ivRefresh);
        v.setVisibility(View.INVISIBLE);
		return view;
	}
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
        ibFind.setBackgroundDrawable(resources.getDrawable(R.drawable.find));
        TextView tvFindText = (TextView)getActivity().findViewById(R.id.rbFindText);
        tvFindText.setTextColor(resources.getColor(R.color.content));
        ImageView ibPost = (ImageView)getActivity().findViewById(R.id.rbTakePhoto);
        ibPost.setBackgroundDrawable(resources.getDrawable(R.drawable.post));
        TextView tvPostText = (TextView)getActivity().findViewById(R.id.rbTakePhotoText);
        tvPostText.setTextColor(resources.getColor(R.color.content));
        ImageView ibMe = (ImageView)getActivity().findViewById(R.id.rbMe);
        ibMe.setBackgroundDrawable(resources.getDrawable(R.drawable.me));
        TextView tvMeText = (TextView)getActivity().findViewById(R.id.rbMeText);
        tvMeText.setTextColor(resources.getColor(R.color.content));
        ImageView ibSetting = (ImageView)getActivity().findViewById(R.id.rbSetting);
        ibSetting.setBackgroundDrawable(resources.getDrawable(R.drawable.atsetting));
        TextView tvSettingText = (TextView)getActivity().findViewById(R.id.rbSettingText);
        tvSettingText.setTextColor(resources.getColor(R.color.bottomfocus));
	}
}
