package com.youli.zbetuch_huangpu.activity;

import android.app.Activity;
import android.os.Bundle;

import com.youli.zbetuch_huangpu.activity.ActivityController;

/**
 * Created by liutao on 2017/9/21.
 */

public class BaseActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ActivityController.removeActivity(this);
    }
}
