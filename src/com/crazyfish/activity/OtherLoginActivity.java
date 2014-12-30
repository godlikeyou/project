package com.crazyfish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.crazyfish.demo.R;

public class OtherLoginActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.otherlogin);
	}
}
