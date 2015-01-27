package com.crazyfish.extension;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.crazyfish.demo.R;

public class ImageDialog extends Dialog {

    public ImageDialog(Context context) {
        super(context, R.style.ImageloadingDialogStyle);
        //setOwnerActivity((Activity) context);// 设置dialog全屏显示
    }

    private ImageDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);
    }

}