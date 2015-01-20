package com.crazyfish.activity.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.crazyfish.demo.R;

public class SettingFragment extends Fragment{
	private View view = null;
	private ListView list = null;
    private Resources resources;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		view =  inflater.inflate(R.layout.setting_fragment, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        resources = getResources();
        ImageButton ibHome = (ImageButton)getActivity().findViewById(R.id.rbHome);
        ibHome.setBackgroundDrawable(resources.getDrawable(R.drawable.first));
        ImageButton ibFind = (ImageButton)getActivity().findViewById(R.id.rbFind);
        ibFind.setBackgroundDrawable(resources.getDrawable(R.drawable.find));
        ImageButton ibPost = (ImageButton)getActivity().findViewById(R.id.rbTakePhoto);
        ibPost.setBackgroundDrawable(resources.getDrawable(R.drawable.post));
        ImageButton ibMe = (ImageButton)getActivity().findViewById(R.id.rbMe);
        ibMe.setBackgroundDrawable(resources.getDrawable(R.drawable.me));
        ImageButton ibSetting = (ImageButton)getActivity().findViewById(R.id.rbSetting);
        ibSetting.setBackgroundDrawable(resources.getDrawable(R.drawable.atsetting));
		list = (ListView)getView().findViewById(R.id.btnTest);
		String[] strs = new String[] {
			    "first", "second", "third", "fourth", "fifth"
			    };
		list.setAdapter(new ArrayAdapter<String>(getActivity(),
		                android.R.layout.simple_gallery_item, strs));
	}
}
