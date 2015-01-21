package com.crazyfish.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfish.activity.fragment.FindFragment;
import com.crazyfish.activity.fragment.HomeFragment;
import com.crazyfish.activity.fragment.MeFragment;
import com.crazyfish.activity.fragment.SettingFragment;
import com.crazyfish.activity.fragment.TakePhotoFragment;
import com.crazyfish.demo.R;
import com.crazyfish.extension.ResizeLayout;

import org.w3c.dom.Text;
//import com.crazyfish.extension.HkDialogLoading;

public class MainActivity extends FragmentActivity {
	private static FragmentManager fm;
	private static final int REQUEST_CODE = 1;
    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private static final int HEIGHT_THREADHOLD = 30;
    private Resources resources;
    class InputHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RESIZE: {
                    if (msg.arg1 == BIGGER) {
                        Toast.makeText(MainActivity.this,"隐藏软键盘",Toast.LENGTH_LONG).show();
                        LinearLayout bottomList = (LinearLayout)findViewById(R.id.bottomList);
                        LinearLayout flPost = (LinearLayout)findViewById(R.id.llUserPost);
                        bottomList.setVisibility(View.VISIBLE);
                        flPost.setVisibility(View.INVISIBLE);
                        //findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this,"显示软键盘",Toast.LENGTH_LONG).show();
                        LinearLayout bottomList = (LinearLayout)findViewById(R.id.bottomList);
                        LinearLayout flPost = (LinearLayout)findViewById(R.id.llUserPost);
                        bottomList.setVisibility(View.INVISIBLE);
                        flPost.setVisibility(View.VISIBLE);
                        //findViewById(R.id.bottom_layout).setVisibility(View.GONE);
                    }
                }
                break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private InputHandler mHandler = new InputHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();
		initFragment();
		this.dealBottomButtonClickEvent();

        ResizeLayout layout = (ResizeLayout) findViewById(R.id.resizeLL);
        layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;
                if (h < oldh) {
                    change = SMALLER;
                }

                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = change;
                mHandler.sendMessage(msg);
            }
        });
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
		findViewById(R.id.bottomHome).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fm.findFragmentByTag("hf") != null
						&& fm.findFragmentByTag("hf").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();
                resources = getResources();
                ImageView ibHome = (ImageView)findViewById(R.id.rbHome);
                ibHome.setBackgroundDrawable(resources.getDrawable(R.drawable.atfirst));
                TextView tvHomeText = (TextView)findViewById(R.id.rbHomeText);
                tvHomeText.setTextColor(resources.getColor(R.color.bottomfocus));
                //ibHome.setColor(resources.getColor(R.color.head));
                ImageView ibFind = (ImageView)findViewById(R.id.rbFind);
                ibFind.setBackgroundDrawable(resources.getDrawable(R.drawable.find));
                TextView tvFindText = (TextView)findViewById(R.id.rbFindText);
                tvFindText.setTextColor(resources.getColor(R.color.content));
                ImageView ibPost = (ImageView)findViewById(R.id.rbTakePhoto);
                ibPost.setBackgroundDrawable(resources.getDrawable(R.drawable.post));
                TextView tvPostText = (TextView)findViewById(R.id.rbTakePhotoText);
                tvPostText.setTextColor(resources.getColor(R.color.content));
                ImageView ibMe = (ImageView)findViewById(R.id.rbMe);
                ibMe.setBackgroundDrawable(resources.getDrawable(R.drawable.me));
                TextView tvMeText = (TextView)findViewById(R.id.rbMeText);
                tvMeText.setTextColor(resources.getColor(R.color.content));
                ImageView ibSetting = (ImageView)findViewById(R.id.rbSetting);
                ibSetting.setBackgroundDrawable(resources.getDrawable(R.drawable.setting));
                TextView tvSettingText = (TextView)findViewById(R.id.rbSettingText);
                tvSettingText.setTextColor(resources.getColor(R.color.content));
			}
		});
		findViewById(R.id.bottomFind).setOnClickListener(new OnClickListener() {
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
		findViewById(R.id.bottomCamera).setOnClickListener(
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_MENU){
            /*AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();*/
            super.openOptionsMenu();
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
            LinearLayout post = (LinearLayout)findViewById(R.id.llUserPost);
            LinearLayout bot = (LinearLayout)findViewById(R.id.bottomList);
            if( post.getVisibility() == View.VISIBLE){
                bot.setVisibility(View.VISIBLE);
                post.setVisibility(View.INVISIBLE);
            }
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }
        return true;
    }
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        //在这里做你想做的事情

        super.onOptionsMenuClosed(menu);
    }

    @Override

    public void openOptionsMenu() {
        // TODO Auto-generated method stub
        super.openOptionsMenu();
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        int group1 = 1;
        menu.add(group1, 1, 1, "退出登录");
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case 1: // do something here

                Log.i("MenuTest:", "ItemSelected:1");
                logOut();

                break;

            case 2: // do something here

                Log.i("MenuTest:", "ItemSelected:2");

                break;

            default:

                return super.onOptionsItemSelected(item);

        }

        return true;

    }

    public void logOut(){
        SharedPreferences preferences;
        preferences = getSharedPreferences("loginInfo", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    //弹出对话框
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i("key pressed", String.valueOf(event.getKeyCode()));
        return super.dispatchKeyEvent(event);
    }
}
