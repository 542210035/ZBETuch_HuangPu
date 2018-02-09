package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.youli.zbetuch_huangpu.entity.TubeInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class CompanyActivity extends BaseActivity {
    private Context mContext=CompanyActivity.this;
    private PullToRefreshListView lv;
    private CommonAdapter adapter;  //apapter
    private List<CompanyInfo> data=new ArrayList<>();
    private int PageIndex;
    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10005;
    private final int OVERTIME=10006;//登录超时
    private int getId;  //获取上个界面传过来的ID
    private TextView no_tv;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            Intent i;
            switch (msg.what){
                case SUCCEED:
                    if(PageIndex==0) {
                        data.clear();
                    }
                    data.addAll((List<CompanyInfo>)msg.obj);
                    if (data.size()<0||data.equals(" ")){
                        no_tv.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }
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
        setContentView(R.layout.activity_company);
        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        getId= (int) intent.getSerializableExtra("ID");
        Log.e("2018-1-31",""+getId);
        lv= (PullToRefreshListView) findViewById(R.id.lv_my_zphdwxx);
        no_tv= (TextView) findViewById(R.id.zpdwxx_zwxx_tv);
        initDatas(PageIndex,getId);

        //上下拉刷新
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //刷新
                PageIndex=0;
                initDatas(PageIndex,getId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多
                PageIndex++;
                initDatas(PageIndex,getId);
            }});

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,PostActivity.class);
                intent.putExtra("name",data.get(position-1));
                startActivity(intent);

            }
        });

    }

    //网络请求数据
    private void initDatas(final int pIndex, final int id){
        //http://192.168.191.2:8088/Json/JobFail/Frm_JobFail_Companys.aspx?JobFailId=1&page=0&rows=10 根据JobFailId判断上个界面传过来的ID
        ProgressDialogUtils.showMyProgressDialog(mContext);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url= MyOkHttpUtils.BaseUrl+"/Json/JobFail/Frm_JobFail_Companys.aspx?JobFailId="+id+"&page="+pIndex+"&rows=10";
                    Response response=MyOkHttpUtils.okHttpGet(url);
                    Log.e("2018-1-31",url);
                    Message msg=Message.obtain();
                    if(response!=null){
                        try {
                            String meetStr=response.body().string();
                            if (!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){
                                Gson gson=new Gson();
                                msg.obj=gson.fromJson(meetStr,new TypeToken<List<CompanyInfo>>(){}.getType());
                                msg.what=SUCCEED;
                            }else {
                                msg.what=SUCCEED_NODATA;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("2018-1-31","登录超时了");
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
        private void LvSetAdapter(final List<CompanyInfo> list) {
            if (adapter == null) {
                adapter = new CommonAdapter<CompanyInfo>(mContext, data, R.layout.lv_item_zphdwxx) {
                    @Override
                    public void convert(CommonViewHolder holder, CompanyInfo item, int position) {
                        TextView tv1 = holder.getView(R.id.lv_zphdwxx_tv1);
                        TextView tv2 = holder.getView(R.id.lv_zphdwxx_tv2);
                        TextView tv3 = holder.getView(R.id.lv_zphdwxx_tv3);
                        TextView tv4 = holder.getView(R.id.lv_zphdwxx_tv4);
                        TextView tv5 = holder.getView(R.id.lv_zphdwxx_tv5);
                        tv1.setText((position + 1) + "");
                        tv2.setText(item.getCOMPANYNAME());
//                        Log.e("2018-27",data.get(position).getCompanyName());
                        tv3.setText(MyDateUtils.stringToYMD(item.getCREATETIME()));
                        tv4.setText(item.getCreateUserName());
                        tv5.setText(item.getJOBNUM()+"");
                        LinearLayout ll = holder.getView(R.id.lv_zphdwxx_ly);
                        if (position % 2 == 0){
                            ll.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
                        }else {
                            ll.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
                        }
                    }
                };
                lv.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
            if(lv.isRefreshing()) {
                lv.onRefreshComplete();//停止刷新或加载更多
            }
        }
}
