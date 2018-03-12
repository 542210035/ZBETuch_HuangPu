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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CheckMailInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.MailInfo;
import com.youli.zbetuch_huangpu.utils.FileUtils;
import com.youli.zbetuch_huangpu.utils.IOUtil;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;
import com.youli.zbetuch_huangpu.view.MyListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class CheCkMailActivity extends BaseActivity {
    @BindView(R.id.mail_ck_zt)
    TextView mailCkZt;    //主题
    @BindView(R.id.mail_ck_fjr)
    TextView mailCkFjr;   //发件人
    @BindView(R.id.mail_ck_sj)
    TextView mailCkSj;   //时间
    @BindView(R.id.wv_yj)
    WebView wv_yj;//邮件内容
    @BindView(R.id.lv_yj_ckyj)
    MyListView lv; //附件内容

    private Context mContext = CheCkMailActivity.this;
    private MailInfo mailInfo;
    private CheckMailInfo checkMailInfo; //第一层json数据
    private List<CheckMailInfo.FilelistBean> weather;//第二层json数据
    private CommonAdapter adapter;
    private int pSign;
    private String currentFile;

    private final int SUCCEED = 10001;
    private final int SUCCEED_NODATA = 10002;
    private final int PROBLEM = 10005;
    private final int OVERTIME = 10006;//登录超时
    private final int SUCCEED_FILE=10004;  //附件
    private final int SUCCEED_SEE=10003;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtils.dismissMyProgressDialog(CheCkMailActivity.this);
            Intent i;
            switch (msg.what) {
                case SUCCEED:
                    wv_yj.getSettings().setJavaScriptEnabled(true);
                    wv_yj.getSettings().setAllowFileAccess(true);
                    wv_yj.getSettings().setPluginState(WebSettings.PluginState.ON);
                    wv_yj.getSettings().setUseWideViewPort(true);
                    wv_yj.getSettings().setLoadWithOverviewMode(true);
                    wv_yj.getSettings().setTextSize(WebSettings.TextSize.LARGEST);//设置字体大小
                    wv_yj.setWebViewClient(new WebViewClient());
                    wv_yj.setBackgroundColor(Color.parseColor("#FAFAFA")); // 设置背景色
                    wv_yj.getBackground().mutate().setAlpha(255);
                    ; // 设置填充透明度 范围：0-255
                    wv_yj.loadDataWithBaseURL(null, checkMailInfo.getDOC(), "text/html", "UTF-8", null);
                    getAdapters(weather);
                    break;
                case PROBLEM:
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;

                case SUCCEED_NODATA:
                    break;

                case OVERTIME:
                    i = new Intent(mContext, OvertimeDialogActivity.class);
                    startActivity(i);
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
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_che_ck_mail);
        ButterKnife.bind(this);
        mailInfo = (MailInfo) getIntent().getSerializableExtra("mail_gj");
        initView();
    }

    private void initView() {
        initDatas(mailInfo.getEMAIL_ID());
        mailCkZt.setText(mailInfo.getTITLE());  //主题
        mailCkFjr.setText(mailInfo.getRECEIVE_STAFF_NAME());  //发件人
        mailCkSj.setText(MyDateUtils.stringToYMD(mailInfo.getREADTIME()));  //时间
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mContext,"下载"+appendixData.get(position),Toast.LENGTH_SHORT).show();
                getPermission(position);
                pSign=position;
//                Log.e("zxc",""+);
            }
        });
    }


    //网络请求数据
    private void initDatas(final int id) {
        ProgressDialogUtils.showMyProgressDialog(CheCkMailActivity.this);
        //http://192.168.191.3:8088/Json/Email/Get_Email_Info.aspx?EmailId=1
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = MyOkHttpUtils.BaseUrl + "/Json/Email/Get_Email_Info.aspx?EmailId=" + id;
                Response response = MyOkHttpUtils.okHttpGet(url);
                Log.e("2018-2-2", url);
                final Message msg = Message.obtain();
                if (response != null) {
                    try {
                        String meetStr = response.body().string();
                        if (!TextUtils.equals(meetStr, "") && !TextUtils.equals(meetStr, "[]")) {
                            final Gson gson = new Gson();
                            checkMailInfo = gson.fromJson(meetStr, CheckMailInfo.class);      //请求的一层json数据
                            Log.e("请求的一层json数据", "" + checkMailInfo.getDOC());
                            weather = checkMailInfo.getFilelist();  //请求的二层json数据
                            Log.e("请求的二层json数据", "" + weather.size());
                            msg.what = SUCCEED;

                        } else {
                            msg.what = SUCCEED_NODATA;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("2018-2-2", "登录超时了");
                        msg.what = OVERTIME;
                    }
                } else {
                    msg.what = PROBLEM;
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private void getAdapters(List<CheckMailInfo.FilelistBean> list){
        if (adapter ==null){
            adapter=new CommonAdapter<CheckMailInfo.FilelistBean>(mContext,list,R.layout.activity_yj_ck_item) {
                @Override
                public void convert(CommonViewHolder holder, CheckMailInfo.FilelistBean item, int position) {
                    TextView tv=holder.getView(R.id.lv_yj_ck_tv);
                    tv.setText(item.getFILE_NAME()+"");
                    tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
                }
            };
        }
        lv.setAdapter(adapter);
    }


    private void getPermission(int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext,"您已经拒绝过一次",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},10000);
        } else {
            downLoadPic(weather.get(position).getPATH(),position);
            Log.e("qwe",""+weather.get(position).getPATH());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        downLoadPic(weather.get(pSign).getPATH(),pSign);
        Log.e("lujin",""+weather.get(pSign).getPATH());
    }

    private void downLoadPic(final String path, final int position){//下载

        new Thread(new Runnable() {
            @Override
            public void run() {

                String url=MyOkHttpUtils.BaseUrl+"/Web/Manage/EMAIL/"+path;
                Log.e("2018-2-4",""+url);
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
        ProgressDialogUtils.showMyProgressDialog(CheCkMailActivity.this);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String fileName = weather.get(position).getFILE_NAME();   //获取的是附件的名字
                        Message msg=Message.obtain();
                        File file = new File(path);
                        FileOutputStream fileOutputStream = null;
                        //文件夹不存在，则创建它
                        if (!file.exists()) {   // 判断文件或文件夹是否存在
                            file.mkdir();   //创建目录用的。
                        }
                        currentFile=path+"/"+fileName;  //l路径加上文件
                        File saveFile = new File(file, fileName); //把文件放入文件夹
                        try {
                            fileOutputStream = new FileOutputStream(saveFile);
                            fileOutputStream.write(IOUtil.getBytesByStream(is));//is是请求的附件内容
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
