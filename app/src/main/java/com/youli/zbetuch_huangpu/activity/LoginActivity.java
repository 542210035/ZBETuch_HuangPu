package com.youli.zbetuch_huangpu.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.utils.AlertDialogUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;

import java.io.IOException;

import okhttp3.Response;

//登录界面
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_userPassword;
    private EditText et_userName;
    private TextView tv_about,tv_title;
    private Button btn_login;
    private final int SUCCEED=10000;
    private final int  PROBLEM=10001;
    private LocationManager locationManager;
    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            ProgressDialogUtils.dismissMyProgressDialog(LoginActivity.this);
            switch (msg.what){

                case SUCCEED:

                    if(TextUtils.equals("true", (String)msg.obj)){
                        Intent intent=new Intent(LoginActivity.this,HomePageActivity.class);
                        startActivity(intent);
                        SharedPreferencesUtils.putString("userName",et_userName.getText().toString().trim());
                        finish();
                    }else if(TextUtils.equals("false", (String)msg.obj)){
                        Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case PROBLEM:

                    Toast.makeText(LoginActivity.this,"网络不给力",Toast.LENGTH_SHORT).show();

                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// 更新apk
//        UpdateManager manager = new UpdateManager(LoginActivity.this);
//        manager.checkUpdate();
        // 更新apk
        UpdateManager manager = new UpdateManager(LoginActivity.this);
        manager.checkUpdate();
        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        initUI();

    }

    //初始化控件
    private void initUI() {
        tv_about = (TextView) findViewById(R.id.tv_login_about);
        tv_title= (TextView) findViewById(R.id.login_title);

        //给标题设置字体
        Typeface type=Typeface.createFromAsset(getApplicationContext().getAssets(),"STXINGKA.TTF");
        tv_title.setTypeface(type);

        et_userName = (EditText) findViewById(R.id.et_user_name);
        String localUserName = SharedPreferencesUtils.getString("userName");

        //注释记住账号
//        if (localUserName != null) {
//            et_userName.setText(localUserName);
//        }
        et_userPassword = (EditText) findViewById(R.id.et_user_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        tv_about.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        if(!TextUtils.equals(et_userName.getText().toString().trim(),"")){
            et_userPassword.requestFocus();
        }

    }

    //显示关于Dialog
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login_about, null);
        builder.setView(view);
        TextView tv_dialog_app_version = (TextView) view.findViewById(R.id.tv_dialog_app_version);
        //修改dialog的软件版本的内容为Manifest中的VersionName
//        tv_dialog_app_version.setText(GetManifestInfo.getVersionName(this));
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {

        String userNameStr=et_userName.getText().toString().trim();
        String passwordStr=et_userPassword.getText().toString().trim();

        switch (v.getId()){
            case R.id.tv_login_about:
                showAboutDialog();
                break;
            case R.id.btn_login:

                if(TextUtils.equals("",userNameStr)||TextUtils.equals("",passwordStr)){
                    Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) != true)) {
                        Toast.makeText(LoginActivity.this, "请打开GPS定位！",
                                Toast.LENGTH_SHORT).show();
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                        startActivity(callGPSSettingIntent);

                    } else {
                        //登录
                        login(userNameStr, passwordStr);
                    }
                }


                break;
        }
    }

    private void login(final String name, final String password){

        ProgressDialogUtils.showMyProgressDialog(this);


        new Thread(

                new Runnable() {
                    @Override
                    public void run() {
                        String url= MyOkHttpUtils.BaseUrl+"/login.aspx?username="+name+"&password="+password;

                        Log.e("2017/11/9","登录="+url);

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        //获得cookies
                        if(response!=null) {
                            if (response.header("Set-Cookie") != null) {
                                String cookies = response.header("Set-Cookie").toString();

                                String mycookies=cookies.substring(0,cookies.indexOf(";"));
                                SharedPreferencesUtils.putString("cookie", mycookies);

                            }
                        }
                        Message msg=Message.obtain();
                        try {
                            if(response!=null) {
                                msg.obj = response.body().string();
                                msg.what=SUCCEED;
                                mHandler.sendMessage(msg);
                            }else{
                                msg.what=PROBLEM;
                                mHandler.sendMessage(msg);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

        ).start();

    }

}