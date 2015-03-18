package com.crazyfish.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazyfish.demo.R;

/**
 * Created by Administrator on 2015/1/5.
 */
public class ChangeInfoActivity extends Activity {
    TextView username;
    TextView nickname;
    TextView phone;
    TextView email;
    TextView tvTop;
    TextView usersiguration;
    private final int CLICK_NiCKNAME = 1;
    private final int CLICK_SIGNATURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_info);
        tvTop = (TextView) this.findViewById(R.id.tvTop);
        tvTop.setText("资料完善");
        ImageView v = (ImageView)findViewById(R.id.ivRefresh);
        v.setVisibility(View.INVISIBLE);
        username = (TextView) this.findViewById(R.id.name);
        nickname = (TextView) this.findViewById(R.id.nickname);
        phone = (TextView) this.findViewById(R.id.phone);
        email = (TextView) this.findViewById(R.id.email);
        usersiguration = (TextView) this.findViewById(R.id.siguration);
        SharedPreferences preferences;
        preferences = this.getSharedPreferences("loginInfo", 0);
        String name = preferences.getString("customerName", "");
        String nickName = preferences.getString("customerNickname", "");
        String phone1 = preferences.getString("customerPhone", "");
        String email1 = preferences.getString("customerEmail", "");
        String siguration = preferences.getString("customerSignature", "");
        Log.v("xxxxx", name + "cc" + nickname + "bb" + phone + "aa" + email);
        if (name == null || "".equals(name)) {
            username.setText("暂无");
        } else
            username.setText(name);
        if (nickName == null || "".equals(nickName)) {
            Log.v("cccc", "mm" + nickname);
            nickname.setText("暂无昵称");
        } else
            nickname.setText(nickName);
        if (phone1 == null || "".equals(phone1))
            phone.setText("未绑定");
        else
            phone.setText(phone1);
        if (email1 == null || "".equals(email1))
            email.setText("未绑定");
        else
            email.setText(email1);
        if (siguration == null || "".equals(siguration))
            usersiguration.setText("前往设置");
        else
            usersiguration.setText(siguration);
        nickname.setOnClickListener(listener);
        nickname.setTag(CLICK_NiCKNAME);
        usersiguration.setOnClickListener(listener);
        usersiguration.setTag(CLICK_SIGNATURE);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //editnick = (EditText) findViewById(R.id.editnickname);
            //editnick.setVisibility(View.VISIBLE);
            //nickname.setVisibility(View.GONE);
            Intent intent = new Intent();
            int tag = (Integer) v.getTag();
            if(tag == CLICK_NiCKNAME) {
                intent.setClass(ChangeInfoActivity.this, ChangeNicknameActivity.class);
            }else if(tag == CLICK_SIGNATURE){
                intent.setClass(ChangeInfoActivity.this, ChangeSignatureActivity.class);
            }
            startActivity(intent);
        }
    };

    //监听EditText内容变化
    /*public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            Log.d("TAG", "afterTextChanged--------------->"+s.toString());
            SharedPreferences mySharedPreferences= getSharedPreferences("editNickname",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("editnickname",s.toString());
            editor.commit();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };*/

    /*private View.OnFocusChangeListener focusChange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    Log.v("bbbbb","获得焦点");
                else
                    Log.v("bbbbb","未获得焦点");
        }
    };*/
}
