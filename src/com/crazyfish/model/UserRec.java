package com.crazyfish.model;

import android.graphics.Bitmap;

/**
 * Created by kamal on 2015/1/20.
 */
public class UserRec {
    public Bitmap bitmap;// 图片
    public String title;// 题标

    // 待扩展

    public UserRec() {
    }

    public UserRec(Bitmap bitmap, String title) {
        super();
        this.bitmap = bitmap;
        this.title = title;
    }
}
