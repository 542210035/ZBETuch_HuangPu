package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;

public class ContentActivity extends BaseActivity {
    private WebView myWebView ;
    private String url;
    private TextView tv_neirong,tv_content;

    private String contentStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
    init();
    }
    private void init(){
        myWebView= (WebView) findViewById(R.id.Mywebview);
        tv_neirong= (TextView) findViewById(R.id.tv_neirong);
        contentStr=getIntent().getStringExtra("Content");
        String b=getIntent().getStringExtra("Nr");
        tv_neirong.setText(b);

        myWebView.getSettings().setSupportZoom(true);   //放大缩小
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        int a =  getIntent().getIntExtra("RDInfoo",0);
        url= MyOkHttpUtils.BaseUrl+"/Web/Manage/FrmShowNewsWindows.aspx?News="+a;
        WebSettings settings = myWebView.getSettings();//自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);//自适应Js
        synCookies(this,url);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadDataWithBaseURL(null,contentStr,"text/html", "UTF-8", null);

    }
    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        cookieManager.removeSessionCookie();//移除
        String cookies =SharedPreferencesUtils.getString("cookie");
        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        init();

    }
}
