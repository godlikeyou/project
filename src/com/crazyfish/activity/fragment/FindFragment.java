package com.crazyfish.activity.fragment;

import com.crazyfish.demo.R;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FindFragment extends Fragment{
    private Resources resources;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.find_fragment, container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        resources = getResources();
        ImageButton ibHome = (ImageButton)getActivity().findViewById(R.id.rbHome);
        ibHome.setBackgroundDrawable(resources.getDrawable(R.drawable.first));
        ImageButton ibFind = (ImageButton)getActivity().findViewById(R.id.rbFind);
        ibFind.setBackgroundDrawable(resources.getDrawable(R.drawable.atfind));
        ImageButton ibPost = (ImageButton)getActivity().findViewById(R.id.rbTakePhoto);
        ibPost.setBackgroundDrawable(resources.getDrawable(R.drawable.post));
        ImageButton ibMe = (ImageButton)getActivity().findViewById(R.id.rbMe);
        ibMe.setBackgroundDrawable(resources.getDrawable(R.drawable.me));
        ImageButton ibSetting = (ImageButton)getActivity().findViewById(R.id.rbSetting);
        ibSetting.setBackgroundDrawable(resources.getDrawable(R.drawable.setting));
	}
}
