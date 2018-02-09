package com.youli.zbetuch_huangpu.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.AppendixInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.StayInfo;
import com.youli.zbetuch_huangpu.utils.AlertDialogUtils;
import com.youli.zbetuch_huangpu.utils.FileUtils;
import com.youli.zbetuch_huangpu.utils.IOUtil;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;
import com.youli.zbetuch_huangpu.view.MyListView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class NeedWebActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext=this;


    private final int GET_BUTTON_STATE=10001;//获取是否阅读按钮的状态
    private final int SET_BUTTON_STATE=10002;//设置是否阅读按钮的状态
    private final int GET_Appendix=10003;//获取附件
    private final int SUCCEED_FILE=10004;
    private final int SUCCEED_SEE=10005;
    private final int  PROBLEM=10006;
    private final int OVERTIME=10007;//登录超时

    private WebView myWeb;//工作内容

    private StayInfo sInfo;

    private TextView tvName;//工作名称
    private TextView tvFounder;//创建人
    private TextView tvFinishTime;//完成时间
    private TextView tvCreatTime;//创建时间

    private Button btnFinish,btnNoFinish;

    private List<AppendixInfo> appendixData=new ArrayList<>();//附件的数据
    private MyListView appendixLv;
    private CommonAdapter adapter;

    private String currentFile;
    private int pSign;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case GET_BUTTON_STATE://获取是否阅读按钮的状态


                    Log.e("2017-12-12","按钮状态=="+msg.obj);

                    if(TextUtils.equals(msg.obj+"","已完成")){
                        btnFinish.setEnabled(false);
                        btnNoFinish.setEnabled(false);
                    }else if(TextUtils.equals(msg.obj+"","未完成")){
                        btnFinish.setEnabled(true);
                        btnNoFinish.setEnabled(true);
                    }


                    getAppendixInfo();//获取附件


                    break;

                case SET_BUTTON_STATE://设置是否阅读按钮的状态

                    Log.e("2017-12-12","==="+msg.obj);
                    if(TextUtils.equals(msg.obj+"","True")){
                        Toast.makeText(mContext,"提交成功",Toast.LENGTH_SHORT).show();
                        btnFinish.setEnabled(false);
                        btnNoFinish.setEnabled(false);
                        EventBus.getDefault().post("DBGZ");//刷新首页，待办工作的数量
                        EventBus.getDefault().post("DCDB");//刷新首页，督察督办的数量
                    }



                    break;


                case GET_Appendix:


                    appendixData.clear();
                    appendixData.addAll((List<AppendixInfo>)msg.obj);
                    LvSetAdapter(appendixData);

                    break;


                case SUCCEED_FILE://保存文件

                    SavaFile((InputStream) msg.obj, Environment.getExternalStorageDirectory().getPath(),msg.arg1);

                    break;

                case SUCCEED_SEE://查看附件的具体内容

                    File file=new File(currentFile);
                    Intent intent = FileUtils.openFile(file.getAbsolutePath());
                    startActivity(intent);
                    currentFile = "";
                    break;

                case PROBLEM:
                    Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();

                    break;

                case OVERTIME:

                    Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(i);


                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_web);

        sInfo= (StayInfo) getIntent().getSerializableExtra("StayInfo");

        initView();
    }
    private void initView(){


        tvName= (TextView) findViewById(R.id.tv_need_web_name);
        tvFounder= (TextView) findViewById(R.id.tv_need_web_founder);
        tvFinishTime= (TextView) findViewById(R.id.tv_need_web_finish_time);
        tvCreatTime= (TextView) findViewById(R.id.tv_need_web_create_time);

        tvName.setText(sInfo.getTitle());
        if(HomePageActivity.adminInfo.getID()==sInfo.getCreate_staff()){
            tvFounder.setText(HomePageActivity.adminName);
        }
        tvFinishTime.setText(MyDateUtils.stringToYMDHMS(sInfo.getNotice_time()));
        tvCreatTime.setText(MyDateUtils.stringToYMDHMS(sInfo.getCreate_date()));


        appendixLv= (MyListView) findViewById(R.id.lv_need_web_appendix);
        appendixLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPermission(position);
                pSign=position;
            }
        });


        btnFinish= (Button) findViewById(R.id.btn_need_web_finish);
        btnFinish.setOnClickListener(this);
        btnNoFinish= (Button) findViewById(R.id.btn_need_web_no_finish);
        btnNoFinish.setOnClickListener(this);

        myWeb= (WebView) findViewById(R.id.wv_need_web);

        myWeb.setWebViewClient(new WebViewClient());
        WebSettings settings = myWeb.getSettings();//自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);//自适应Js
        myWeb.getSettings().setTextSize(WebSettings.TextSize.LARGEST);//设置字体大小
        myWeb.setBackgroundColor(Color.parseColor("#FAFAFA")); // 设置背景色
        myWeb.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
        myWeb.loadDataWithBaseURL(null,sInfo.getDoc(),"text/html", "UTF-8", null);

        getOrSetButtonState("get","");

    }

    private void getOrSetButtonState(final String mark, final String beizhu){
        // 获取按钮的状态：
        // http://web.youli.pw:8088/Json/First/Get_Work_Notice_State.aspx?MASTER_ID=1

        //已完成:
        //http://web.youli.pw:8088/Json/First/Set_Work_Notice_Check.aspx?MASTER_ID=1

        //无法完成:
        //http://web.youli.pw:8088/Json/First/Set_Work_Notice_Check.aspx?MASTER_ID=1&MARK
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url = null;

                        if(TextUtils.equals(mark,"get")){
                            url=MyOkHttpUtils.BaseUrl+"/Json/First/Get_Work_Notice_State.aspx?MASTER_ID="+sInfo.getId();
                        }else if(TextUtils.equals(mark,"set")){
                            url=MyOkHttpUtils.BaseUrl+"/Json/First/Set_Work_Notice_Check.aspx?MASTER_ID="+sInfo.getId();
                        }else if(TextUtils.equals(mark,"mark")){
                            url=MyOkHttpUtils.BaseUrl+"/Json/First/Set_Work_Notice_Check.aspx?MASTER_ID="+sInfo.getId()+"&MARK="+beizhu;
                        }
                        Log.e("2017-12-12","url="+url);
                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    msg.obj=meetStr;
                                    if(TextUtils.equals(mark,"get")) {
                                        msg.what = GET_BUTTON_STATE;
                                    }else if(TextUtils.equals(mark,"set")||TextUtils.equals(mark,"mark")){
                                        msg.what = SET_BUTTON_STATE;
                                    }
                                }else{
                                    //msg.what=SUCCEED_NODATA;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                msg.what=OVERTIME;

                            }

                        }else{

                            msg.what=PROBLEM;

                        }

                        mHandler.sendMessage(msg);

                    }
                }


        ).start();

    }


    private void getAppendixInfo(){

        //http://web.youli.pw:8088/Json/First/Get_Work_Notice_File.aspx?MASTER_ID=1
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String meetUrl= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Work_Notice_File.aspx?MASTER_ID="+sInfo.getId();

                        Response response=MyOkHttpUtils.okHttpGet(meetUrl);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<AppendixInfo>>(){}.getType());
                                    msg.what=GET_Appendix;
                                }else{
                                    // msg.what=SUCCEED_NODATA; 没有数据
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                msg.what=OVERTIME;

                            }

                        }else{

                            msg.what=PROBLEM;

                        }

                        mHandler.sendMessage(msg);

                    }
                }


        ).start();

    }



    private void LvSetAdapter(List<AppendixInfo> list){

        if(adapter==null){

            adapter=new CommonAdapter<AppendixInfo>(mContext,list,R.layout.item_meet_detail_appendix) {
                @Override
                public void convert(CommonViewHolder holder, AppendixInfo item, int position) {

                    TextView name=holder.getView(R.id.item_meet_detail_appendix_name);
                    name.setText(item.getFILE_NAME());
                    name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

                }
            };

            appendixLv.setAdapter(adapter);

        }else{


            adapter.notifyDataSetChanged();

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_need_web_finish://已完成

                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setMessage("您确定保存吗?");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getOrSetButtonState("set","");
                    }
                });
                builder.show();

                break;


            case R.id.btn_need_web_no_finish://无法完成

               showNoFinishDialog();//无法完成的对话框

                break;
        }

    }

    private void showNoFinishDialog(){



        final AlertDialogUtils noFinishDialog=new AlertDialogUtils(mContext,R.layout.dialog_show_no_finish);

        noFinishDialog.showAlertDialog();

       final EditText etMark= (EditText) noFinishDialog.getAduView().findViewById(R.id.dialog_no_finish_et);



        Button sureBtn= (Button) noFinishDialog.getAduView().findViewById(R.id.dialog_no_finish_ensure_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String   markStr=etMark.getText().toString().trim();

                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setMessage("您确定保存吗?");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(TextUtils.equals(markStr,"")){
                            Toast.makeText(mContext,"原因不能为空",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        noFinishDialog.dismissAlertDialog();

                        getOrSetButtonState("mark",markStr);
                    }
                });
                builder.show();


            }
        });

        Button cancelBtn= (Button) noFinishDialog.getAduView().findViewById(R.id.dialog_no_finish_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noFinishDialog.dismissAlertDialog();
            }
        });
    }


    private void getPermission(int position) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext,"您已经拒绝过一次",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},10000);
        } else {
            downLoadPic(appendixData.get(position).getFILE_PATH(),position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        downLoadPic(appendixData.get(pSign).getFILE_PATH(),pSign);
    }


    private void downLoadPic(final String path, final int position){//下载

        new Thread(new Runnable() {
            @Override
            public void run() {

                String url=MyOkHttpUtils.BaseUrl+"/"+path;

                Response response=MyOkHttpUtils.okHttpGet(url);

                Message msg=Message.obtain();

                if(response!=null){

                    msg.obj= response.body().byteStream();

                    msg.what=SUCCEED_FILE;

                    msg.arg1=position;

                }else{

                    msg.what=PROBLEM;

                }

                mHandler.sendMessage(msg);

            }

        }).start();

    }


    private void SavaFile(final InputStream is, final String path, final int position) {//保存文件

        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                        String fileName = appendixData.get(position).getFILE_NAME();

                        Message msg=Message.obtain();

                        File file = new File(path);
                        FileOutputStream fileOutputStream = null;
                        //文件夹不存在，则创建它
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        currentFile=path+"/"+fileName;

                        File saveFile = new File(file, fileName);


                        try {

                            fileOutputStream = new FileOutputStream(saveFile);
                            fileOutputStream.write(IOUtil.getBytesByStream(is));
                            fileOutputStream.close();
                            msg.what=SUCCEED_SEE;
                        } catch (IOException e) {
                            if(saveFile.exists()){
                                saveFile.delete();
                            }
                            e.printStackTrace();
                            msg.what=PROBLEM;
                        }
                        mHandler.sendMessage(msg);
                    }
                }

        ).start();

    }
}
