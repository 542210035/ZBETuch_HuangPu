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
import com.youli.zbetuch_huangpu.entity.InspectorInfo;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.entity.ZhiwuInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by liutao on 2018/1/2.
 */

public class PersonInfoListActivity extends BaseActivity{


    private Context mContext=this;

    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时

    private List<PersonListInfo> data=new ArrayList<>();
    private CommonAdapter adapter;
    private PullToRefreshListView lv;

    private int PageIndex=0;

    private TextView tvNum,tvNoData;

    private String sfzStr,paramStr;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED:

                    if(PageIndex==0||sfzStr!=null) {
                        data.clear();
                    }
                    data.addAll((List<PersonListInfo>)msg.obj);

                    if(data!=null) {
                        if(sfzStr==null) {
                            tvNum.setText("共有" + data.get(0).getRecordCount() + "人");
                        }
                        tvNoData.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
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
                    if(data.size()==0) {
                        tvNoData.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }
                    if(lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
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
        setContentView(R.layout.activity_person_info_list);

        sfzStr=getIntent().getStringExtra("sfz");
        paramStr=getIntent().getStringExtra("param");
        initViews();

    }

    private void initViews(){

        tvNoData= (TextView) findViewById(R.id.tv_person_info_list_no_data);

        tvNum= (TextView) findViewById(R.id.tv_person_info_list_num);

        lv= (PullToRefreshListView) findViewById(R.id.lv_person_info_list);

        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(mContext,PersonDetaileInfoActivity.class);
                intent.putExtra("pInfo",data.get(i-1));
                intent.putExtra("zjhm",data.get(i-1).getZJHM());
                startActivity(intent);
            }
        });
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
            }
        });

        initDatas(PageIndex);

    }


    private void initDatas(final int pIndex){


        new Thread(


                new Runnable() {
                    @Override
                    public void run() {
                        String url;

                        if(sfzStr==null) {
// http://web.youli.pw:8088/Json/Get_BASIC_INFORMATION.aspx?page=0&xm=&rows=20&gender=&whcd=&hjjd=&hjjw=&nld_ks=&ndl_js=&jyzt=&dcmqzk_last=&dcdqyx_last=&rbl_gq=&syys_ks=&syys_js=
                            url = MyOkHttpUtils.BaseUrl + "/Json/Get_BASIC_INFORMATION.aspx?page=" + PageIndex +paramStr;

                            Log.e("2018-1-4","url="+url);

                        }else{
                          //  http://web.youli.pw:8088/Json/Get_BASIC_INFORMATION.aspx?sfz=110102196901201936
                            url=MyOkHttpUtils.BaseUrl+"/Json/Get_BASIC_INFORMATION.aspx?sfz="+sfzStr;
                        }
                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"[]")&&!TextUtils.equals(meetStr,"[null]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<PersonListInfo>>(){}.getType());
                                    msg.what=SUCCEED;
                                }else{
                                    msg.what=SUCCEED_NODATA;
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

    private void LvSetAdapter(List<PersonListInfo> list){


        if(adapter==null){

            adapter=new CommonAdapter<PersonListInfo>(mContext,list,R.layout.item_person_info_list) {
                @Override
                public void convert(CommonViewHolder holder, PersonListInfo item, int position) {

                    TextView tvNo=holder.getView(R.id.tv_item_person_info_list_no);
                    tvNo.setText((position+1)+"");
                    TextView tvName=holder.getView(R.id.tv_item_person_info_list_name);
                    tvName.setText(item.getXM());
                    TextView tvSex=holder.getView(R.id.tv_item_person_info_list_sex);
                    tvSex.setText(item.getGENDER());
                    TextView tvBirth=holder.getView(R.id.tv_item_person_info_list_birth);
                    tvBirth.setText(MyDateUtils.stringToYMD(item.getCSDATE1()));
                    TextView tvCurrent=holder.getView(R.id.tv_item_person_info_list_current);
                    tvCurrent.setText(item.getDCMQZK_LAST());
                    TextView tvMd=holder.getView(R.id.tv_item_person_info_list_md);
                    tvMd.setText(item.getDCDQYX_LAST());

                    LinearLayout ll = holder.getView(R.id.item_person_info_list_ll);

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

    };

}
