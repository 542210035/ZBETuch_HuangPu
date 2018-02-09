package com.youli.zbetuch_huangpu.activity;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.youli.zbetuch_huangpu.entity.AdoptInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.PostInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class AdoptActivity extends BaseActivity {
    private Context mContext=AdoptActivity.this;
    @BindView(R.id.lv_my_zphdwxx)
    PullToRefreshListView lv;
    @BindView(R.id.zpdwxx_zwxx_tv)
    TextView wsj;
    private int indexes;
    private List<AdoptInfo> data=new ArrayList();
    private CommonAdapter adapter;

    private final int SUCCEED = 10001;
    private final int SUCCEED_NODATA = 10002;
    private final int PROBLEM = 10005;
    private final int OVERTIME = 10006;//登录超时
    private int a; //上个页面传过来的ID




    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            Intent i;
            switch (msg.what) {
                case SUCCEED:
                    if (indexes == 0) {
                        data.clear();
                    }
                    data.addAll((List<AdoptInfo>) msg.obj);
                    getAdapters(data);
                    break;

                case PROBLEM:
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                    if (lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;

                case SUCCEED_NODATA:
                    if (lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;

                case OVERTIME:
                    i = new Intent(mContext, OvertimeDialogActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        a= (int) getIntent().getSerializableExtra("IDD");
        Log.e("2018_aaa",""+a);

        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                indexes=0;
                initDates(indexes,a);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                indexes++;
                initDates(indexes,a);
            }
        });
        initDates(indexes,a);

    }



    private void initDates(final int inda, final int id) {
        //http://192.168.191.3:8088/Json/JobFail//Get_JobFail_Interview_Info.aspx?JobId=25&page=0&rows=10
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url= MyOkHttpUtils.BaseUrl+"/Json/JobFail//Get_JobFail_Interview_Info.aspx?JobId="+id+"&page="+inda+"&rows=10";
                Response response=MyOkHttpUtils.okHttpGet(url);
                Log.e("2018-2-8", url);
                Message msg = Message.obtain();
                if (response !=null){
                    try {
                        String meetStr=response.body().string();
                        if (!TextUtils.equals(meetStr,"") && !TextUtils.equals(meetStr,"[]")){
                            Gson gson=new Gson();
                            msg.obj=gson.fromJson(meetStr,new TypeToken<List<AdoptInfo>>(){}.getType());
                            msg.what=SUCCEED;
                        }else {
                            msg.what = SUCCEED_NODATA;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("2018-1-31", "登录超时了");
                        msg.what = OVERTIME;
                    }
                }else {
                    msg.what = PROBLEM;
                }
                mHandler.sendMessage(msg);
            }
        }).start();

    }


    //adapter
    private void getAdapters(final List<AdoptInfo> list) {
//        Toast.makeText(mContext,"进入",Toast.LENGTH_SHORT).show();
        if (adapter == null) {
            adapter = new CommonAdapter(mContext, list, R.layout.activity_lv_item_msxx) {
                @Override
                public void convert(CommonViewHolder holder, Object item, int position) {
                    TextView tv2=holder.getView(R.id.lv_item_msxx_tv2);
                    TextView tv3=holder.getView(R.id.lv_item_msxx_tv3);
                    TextView tv4=holder.getView(R.id.lv_item_msxx_tv4);
                    TextView tv5=holder.getView(R.id.lv_item_msxx_tv5);
                    TextView tv6=holder.getView(R.id.lv_item_msxx_tv6);
                    TextView tv7=holder.getView(R.id.lv_item_msxx_tv7);
                    tv2.setText(list.get(position).getCOMPANYNAME());
                    tv3.setText(list.get(position).getJOBNAME());
                    tv4.setText(list.get(position).getNAME());
                    tv5.setText(list.get(position).getIDCARD());
                    tv6.setText(list.get(position).getPHONE());
                    tv7.setText(list.get(position).getYXLY());
                }

            };
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (lv.isRefreshing()) {
            lv.onRefreshComplete();//停止刷新或加载更多
        }
    }


}
