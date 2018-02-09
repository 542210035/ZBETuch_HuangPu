package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.youli.zbetuch_huangpu.entity.CompanyInfo;
import com.youli.zbetuch_huangpu.entity.MailInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class Mail_Activity extends BaseActivity {
    @BindView(R.id.lv_my_mail)
    PullToRefreshListView lv;
    @BindView(R.id.tv_mail_yj)
    TextView tvMailYj;
    private Context mContext = Mail_Activity.this;
    private CommonAdapter adapter;  //apapter
    private List<MailInfo> data = new ArrayList<>();
    private int PageIndex;
    private final int SUCCEED = 10001;
    private final int SUCCEED_NODATA = 10002;
    private final int PROBLEM = 10005;
    private final int OVERTIME = 10006;//登录超时


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            super.handleMessage(msg);
            Intent i;
            switch (msg.what){
                case SUCCEED:
                    if(PageIndex==0) {
                        data.clear();
                    }
                    data.addAll((List<MailInfo>)msg.obj);
//                    if (data.size()<0||data.equals(" ")){
//                        no_tv.setVisibility(View.VISIBLE);
//                        lv.setVisibility(View.GONE);
//                    }
                    LvSetAdapter(data);
                    break;

                case PROBLEM:
                    Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();
                    if(lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;

                case SUCCEED_NODATA:
                    if(lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;

                case OVERTIME:
                    i=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initDatas(PageIndex);
        //上下拉刷新
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
                PageIndex=0;
                initDatas(PageIndex);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                PageIndex++;
                initDatas(PageIndex);
            }});

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mContext,CheCkMailActivity.class);
                intent.putExtra("mail_gj",data.get(position-1));
                startActivity(intent);
            }
        });
    }

    //网络请求数据
    private void initDatas(final int pIndex){
        //http://192.168.191.2:8088/Json/Email/Get_Receive.aspx?page=0&rows=1
        ProgressDialogUtils.showMyProgressDialog(mContext);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url= MyOkHttpUtils.BaseUrl+"/Json/Email/Get_Receive.aspx?page="+pIndex+"&rows=10";
                Response response=MyOkHttpUtils.okHttpGet(url);
                Log.e("2018-2-1",url);
                Message msg=Message.obtain();
                if(response!=null){
                    try {
                        String meetStr=response.body().string();
                        if (!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){
                            Gson gson=new Gson();
                            msg.obj=gson.fromJson(meetStr,new TypeToken<List<MailInfo>>(){}.getType());
                            msg.what=SUCCEED;
                        }else {
                            msg.what=SUCCEED_NODATA;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("2018-2-1","登录超时了");
                        msg.what=OVERTIME;
                    }
                }else {
                    msg.what=PROBLEM;
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    //adapter
    private void LvSetAdapter(final List<MailInfo> list) {
        if (adapter == null) {
            adapter=new CommonAdapter<MailInfo>(mContext,list,R.layout.activity_mail_item) {

                @Override
                public void convert(CommonViewHolder holder, MailInfo item, int position) {
                    TextView tv1=holder.getView(R.id.itme_yj_bh); //编号
                    TextView tv2=holder.getView(R.id.itme_yj_bt);  //标题
                    TextView tv3=holder.getView(R.id.itme_yj_fsz);  //发送者
                    TextView tv4=holder.getView(R.id.itme_yj_fssj); //发送时间
                    TextView tv5=holder.getView(R.id.itme_yj_ydzt);  //阅读状态
                    tv1.setText((position+1)+"");
                    tv2.setText(item.getTITLE());
                    tv3.setText(item.getRECEIVE_STAFF_NAME());
                    tv4.setText(MyDateUtils.stringToYMD(item.getSENDTIME()));
                    tv5.setText(item.getREADED_state());
                    LinearLayout ll = holder.getView(R.id.lv_yj_ly);
                    if (position % 2 == 0){
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
                    }else {
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
                    }
                }
            };
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        if(lv.isRefreshing()) {
            lv.onRefreshComplete();//停止刷新或加载更多
        }
    }
}

