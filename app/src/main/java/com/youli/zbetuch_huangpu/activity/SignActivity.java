package com.youli.zbetuch_huangpu.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import com.youli.zbetuch_huangpu.entity.SignInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class SignActivity extends BaseActivity {
    private Context mContext = SignActivity.this;
    private PullToRefreshListView lv;
    private ImageButton btn_qd;
    private List<SignInfo> nList = new ArrayList<SignInfo>();
    private int index;
    private int HDID;

    private final int SUCCESS = 10000;
    private final int NODATA = 10001;
    private final int PROBLEM = 10002;  //网络不给力
    private final int OVERTIME = 10003;//登录超时

    private final int SUBMITSUCCESS = 10004;//提交成功
    private final int SUBMITFAIL = 10005;//提交失败
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
                    nList.addAll((List<SignInfo>) msg.obj);

//                    for(SignInfo info:nList){
//
//                        info.setXZ(false);
//
//                    }

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

                case SUBMITSUCCESS:
                    //签到成功
                         nList.get(msg.arg1).setIsQD(true);
                        commonAdapter.notifyDataSetChanged();
                        Toast.makeText(mContext,"签到成功",Toast.LENGTH_SHORT).show();
                    break;

                case SUBMITFAIL:
                    //签到失败
                    Toast.makeText(mContext,"签到失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        HDID=getIntent().getIntExtra("HDID",0);
        Log.e("HDID=",""+HDID);
        initView();

    }

    private void initView() {
        lv = (PullToRefreshListView) findViewById(R.id.lv_qd_info_list);
        getNetWorkData(pageIndex);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                getNetWorkData(pageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                getNetWorkData(pageIndex);
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
                //http://192.168.191.2:8088/Json/CYWork/Get_CY_User_Info.aspx?page=0&rows=5&hdId=8
                String url= MyOkHttpUtils.BaseUrl+"/Json/CYWork/Get_CY_User_Info.aspx?page="+page+"&rows=5&&hdId="+HDID;
                Log.e("----2018-3-5---","url="+url);
                Response response= MyOkHttpUtils.okHttpGet(url);
                Message msg=Message.obtain();
                try {
                    if (response !=null) {
                        String infoStr = response.body().string();
                        Gson gson=new Gson();
                        try {
                            msg.obj=gson.fromJson(infoStr,new TypeToken<List<SignInfo>>(){}.getType());
                            Log.e("qwer","进入");
                            msg.what=SUCCESS;
                            mHandler.sendMessage(msg);
                        }catch (Exception e){
                            Log.e("2018/3/2","登录超时了");
                            msg.what=OVERTIME;
                            mHandler.sendMessage(msg);
                        }
                    }else{
                        msg.what = PROBLEM;
                        mHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //adapter
    private void lvSetAdapter(final List<SignInfo> data){
        if (commonAdapter==null){
            commonAdapter=new CommonAdapter<SignInfo>(mContext,data,R.layout.activity_sign_ltem_lv) {
                @Override
                public void convert(CommonViewHolder holder, final SignInfo item, final int position) {
                    ImageButton btn=holder.getView(R.id.cb_item_lv_sign);
                    TextView tv1=holder.getView(R.id.tv_item_lv_sign1);
                    TextView tv2=holder.getView(R.id.tv_item_lv_sign2);
                    TextView tv3=holder.getView(R.id.tv_item_lv_sign3);
                    TextView tv4=holder.getView(R.id.tv_item_lv_sign4);
                    TextView tv5=holder.getView(R.id.tv_item_lv_sign5);
                    TextView tv6=holder.getView(R.id.tv_item_lv_sign6);
                    TextView tv7=holder.getView(R.id.tv_item_lv_sign7);
                    TextView tv8=holder.getView(R.id.tv_item_lv_sign8);
                    TextView tv9=holder.getView(R.id.tv_item_lv_sign9);
                    tv1.setText(item.getXM());
                    tv2.setText(item.getGENDER());
                    tv3.setText(item.getWHCD());
                    tv4.setText(MyDateUtils.stringToYMD(item.getTJDATE()));
                    tv5.setText(item.getZJHM());
                    tv6.setText(item.getLXDH());
                    tv7.setText(item.getTJJD());
                    tv8.setText(item.getTJRXM());
                    tv9.setText(item.getBMZT());
                    if (data.get(position).isIsQD()){
                        btn.setImageResource(R.drawable.yqd6);
                        btn.setEnabled(false);
                    }else if (!data.get(position).isIsQD()){
                        btn.setImageResource(R.drawable.button_qiandao);
                        btn.setEnabled(true);
                    }
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showSubmitDialog(data.get(position).getMDID(),position);
                            Log.e("----2018-3-6---",data.get(position).getMDID()+"");
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


//以下是签到内容
    private void showSubmitDialog(final int id, final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定签到?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                             submitInfo(id,position);
                            }}
                ).start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void submitInfo(int id,int position) {
        //http://192.168.191.2:8088/Json/CYWork/Set_UserQD_Info.aspx?page=0&rows=5&mdId=3    给mdId传id
        String url = MyOkHttpUtils.BaseUrl+"/Json/CYWork/Set_UserQD_Info.aspx?page=0&rows=5&mdId="+id;
        Log.e("签到提交内容：",url);
        Response response = MyOkHttpUtils.okHttpGet(url);
        Message msg=Message.obtain();
        try {
        if (response !=null) {
            String resStr = response.body().string();
            Log.e("2018/3/6", "提交==" + resStr);
            msg.what=SUBMITSUCCESS;
            msg.arg1=position;
            mHandler.sendMessage(msg);
        }
            } catch (IOException e) {
            msg.what=SUBMITFAIL;
            mHandler.sendMessage(msg);
            }
    }
}
