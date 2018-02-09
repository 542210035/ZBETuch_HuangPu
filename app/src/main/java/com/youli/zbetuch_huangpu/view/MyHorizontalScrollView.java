package com.youli.zbetuch_huangpu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;

/**
 * Created by liutao on 2018/1/7.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {


    ScrollViewListener scrollViewListener;


    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }




    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);




        }
    }


    //    private View mView;
//
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        if(mView!=null){
//            mView.scrollTo(l, t);
//
//        }
//    }
//
//    public void setScrollView(View view){
//        mView = view;
//    }




    private int lastX,lastY;



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO 自动生成的方法存根
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                onTouchEvent(ev);  //添加这一句即可
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - lastX) > Math.abs(y - lastY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                intercept = true;
                break;
        }
        lastX = x;
        lastY = y;
        return intercept;
    }


}

