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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.OvertimeDialogActivity;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.BtInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.MyFollowInfo;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by liutao on 2018/1/5.
 *
 * 补贴信息
 */

public class BtInfoFragment extends BaseVpFragment{

    private PersonListInfo pInfo;

    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时

    private View view;
    private List<BtInfo> data=new ArrayList<>();
    private CommonAdapter adapter;
    private PullToRefreshListView lv;

    private int PageIndex=0;


    public static final BtInfoFragment newInstance(PersonListInfo p){

        BtInfoFragment fragment=new BtInfoFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("pInfo",p);
        fragment.setArguments(bundle);
        return  fragment;

    }

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED:

                    if(PageIndex==0) {
                        data.clear();
                    }
                    data.addAll((List<BtInfo>)msg.obj);
                    LvSetAdapter(data);


                    break;
                case PROBLEM:
                    if(getActivity()!=null) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                    if(lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;
                case SUCCEED_NODATA:

                    Log.e("2018-1-9","暂无数据");

                    if(getActivity()!=null) {
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    if(lv.isRefreshing()) {
                        lv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;
                case OVERTIME:

                    if(getActivity()!=null) {
                        Intent i = new Intent(getActivity(), OvertimeDialogActivity.class);
                        startActivity(i);
                    }

                    break;


            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pInfo=(PersonListInfo) getArguments().getSerializable("pInfo");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_bt_info,container,false);

        isViewCreated=true;//标记不能少

        return view;
    }

    @Override
    public void lazyLoadData() {

        if(isViewCreated){//逻辑都写这个里面

            initViews();

        }

    }

    private void initViews(){

        lv= (PullToRefreshListView) view.findViewById(R.id.fragment_bt_info_lv);


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
        if(pInfo==null){
            if(lv.isRefreshing()) {
                lv.onRefreshComplete();//停止刷新或加载更多
            }
            return;
        }
        //http://web.youli.pw:8088/Json/Get_grbtxx.aspx?sfz=110101196212083069
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_grbtxx.aspx?sfz="+ pInfo.getZJHM();

                        Log.e("2018-1-9","url=="+url);

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<BtInfo>>(){}.getType());
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

    private void LvSetAdapter(List<BtInfo> list){

        if(adapter==null){

            adapter=new CommonAdapter<BtInfo>(getContext(),list,R.layout.item_fragment_bt_info) {
                @Override
                public void convert(CommonViewHolder holder, BtInfo item, int position) {

                    TextView tvNo=holder.getView(R.id.tv_item_fragment_bt_info_no);//编号
                    tvNo.setText((position+1)+"");
                    TextView tvTime=holder.getView(R.id.tv_item_fragment_bt_info_time);//支付年月
                    tvTime.setText(item.getBtzfny());
                    TextView tvName=holder.getView(R.id.tv_item_fragment_bt_info_name);//补贴项目名称
                    tvName.setText(item.getBtxmmc());
                    TextView tvMoney=holder.getView(R.id.tv_item_fragment_bt_info_money);//补贴金额
                    tvMoney.setText(item.getBtje());
                    TextView tvNature=holder.getView(R.id.tv_item_fragment_bt_info_nature);//补贴性质
                    tvNature.setText(item.getBtxz());

                    LinearLayout ll = holder.getView(R.id.item_fragment_bt_info_ll);

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

    }

}
