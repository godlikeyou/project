package com.crazyfish.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crazyfish.demo.R;

public class SettingFragment extends Fragment{
	private View view = null;
	private ListView list = null;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		view =  inflater.inflate(R.layout.setting_fragment, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		list = (ListView)getView().findViewById(R.id.btnTest);
		String[] strs = new String[] {
			    "first", "second", "third", "fourth", "fifth"
			    };
		list.setAdapter(new ArrayAdapter<String>(getActivity(),
		                android.R.layout.simple_gallery_item, strs));
	}
}
