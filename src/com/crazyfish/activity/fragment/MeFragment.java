package com.crazyfish.activity.fragment;

import com.crazyfish.activity.ChangeInfoActivity;
import com.crazyfish.activity.CustomerFansActivity;
import com.crazyfish.activity.LoginActivity;
import com.crazyfish.demo.R;
import com.crazyfish.util.PicHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MeFragment extends Fragment {
	private static final int REQUEST_CODE = 1;
	private Button login = null;
	private View view = null;
	private TextView meFans = null;
	private TextView mePost = null;
	private TextView meCollection = null;
	private TextView meFocus = null;
	private TextView meChangeInfo = null;
	private final int CLICK_LOGIN = 1;
	private final int CLICK_FANS = 2;
    private final int CLICK_CHANGE_INFO = 3;
	ImageView imageview;
	Bitmap bitmap;

	public MeFragment() {

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x9527) {
				imageview.setImageBitmap(bitmap);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.me_fragment, container, false);
		// loginReturn();
		return view;
	}

	public void onResume() {
		super.onResume();
		loginReturn();
	}

	// @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loginReturn();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			int tag = (Integer) v.getTag();
			if (tag == CLICK_LOGIN) {
				intent.setClass(getActivity(), LoginActivity.class);
				intent.putExtra("str", "from home");
				startActivityForResult(intent, REQUEST_CODE);
			}else if(tag == CLICK_CHANGE_INFO){
                intent.setClass(getActivity(), ChangeInfoActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            } else {
				Toast.makeText(getActivity(), "登录!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == LoginActivity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				String str = bundle.getString("back");
				Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			}
		}
	}

	private void loginReturn() {
		SharedPreferences preferences;
		preferences = getActivity().getSharedPreferences("loginInfo", 0);
		int iflogin = preferences.getInt("login", 0);
        //int iflogin = 2;
		if (iflogin == 001) {
			String customername = preferences.getString("customerName", null);
			int fansCount = preferences.getInt("fansCount", 0);
			int postCount = preferences.getInt("postCount", 0);
			int collectionCount = preferences.getInt("collectionCount", 0);
			int focusCount = preferences.getInt("focusCount", 0);
			String customerPic = preferences.getString("customerPic", null);

			((TextView) getView().findViewById(R.id.tvTop)).setText("个人中心");
			login = (Button) view.findViewById(R.id.toLogin);
			login.setVisibility(View.GONE);
			imageview = (ImageView) getView().findViewById(R.id.myPic);
			if (customerPic == null || "".equals(customerPic))
				customerPic = "http://p.qq181.com/cms/1210/2012100413195471481.jpg";
			bitmap = PicHandler.getBitmap(customerPic, getActivity());
			bitmap = PicHandler.toRoundCorner(bitmap, 100);
			handler.sendEmptyMessage(0x9527);
			TextView userName = (TextView) getView()
					.findViewById(R.id.userName);
			userName.setText(customername);
			TextView fans = (TextView) getView().findViewById(R.id.fans);
			TextView post = (TextView) getView().findViewById(R.id.myPost);
			TextView collection = (TextView) getView().findViewById(
					R.id.collection);
			TextView focus = (TextView) getView().findViewById(R.id.ifocus);
			String fansStr = "我的粉丝" + "(" + fansCount + ")";
			fans.setText(fansStr);
			String postStr = "我的帖子" + "(" + postCount + ")";
			post.setText(postStr);
			String collectionStr = "我的收藏" + "(" + collectionCount + ")";
			collection.setText(collectionStr);
			String focusStr = "我的关注" + "(" + focusCount + ")";
			focus.setText(focusStr);
			meFans = (TextView) view.findViewById(R.id.fans);
			meFans.setOnClickListener(listener);
			meFans.setTag(CLICK_FANS);
            TextView changeInfo = (TextView) getView().findViewById(
                    R.id.changeInfo);
            changeInfo.setOnClickListener(listener);
            changeInfo.setTag(CLICK_CHANGE_INFO);
		} else {
			imageview = (ImageView) getView().findViewById(R.id.myPic);
			TextView userName = (TextView) getView()
					.findViewById(R.id.userName);
			imageview.setVisibility(View.GONE);
			userName.setVisibility(View.GONE);
			((TextView) getView().findViewById(R.id.tvTop)).setText("个人中心");
			login = (Button) view.findViewById(R.id.toLogin);
			login.setOnClickListener(listener);
			login.setTag(CLICK_LOGIN);

			meFans = (TextView) view.findViewById(R.id.fans);
			mePost = (TextView) view.findViewById(R.id.myPost);
			meCollection = (TextView) view.findViewById(R.id.collection);
			meFocus = (TextView) view.findViewById(R.id.ifocus);
			meChangeInfo = (TextView) view.findViewById(R.id.changeInfo);
			meFans.setOnClickListener(listener);
			mePost.setOnClickListener(listener);
			meCollection.setOnClickListener(listener);
			meFocus.setOnClickListener(listener);
			meChangeInfo.setOnClickListener(listener);
			meFans.setTag(CLICK_FANS);
			mePost.setTag(CLICK_FANS);
			meCollection.setTag(CLICK_FANS);
			meFocus.setTag(CLICK_FANS);
			meChangeInfo.setTag(CLICK_FANS);
		}
	}
}
