package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.CreateActivityIndo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class KechengActivity extends BaseActivity {
    private Context mContext=KechengActivity.this;
    private PullToRefreshListView lv;
    private List<CreateActivityIndo> nList=new ArrayList<CreateActivityIndo>();

    private final int SUCCESS=10000;
    private final int NODATA=10001;
    private final int PROBLEM=10002;  //网络不给力
    private final int OVERTIME=10003;//登录超时
    private CommonAdapter commonAdapter;
    private int pageIndex;



    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            switch (msg.what) {
                case SUCCESS:
                    if (pageIndex == 0) {
                        nList.clear();
                    }
                    nList.addAll((List<CreateActivityIndo>) msg.obj);
                    if (nList.size() > 0) {
//                        tv1.setVisibility(View.GONE);
//                        lv.setVisibility(View.VISIBLE);
                        lvSetAdapter(nList);
                    } else {
//                        tv.setText("总共0条数据");
//                        tv1.setVisibility(View.VISIBLE);
//                        lv.setVisibility(View.GONE);
                    }
                    break;
                case PROBLEM:
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;

                case NODATA:
                    if (lv.isRefreshing()) {
                        lv.onRefreshComplete();
                    }
                    break;

                case OVERTIME:
                    //跳转登陆超时对话框
                    Intent i = new Intent(mContext, OvertimeDialogActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecheng);
        initView();
    }

    private void initView(){
        lv= (PullToRefreshListView) findViewById(R.id.lv_kecheng_info_list);
        getNetWorkData(pageIndex);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex=0;
                getNetWorkData(pageIndex);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                getNetWorkData(pageIndex);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mContext,GuidanceManagerActivity.class);
                intent.putExtra("ZDHD",nList.get(position-1));
                startActivity(intent);
            }
        });


    }
    private void getNetWorkData(final int page){
        ProgressDialogUtils.showMyProgressDialog(mContext);
        if(page==0) {
            if (nList != null && nList.size() > 0 ) {
                nList.clear();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //http://192.168.191.2:8088/Json/CYWork/Get_CYWorkInfo.aspx?page=0&rows=10&HDFL=指导活动管理
                String url= MyOkHttpUtils.BaseUrl+"/Json/CYWork/Get_CYWorkInfo.aspx?page="+page+"&rows=10&HDFL=培训课程管理";
                Log.e("----2018-3-1---","url="+url);
                Response response= MyOkHttpUtils.okHttpGet(url);
                Message msg=Message.obtain();
                try {
                    if (response !=null) {
                        String infoStr = response.body().string();
                        Gson gson=new Gson();
                        try {
                            msg.obj=gson.fromJson(infoStr,new TypeToken<List<CreateActivityIndo>>(){}.getType());
                            Log.e("qwer","进入");
                            msg.what=SUCCESS;
                            mHandler.sendMessage(msg);
                        }catch (Exception e){
                            Log.e("2018/3/2","登录超时了");
                            msg.what=OVERTIME;
                            mHandler.sendMessage(msg);
                        }
                    }else{
                        sendProblemMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void sendProblemMessage(Message msg) {
        msg.what = PROBLEM;
        mHandler.sendMessage(msg);
    }


    //adapter
    private void lvSetAdapter(final List<CreateActivityIndo> data){
        if (commonAdapter==null){
            commonAdapter=new CommonAdapter<CreateActivityIndo>(mContext,data,R.layout.activity_cyzd_item) {
                @Override
                public void convert(CommonViewHolder holder, CreateActivityIndo item, final int position) {
                    Button btn=holder.getView(R.id.sign);
                    TextView tv2=holder.getView(R.id.tv_cyzd_2);
                    TextView tv3=holder.getView(R.id.tv_cyzd_3);
                    TextView tv4=holder.getView(R.id.tv_cyzd_4);
                    TextView tv5=holder.getView(R.id.tv_cyzd_5);
                    TextView tv6=holder.getView(R.id.tv_cyzd_6);
                    TextView tv7=holder.getView(R.id.tv_cyzd_7);
                    tv2.setText(item.getHDTHEME());
                    tv3.setText(item.getHDADDRESS());
                    tv4.setText(MyDateUtils.stringToYMD(item.getHDKSDATE()));
                    tv5.setText(MyDateUtils.stringToYMD(item.getHDJSDATE()));
                    tv6.setText(item.getHDZT());
                    tv7.setText(item.getTJRS()+"");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(mContext,SignActivity.class);
                            intent.putExtra("HDID",data.get(position).getHDID());
                            startActivity(intent);
                        }
                    });

                }
            };
            lv.setAdapter(commonAdapter);
        }else{
            commonAdapter.notifyDataSetChanged();
        }
        if (lv.isRefreshing()) {
            lv.onRefreshComplete();
        }
    }
}
