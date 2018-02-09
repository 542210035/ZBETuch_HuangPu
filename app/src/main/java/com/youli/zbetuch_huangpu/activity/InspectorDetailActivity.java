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
import com.youli.zbetuch_huangpu.entity.InspectorDetailInfo;
import com.youli.zbetuch_huangpu.entity.InspectorInfo;
import com.youli.zbetuch_huangpu.entity.ZhiwuInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/12/4.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 督察督办详情界面
 */

public class InspectorDetailActivity extends BaseActivity{


    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时

    private Context mContext=this;

  //  private PullToRefreshListView lv;
    private ListView lv;
    private List<InspectorDetailInfo> data=new ArrayList<>();
    private CommonAdapter adapter;

    private int PageIndex=0;

    private int masterId;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED:

                   // if(PageIndex==0) {
                        data.clear();
                   // }
                    data.addAll((List<InspectorDetailInfo>)msg.obj);
                    LvSetAdapter(data);


                    break;
                case PROBLEM:
                    Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();
//                    if(lv.isRefreshing()) {
//                        lv.onRefreshComplete();//停止刷新或加载更多
//                    }
                    break;
                case SUCCEED_NODATA:
//                    if(lv.isRefreshing()) {
//                        lv.onRefreshComplete();//停止刷新或加载更多
//                    }
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
        setContentView(R.layout.activity_inspector_detail);

        masterId=((InspectorInfo)getIntent().getSerializableExtra("InInfo")).getId();

        initViews();

    }

    private void initViews(){


        lv= (ListView) findViewById(R.id.lv_inspector_detail);
//        lv.setMode(PullToRefreshBase.Mode.BOTH);
//        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                //刷新
//                PageIndex=0;
//                initDatas(PageIndex);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                //加载更多
//                PageIndex++;
//                initDatas(PageIndex);
//            }
//        });

        initDatas(PageIndex);


    }


    private void initDatas(final int pIndex){

        //http://web.youli.pw:8088/Json/First/Get_Work_Notice_finish.aspx?master_id=1
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Work_Notice_finish.aspx?master_id="+masterId;

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<InspectorDetailInfo>>(){}.getType());
                                    msg.what=SUCCEED;
                                }else{
                                    msg.what=SUCCEED_NODATA;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("2017/11/13","登录超时了");
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

    private void LvSetAdapter(List<InspectorDetailInfo> list){


        if(adapter==null){

            adapter=new CommonAdapter<InspectorDetailInfo>(mContext,list,R.layout.item_inspector_detail_lv) {
                @Override
                public void convert(CommonViewHolder holder, InspectorDetailInfo item, int position) {

                    TextView noTv=holder.getView(R.id.item_inspector_detail_number_tv);//编号
                    noTv.setText((position+1)+"");
                    TextView nameTv=holder.getView(R.id.item_inspector_detail_name_tv);//姓名
                    nameTv.setText(item.getName());
                    TextView createTimeTv=holder.getView(R.id.item_inspector_detail_create_time_tv);//状态
                    createTimeTv.setText(item.getType());
                    TextView comTimeTv=holder.getView(R.id.item_inspector_detail_com_time_tv);//备注
                    comTimeTv.setText(item.getMark());
                    TextView stateTv=holder.getView(R.id.item_inspector_detail_state_tv);//时间
                    stateTv.setText(MyDateUtils.stringToYMD(item.getCheck_date()));

                    LinearLayout ll = holder.getView(R.id.item_inspector_detail_ll);
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
//        if(lv.isRefreshing()) {
//            lv.onRefreshComplete();//停止刷新或加载更多
//        }

    }
}
