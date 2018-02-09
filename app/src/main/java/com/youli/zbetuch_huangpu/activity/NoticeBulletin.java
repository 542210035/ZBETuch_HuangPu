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
import com.youli.zbetuch_huangpu.entity.NoticeInfo;
import com.youli.zbetuch_huangpu.entity.ResourcesDetailInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class NoticeBulletin extends BaseActivity implements AdapterView.OnItemClickListener{

    private Context mContext=NoticeBulletin.this;
    private TextView tv,tv1;
    private PullToRefreshListView lv;
    private int pageIndex;
    private List<NoticeInfo> nList=new ArrayList<>();
    private CommonAdapter commonAdapter;


    private final int SUCCESS=10000;
    private final int NODATA=10001;
    private final int PROBLEM=10002;  //网络不给力
    private final int OVERTIME=10003;//登录超时

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ProgressDialogUtils.dismissMyProgressDialog(mContext);
            switch (msg.what){
                case SUCCESS:
                    if(pageIndex==0){
                        nList.clear();
                    }
                    nList.addAll((List<NoticeInfo>)msg.obj);
                    if(nList.size()>0){
                        tv1.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        lvSetAdapter(nList);
                    }else {
                        tv.setText("总共0条数据");
                        tv1.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
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
                    Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_bulletin);
        initViews();
    }
    private void initViews(){
        tv1= (TextView) findViewById(R.id.tongzhi_detail_tv_nodata);
        tv= (TextView) findViewById(R.id.tv_tongzhi_naire_num);
        lv= (PullToRefreshListView) findViewById(R.id.lv_tongzhi_naire);
        lv.setMode(PullToRefreshBase.Mode.BOTH);  //设置上下拉都可以刷新
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
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        //http://web.youli.pw:8088/Json/First/Get_News.aspx?page=0&rows=15
                        String url= MyOkHttpUtils.BaseUrl+"/Json/First/Get_News.aspx?page="+page +"&rows=20";
                        Log.e("----2017-12-1---","url="+url);
                        Response response= MyOkHttpUtils.okHttpGet(url);
                        Message msg=Message.obtain();
                        try {
                            if(response!=null){
                                String infoStr=response.body().string();
                                Gson gson=new Gson();
                                try{
                                    msg.what=SUCCESS;
                                    msg.obj=gson.fromJson(infoStr,
                                            new TypeToken<List<NoticeInfo>>(){}.getType());
                                    handler.sendMessage(msg);
                                }catch(Exception e){
                                    Log.e("2017/11/13","登录超时了");
                                    msg.what=OVERTIME;
                                    handler.sendMessage(msg);
                                }
                            }else{
                                sendProblemMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

    }

    private void sendProblemMessage(Message msg) {
        msg.what = PROBLEM;
        handler.sendMessage(msg);
    }


    //adapter
    private void lvSetAdapter(final List<NoticeInfo> data){

        if(commonAdapter==null) {

            commonAdapter = new CommonAdapter<NoticeInfo>(this, nList, R.layout.item_tongzhi_detail_lv) {
                @Override
                public void convert(CommonViewHolder holder, NoticeInfo item, int position) {

                    TextView numTv = holder.getView(R.id.item_tongzhi_detail_num_tv);
                    numTv.setText(MyDateUtils.stringToYMDHMS(data.get(position).getCREATE_TIME()));
                    TextView nameTv = holder.getView(R.id.item_tongzhi_detail_name_tv);
                    nameTv.setText(data.get(position).getTITLE());

                    LinearLayout ll = holder.getView(R.id.item_tongzhi_detail_ll);
//                    if (position % 2 == 0) {
//                        ll.setBackgroundResource(R.drawable.selector_ziyuandiaocha_item1);
//                    } else {
//                        ll.setBackgroundResource(R.drawable.selector_ziyuandiaocha_item2);
//                    }
                    if (position % 2 == 0){
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
                    }else {
                        ll.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
                    }
                }
            };

            lv.setAdapter(commonAdapter);

        }else{

            commonAdapter.notifyDataSetChanged();

        }
        if (lv.isRefreshing()) {
            lv.onRefreshComplete();
        }
        tv.setText("总共" + data.get(0).getRecordCount() + "条数据");
    }

        @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(this,ContentActivity.class);
            intent.putExtra("RDInfoo",nList.get(position-1).getID());
            intent.putExtra("Nr",nList.get(position-1).getTITLE());
            intent.putExtra("Content",nList.get(position-1).getDOC());
            startActivity(intent);
        }
}
