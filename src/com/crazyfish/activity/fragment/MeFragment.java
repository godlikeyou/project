package com.crazyfish.activity.fragment;

import com.crazyfish.activity.ChangeInfoActivity;
import com.crazyfish.activity.LoginActivity;
import com.crazyfish.activity.MyArticleActivity;
import com.crazyfish.demo.R;
import com.crazyfish.util.PicHandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MeFragment extends Fragment {
	private static final int REQUEST_CODE = 1;
	private Button login;
	private View view;
	private TextView meFans;
	private TextView mePost;
	private TextView meCollection;
	private TextView meFocus;
	private TextView meChangeInfo;
	private final int CLICK_LOGIN = 1;
	private final int CLICK_FANS = 2;
    private final int CLICK_CHANGE_INFO = 3;
    private final int CLICK_MYPOST = 4 ;
	ImageView imageview;
    private Resources resources;
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
        ibMe.setBackgroundDrawable(resources.getDrawable(R.drawable.atme));
        TextView tvMeText = (TextView)getActivity().findViewById(R.id.rbMeText);
        tvMeText.setTextColor(resources.getColor(R.color.bottomfocus));
        ImageView ibSetting = (ImageView)getActivity().findViewById(R.id.rbSetting);
        ibSetting.setBackgroundDrawable(resources.getDrawable(R.drawable.setting));
        TextView tvSettingText = (TextView)getActivity().findViewById(R.id.rbSettingText);
        tvSettingText.setTextColor(resources.getColor(R.color.content));
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
            } else if(tag == CLICK_MYPOST){
                intent.setClass(getActivity(), MyArticleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
            else {
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
            post.setOnClickListener(listener);
            post.setTag(CLICK_MYPOST);
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
