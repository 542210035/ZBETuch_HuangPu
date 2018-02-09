package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.StayInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import okhttp3.Response;

public class NeedWorkActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,AdapterView.OnItemClickListener {
    private Context mContext=NeedWorkActivity.this;
    private RadioGroup rg;
    private RadioButton weiRb,yiRb;
    private PullToRefreshListView lv;
    private String typeId;//已完成，未完成标记
    private int pageIndex;
    private TextView not;
    private CommonAdapter commonAdapter;
    List<StayInfo> nList=new ArrayList<StayInfo>();

    private final int SUCCESS=10000;
    private final int NODATA=10001;
    private final int PROBLEM=10002;
    private final int OVERTIME=10003;//登录超时

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            switch (msg.what){
                case SUCCESS:
                    if (pageIndex==0){
                        nList.clear();
                    }
                    nList.addAll((List<StayInfo>) msg.obj);
                    if (nList.size()>0){
                        not.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        lvsetAdapter(nList);
                    }else {
                        not.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }
                    break;

                case PROBLEM:
                    Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();
                    break;

                case OVERTIME:
                    Intent intent=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_work);
        initViews();
    }
    private void initViews(){
        not= (TextView) findViewById(R.id.not);
        weiRb= (RadioButton) findViewById(R.id.wei_meet_manage_current);
        yiRb= (RadioButton) findViewById(R.id.yi_meet_manage_history);
        rg= (RadioGroup) findViewById(R.id.wyRg);
        rg.check(R.id.wei_meet_manage_current);
        rg.setOnCheckedChangeListener(this);
        lv= (PullToRefreshListView) findViewById(R.id.daiban_detail_lv);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnItemClickListener(this);
        getNetWorkData(pageIndex);
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
    }

    private void getNetWorkData(final int page){
        ProgressDialogUtils.showMyProgressDialog(this);
        if(page==0) {
            if (nList != null && nList.size() > 0 ) {
                nList.clear();
            }
        }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = null;
                        if (rg.getCheckedRadioButtonId()==R.id.wei_meet_manage_current){
                             url= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Work_Info.aspx?type=未完成&page="+page+"&rows=20";
                        }else if (rg.getCheckedRadioButtonId()==R.id.yi_meet_manage_history){
                            url= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Work_Info.aspx?type=已完成&page="+page+"&rows=20";
                        }

                        Log.e("2017-12-20","url="+url);

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        try {
                            Message msg=Message.obtain();
                        if (response !=null){
                                String infoStr=response.body().string();
                            Gson gson=new Gson();
                            try {
                                msg.what=SUCCESS;
                                msg.obj=gson.fromJson(infoStr,new TypeToken<List<StayInfo>>(){}.getType());
                                mHandler.sendMessage(msg);
                            }catch (Exception e){

                                msg.what=OVERTIME;
                                mHandler.sendMessage(msg);
                            }

                            }else{

                            sendProblemMessage(msg);
                        }
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    private void sendProblemMessage(Message msg) {
        msg.what = PROBLEM;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId) {

        switch (checkedId){
            case R.id.wei_meet_manage_current:
             //   rg.check(R.id.wei_meet_manage_current);
                getNetWorkData(0);

                break;
            case R.id.yi_meet_manage_history:
               // rg.check(R.id.yi_meet_manage_history);
                getNetWorkData(0);
                break;
        }


    }


    private void lvsetAdapter(final List<StayInfo> data) {
        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter(mContext, data, R.layout.item_new_needwork) {
                @Override
                public void convert(CommonViewHolder holder, Object item, int position) {
                    TextView bianTv=holder.getView(R.id.bianhao);
                    bianTv.setText(position+1+"");
                    TextView themeTv = holder.getView(R.id.item_daiban_detail_num_tv);
                    themeTv.setText(data.get(position).getTitle());
                    TextView timeTv = holder.getView(R.id.item_daiban_detail_name_tv);
                    timeTv.setText(MyDateUtils.stringToYMDHMS(data.get(position).getCreate_date()));
                    TextView stateTv = holder.getView(R.id.item_daiban_detail_idcard_tv);
                    stateTv.setText(data.get(position).getType());
                    TextView remTv = holder.getView(R.id.item_daiban_detail_juwei_tv);
                    remTv.setText(data.get(position).getMark());
                    LinearLayout ll = holder.getView(R.id.item_daiban_detail_ll);

                    if (position % 2 == 0) {
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
                    } else {
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
                    }

                }
            };

            lv.setAdapter(commonAdapter);
        }else {
            commonAdapter.notifyDataSetChanged();
        }
        if (lv.isRefreshing()) {
            lv.onRefreshComplete();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent mIntent=new Intent(mContext,NeedWebActivity.class);
        mIntent.putExtra("RDInfoo",nList.get(position-1).getId());
        mIntent.putExtra("StayInfo",nList.get(position-1));
        startActivity(mIntent);

    }
}
