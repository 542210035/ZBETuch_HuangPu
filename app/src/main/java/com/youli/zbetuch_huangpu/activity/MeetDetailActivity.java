package com.youli.zbetuch_huangpu.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.AppendixInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.MeetInfo;
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

/**
 * 作者: zhengbin on 2017/12/2.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 会议详情界面
 */

public class MeetDetailActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext=this;

    private final int GET_BUTTON_STATE=10001;//获取是否阅读按钮的状态
    private final int SET_BUTTON_STATE=10002;//设置是否阅读按钮的状态
    private final int GET_Appendix=10003;//获取附件
    private final int SUCCEED_FILE=10004;
    private final int SUCCEED_SEE=10005;
    private final int  PROBLEM=10006;
    private final int OVERTIME=10007;//登录超时

    private MeetInfo meetInfo;

    private WebView web;//会议内容
    private TextView tvTitle;//会议名称
    private TextView tvNotifier;//通知人
    private TextView tvMtime;//会议时间
    private TextView tvAddress;//会议地点
    private TextView tvPTime;//发布时间

    private Button btnIsRead;//已读或未读

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

                    if(TextUtils.equals(msg.obj+"","已阅")){
                        btnIsRead.setEnabled(false);
                    }else if(TextUtils.equals(msg.obj+"","未阅")){
                        btnIsRead.setEnabled(true);
                    }

                    btnIsRead.setText(msg.obj+"");


                   getAppendixInfo();//获取附件

                    break;

                case SET_BUTTON_STATE://设置是否阅读按钮的状态

                    Log.e("2017-12-12","==="+msg.obj);
                    if(TextUtils.equals(msg.obj+"","True")){
                        btnIsRead.setEnabled(false);
                        btnIsRead.setText("已阅");
                        EventBus.getDefault().post("HYGL");//刷新首页，会议管理的数量
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
        setContentView(R.layout.activity_meet_detail);


        meetInfo= (MeetInfo) getIntent().getSerializableExtra("meetInfo");

        initViews();

    }

    private void initViews(){

        btnIsRead= (Button) findViewById(R.id.btn_meet_detail_isread);
        btnIsRead.setOnClickListener(this);

        tvTitle= (TextView) findViewById(R.id.tv_meet_detail_name);
         tvNotifier= (TextView) findViewById(R.id.tv_meet_detail_notifier);
         tvMtime= (TextView) findViewById(R.id.tv_meet_detail_mtime);
         tvAddress= (TextView) findViewById(R.id.tv_meet_detail_address);
         tvPTime= (TextView) findViewById(R.id.tv_meet_detail_ptime);

        tvTitle.setText(meetInfo.getTITLE());
        tvNotifier.setText(meetInfo.getCREATE_STAFF_NAME());
        tvMtime.setText(MyDateUtils.stringToYMDHMS(meetInfo.getMEETING_TIME()));
        tvAddress.setText(meetInfo.getMEETING_ADD());
        tvPTime.setText(MyDateUtils.stringToYMDHMS(meetInfo.getCREATE_DATE()));

         appendixLv= (MyListView) findViewById(R.id.lv_meet_detail_appendix);
         appendixLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //Toast.makeText(mContext,"下载"+appendixData.get(position),Toast.LENGTH_SHORT).show();
                 getPermission(position);
                 pSign=position;
             }
         });


        web= (WebView) findViewById(R.id.wv_meet_detail);

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setTextSize(WebSettings.TextSize.LARGEST);//设置字体大小
        web.setWebViewClient(new WebViewClient());

        web.setBackgroundColor(Color.parseColor("#FAFAFA")); // 设置背景色
        web.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
        web.loadDataWithBaseURL(null,meetInfo.getDOC(),"text/html", "UTF-8", null);

        getOrSetButtonState("get");

    }


    private void getOrSetButtonState(final String mark){
       // 获取按钮的状态：
       // http://web.youli.pw:8088/Json/First/Get_Meeting_Check_State.aspx?MASTER_ID=1002

      //  设置按钮的状态：
       // http://web.youli.pw:8088/Json/First/Set_Meeting_Check.aspx?MASTER_ID=1002
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url = null;

                        if(TextUtils.equals(mark,"get")){
                            url=MyOkHttpUtils.BaseUrl+"/Json/First/Get_Meeting_Check_State.aspx?MASTER_ID="+meetInfo.getID();
                        }else if(TextUtils.equals(mark,"set")){
                            url=MyOkHttpUtils.BaseUrl+"/Json/First/Set_Meeting_Check.aspx?MASTER_ID="+meetInfo.getID();
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
                                    }else if(TextUtils.equals(mark,"set")){
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_meet_detail_isread:

                getOrSetButtonState("set");

                break;



        }

    }

    private void getAppendixInfo(){

        //http://web.youli.pw:8088/Json/First/Get_Meeting_File.aspx?MASTER_ID=1
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String meetUrl= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Meeting_File.aspx?MASTER_ID="+meetInfo.getID();

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
//              Log.e("2018-2-4",""+url);

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


    private void SavaFile(final InputStream is, final String path, final int position) {

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
