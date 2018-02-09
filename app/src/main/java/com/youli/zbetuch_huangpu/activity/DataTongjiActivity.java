package com.youli.zbetuch_huangpu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

/**
 * Created by liutao on 2018/1/10.
 *
 * 数据统计
 */

public class DataTongjiActivity extends BaseActivity{

    private WebView wv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_tongji);

        initViews();
    }

    private void initViews(){

        wv= (WebView) findViewById(R.id.wv_data_tongji);
        loadUrl(wv);
    }

    private void loadUrl(WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        // 加载数据
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }
        });
        wv.setWebViewClient(new WebViewClient());
        //  http://web.youli.pw:8088/Chart/PadReport.aspx?JD=黄浦区&Staff=1
        wv.loadUrl(MyOkHttpUtils.BaseUrl+"/Chart/PadReport.aspx?JD="+HomePageActivity.adminInfo.getJD()+"&Staff="+HomePageActivity.adminInfo.getID());

    }

         //支持网页的回退
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == event.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
