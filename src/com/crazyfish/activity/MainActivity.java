package com.crazyfish.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.crazyfish.activity.fragment.FindFragment;
import com.crazyfish.activity.fragment.HomeFragment;
import com.crazyfish.activity.fragment.MeFragment;
import com.crazyfish.activity.fragment.SettingFragment;
import com.crazyfish.activity.fragment.TakePhotoFragment;
import com.crazyfish.demo.R;
//import com.crazyfish.extension.HkDialogLoading;

public class MainActivity extends FragmentActivity {
	private static FragmentManager fm;
	private static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();
		initFragment();
		this.dealBottomButtonClickEvent();
	}

	private void initFragment() {
		FragmentTransaction ft = fm.beginTransaction();
		HomeFragment hf = new HomeFragment();
		ft.add(R.id.fragmentRoot, hf, "hf");
		ft.addToBackStack("hf");
		ft.commit();
		// findViewById(R.id.bottomHome).setBackgroundColor(getResources().getColor(R.color.red));
	}

	private void dealBottomButtonClickEvent() {
		findViewById(R.id.rbHome).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fm.findFragmentByTag("hf") != null
						&& fm.findFragmentByTag("hf").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();
			}
		});
		findViewById(R.id.rbFind).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fm.beginTransaction();
				ft.hide(fm.findFragmentByTag("hf"));
				FindFragment ff = new FindFragment();
				ft.add(R.id.fragmentRoot, ff, "ff");
				ft.addToBackStack("ff");
				ft.commit();
			}
		});
		findViewById(R.id.rbTakePhoto).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						popAllFragmentsExceptTheBottomOne();
						FragmentTransaction ft = fm.beginTransaction();
						ft.hide(fm.findFragmentByTag("hf"));
						TakePhotoFragment tf = new TakePhotoFragment();
						ft.add(R.id.fragmentRoot, tf, "tf");
						ft.addToBackStack("tf");
						ft.commit();
					}
				});
		findViewById(R.id.rbMe).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences preferences;
				preferences = getSharedPreferences("loginInfo",
						MODE_WORLD_READABLE);
				int login = preferences.getInt("login", 0);
				if (login != 1) {
					Toast.makeText(getApplicationContext(),
							"可以登录!", Toast.LENGTH_LONG).show();
				}
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fm.beginTransaction();
				ft.hide(fm.findFragmentByTag("hf"));
				MeFragment mf = new MeFragment();
				ft.add(R.id.fragmentRoot, mf, "mf");
				ft.addToBackStack("mf");
				ft.commit();
			}
		});
		findViewById(R.id.rbSetting).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fm.beginTransaction();
				ft.hide(fm.findFragmentByTag("hf"));
				SettingFragment sf = new SettingFragment();
				ft.add(R.id.fragmentRoot, sf, "sf");
				ft.addToBackStack("sf");
				ft.commit();
			}
		});
	}

	protected void popAllFragmentsExceptTheBottomOne() {
		// TODO Auto-generated method stub
		for (int i = 0, count = fm.getBackStackEntryCount() - 1; i < count; i++) {
			fm.popBackStack();
		}
	}

	@Override
	public void onBackPressed() {
		if (fm.findFragmentByTag("hf") != null
				&& fm.findFragmentByTag("hf").isVisible()) {
			MainActivity.this.finish();
		} else {
			super.onBackPressed();
		}
	}

}
