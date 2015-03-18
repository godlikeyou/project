package com.crazyfish.activity;

import com.crazyfish.activity.fragment.MeFragment;
import com.crazyfish.demo.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class CustomerHomeActivity extends FragmentActivity {
	private static FragmentManager fm;
	private int fansCount;
	private int postCount;
	private int collectionCount;
	private int focusCount;
	private String customerName;
	private String customerPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main); 
        Intent intent = getIntent();
      	Bundle bundle = intent.getExtras();
      	fansCount = bundle.getInt("fansCount");
      	postCount = bundle.getInt("postCount");
      	collectionCount = bundle.getInt("collectionCount");
      	focusCount = bundle.getInt("focusCount");
      	customerName = bundle.getString("customerName");
      	customerPic = bundle.getString("customerPic");
        ImageView v = (ImageView)findViewById(R.id.ivRefresh);
        v.setVisibility(View.INVISIBLE);
        fm = getSupportFragmentManager();
        initFragment();
    }
   
    private void initFragment(){
    	FragmentTransaction ft = fm.beginTransaction();
    	//MeFragment hf = new MeFragment(fansCount,postCount,collectionCount,focusCount,customerName,customerPic);
    	//ft.add(R.id.fragmentRoot,hf,"hf");
    	//ft.addToBackStack("hf");
    	//ft.commit();
    	//findViewById(R.id.bottomHome).setBackgroundColor(getResources().getColor(R.color.red));
    }
}
