package com.youli.zbetuch_huangpu.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youli.zbetuch_huangpu.R;

/**
 * Created by liutao on 2017/12/29.
 */

public class MyToast {

    private Toast mToast;
    private MyToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_toast);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        setGravity(Gravity.CENTER,0,0);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

}
