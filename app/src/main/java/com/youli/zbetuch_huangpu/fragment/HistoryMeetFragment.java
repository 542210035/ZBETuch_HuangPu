package com.youli.zbetuch_huangpu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.youli.zbetuch_huangpu.activity.MeetDetailActivity;
import com.youli.zbetuch_huangpu.activity.OvertimeDialogActivity;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.MeetInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/12/1.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 历史会议的fragment
 */

public class HistoryMeetFragment extends BaseFragment{


    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时
    private View contentView;

    private List<MeetInfo> data=new ArrayList<>();

    private PullToRefreshListView lv;

    private CommonAdapter adapter;

    private int PageIndex=0;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED:

                    if(PageIndex==0) {
                        data.clear();
                    }
                    data.addAll((List<MeetInfo>)msg.obj);
                    LvSetAdapter(data);


                    break;
                case PROBLEM:
                    Toast.makeText(getActivity(),"网络不给力",Toast.LENGTH_SHORT).show();
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

                    Intent i=new Intent(getActivity(),OvertimeDialogActivity.class);
                    startActivity(i);


                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_history_meet,container,false);

        return contentView;
    }

    //懒加载的方法
    //所有的逻辑都写在这里吧
    @Override
    protected void loadData() {

        initViews();

    }

    private void initViews(){

        lv= (PullToRefreshListView) contentView.findViewById(R.id.lv_fmt_history_meet);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(), MeetDetailActivity.class);

                intent.putExtra("meetInfo",data.get(position-1));
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

        //http://web.youli.pw:8088/Json/First/Get_Meeting_Master.aspx?page=0&rows=20&type=1
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String meetUrl= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Meeting_Master.aspx?page="+pIndex+"&rows=20&type=1";

                        Response response=MyOkHttpUtils.okHttpGet(meetUrl);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<MeetInfo>>(){}.getType());
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


    private void LvSetAdapter(final List<MeetInfo> list){

        if(adapter==null){

            adapter=new CommonAdapter<MeetInfo>(getActivity(),list,R.layout.item_fmt_meet_manage_lv) {
                @Override
                public void convert(CommonViewHolder holder, MeetInfo item, int position) {

                    TextView noTv=holder.getView(R.id.item_meet_manage_number_tv);//编号
                    noTv.setText((position+1)+"");
                    TextView themeTv=holder.getView(R.id.item_meet_manage_theme_tv);//会议主题
                    themeTv.setText(list.get(position).getTITLE());
                    TextView addressTv=holder.getView(R.id.item_meet_manage_address_tv);//会议地址
                    addressTv.setText(list.get(position).getMEETING_ADD());
                    TextView mTimeTv=holder.getView(R.id.item_meet_manage_mtime_tv);//会议时间
                    mTimeTv.setText(MyDateUtils.stringToYMDHMS(list.get(position).getMEETING_TIME()));
                    TextView notifierTv=holder.getView(R.id.item_meet_manage_notifier_tv);//通知人
                    notifierTv.setText(list.get(position).getCREATE_STAFF_NAME());
                    TextView rTimeTv=holder.getView(R.id.item_meet_manage_rtime_tv);//发布时间
                    rTimeTv.setText(MyDateUtils.stringToYMDHMS(list.get(position).getCREATE_DATE()));
                    LinearLayout ll = holder.getView(R.id.item_meet_manage_ll);
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
