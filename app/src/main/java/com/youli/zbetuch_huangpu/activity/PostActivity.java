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
import com.youli.zbetuch_huangpu.entity.PostInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class PostActivity extends BaseActivity {
    private PullToRefreshListView lv;

    private Context mContext = PostActivity.this;
    private int Indexes;
    private List<PostInfo> data = new ArrayList<>();
    private CompanyInfo companyInfo;//传过来的对象
    private CommonAdapter adapter;

    private final int SUCCEED = 10001;
    private final int SUCCEED_NODATA = 10002;
    private final int PROBLEM = 10005;
    private final int OVERTIME = 10006;//登录超时

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            Intent i;
            switch (msg.what) {
                case SUCCEED:
                    if (Indexes == 0) {
                        data.clear();
                    }
                    data.addAll((List<PostInfo>) msg.obj);
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
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        lv= (PullToRefreshListView) findViewById(R.id.lv_my_zpgwxx);

        companyInfo = (CompanyInfo) getIntent().getSerializableExtra("name");
        Log.e("tthj",companyInfo.getID()+"");

        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Indexes = 0;
                initDates(Indexes, companyInfo.getID());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Indexes++;
                initDates(Indexes, companyInfo.getID());
            }
        });
        initDates(Indexes, companyInfo.getID());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intnetn=new Intent(mContext,AdoptActivity.class);
                intnetn.putExtra("IDD",data.get(position-1).getID());
                Log.e("2018-2-8-8",""+data.get(position-1).getID());
                startActivity(intnetn);
            }
        });
    }

    private void initDates(final int inda, final int id) {
        //http://192.168.191.2:8088/Json/JobFail/Get_JobsInfo.aspx?JobComPanyId=7&page=0&rows=10   //招聘岗位信息
        ProgressDialogUtils.showMyProgressDialog(mContext);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = MyOkHttpUtils.BaseUrl + "/Json/JobFail/Get_JobsInfo.aspx?JobComPanyId=" + id + "&page=" + inda + "&rows=10";
                Response response = MyOkHttpUtils.okHttpGet(url);
                Log.e("2018-2-7", url);
                Message msg = Message.obtain();
                if (response != null) {
                    try {
                        String meetStr = response.body().string();
                        if (!TextUtils.equals(meetStr, "") && !TextUtils.equals(meetStr, "[]")) {
                            Gson gson = new Gson();
                            msg.obj = gson.fromJson(meetStr, new TypeToken<List<PostInfo>>(){}.getType());
                            msg.what = SUCCEED;
                        } else {
                            msg.what = SUCCEED_NODATA;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("2018-1-31", "登录超时了");
                        msg.what = OVERTIME;
                    }
                } else {
                    msg.what = PROBLEM;
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    //adapter
    private void getAdapters(List<PostInfo> list) {
//        Toast.makeText(mContext,"进入",Toast.LENGTH_SHORT).show();
        if (adapter == null) {
            adapter = new CommonAdapter<PostInfo>(mContext, list, R.layout.post_gwxx_item_lv) {
                @Override
                public void convert(CommonViewHolder holder, PostInfo item, int position) {
                    TextView tv1 = holder.getView(R.id.lv_gwxx_itwm1);
                    TextView tv2 = holder.getView(R.id.lv_gwxx_itwm2);
                    TextView tv3 = holder.getView(R.id.lv_gwxx_itwm3);
                    tv1.setText((position + 1) + "");
                    Log.e("aaaa",(position + 1) + "");
                    tv2.setText(item.getJOBNAME());
                    Log.e("bbbb",item.getJOBNAME());

                    tv3.setText(item.getMSNum()+"");
                    LinearLayout ll = holder.getView(R.id.lv_gwxx_ly);
                    if (position % 2 == 0) {
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
                    } else {
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
                    }
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
