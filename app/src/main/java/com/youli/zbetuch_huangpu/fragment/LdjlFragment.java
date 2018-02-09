package com.youli.zbetuch_huangpu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.OvertimeDialogActivity;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.LdjlInfo;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.entity.StudyJlInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.view.MyHorizontalScrollView;
import com.youli.zbetuch_huangpu.view.MyListView;
import com.youli.zbetuch_huangpu.view.ScrollViewListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by liutao on 2018/1/5.
 *
 * 劳动经历
 */

public class LdjlFragment extends BaseVpFragment implements ScrollViewListener{

    private View view;

    private PersonListInfo pInfo;

    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时

    private MyHorizontalScrollView titleHsv,contentHsv;

    private PullToRefreshScrollView psv;

    private List<LdjlInfo> data=new ArrayList<>();
    private CommonAdapter adapter;
    private MyListView lv;

    private int PageIndex=0;

    public static final LdjlFragment newInstance(PersonListInfo p){

        LdjlFragment fragment=new LdjlFragment();
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
                    data.addAll((List<LdjlInfo>)msg.obj);
                    LvSetAdapter(data);


                    break;
                case PROBLEM:
                    if(getActivity()!=null) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                    if(psv.isRefreshing()) {
                        psv.onRefreshComplete();//停止刷新或加载更多
                    }
                    break;
                case SUCCEED_NODATA:

                    Log.e("2018-1-9","暂无数据");

                    if(getActivity()!=null) {
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    if(psv.isRefreshing()) {
                        psv.onRefreshComplete();//停止刷新或加载更多
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

        view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_ldjl,container,false);

        isViewCreated=true;//标记不能少


        return view;
    }

    @Override
    public void lazyLoadData() {

        if(isViewCreated){//逻辑都写这个里面
            initViews();

        }

    }

    private void initViews() {


        titleHsv= (MyHorizontalScrollView) view.findViewById(R.id.hsv_fragment_ldjl_title);
        contentHsv= (MyHorizontalScrollView) view.findViewById(R.id.hsv_fragment_ldjl_content);

        titleHsv.setOnScrollViewListener(this);
        contentHsv.setOnScrollViewListener(this);

        psv= (PullToRefreshScrollView) view.findViewById(R.id.psv_ldjl);

        psv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //刷新
                PageIndex=0;
                initDatas(PageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //加载更多
                PageIndex++;
                initDatas(PageIndex);
            }
        });

       lv = (MyListView) view.findViewById(R.id.lv_fragment_ldjl);

        initDatas(PageIndex);

    }


    private void initDatas(final int pIndex){

        if(pInfo==null){
            if(psv.isRefreshing()) {
                psv.onRefreshComplete();//停止刷新或加载更多
            }
            return;
        }

        //http://web.youli.pw:8088/Json/Get_grldjlxx.aspx?sfz=110101196212083069
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_grldjlxx.aspx?sfz="+ pInfo.getZJHM();

                        Log.e("2018-1-10","劳动经历url=="+url);

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<LdjlInfo>>(){}.getType());
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


    private void LvSetAdapter(List<LdjlInfo> list){

        if(adapter==null){

            adapter=new CommonAdapter<LdjlInfo>(getContext(),list,R.layout.item_fragment_ldjl) {
                @Override
                public void convert(CommonViewHolder holder, LdjlInfo item, int position) {

                    TextView tvNo=holder.getView(R.id.tv1);//编号
                    tvNo.setText((position+1)+"");
                    TextView tvDWMC=holder.getView(R.id.tv2);//工作单位名称
                    tvDWMC.setText(item.getDWMC());
                    TextView tvDWXZ=holder.getView(R.id.tv3);//单位性质
                    tvDWXZ.setText(item.getDWXZ());
                    TextView tvQSDATE=holder.getView(R.id.tv4);//起始日期
                    tvQSDATE.setText(item.getQSDATE());
                    TextView tvZZDATE=holder.getView(R.id.tv5);//终止日期
                    tvZZDATE.setText(item.getZZDATE());
                    TextView tvJYLX=holder.getView(R.id.tv6);//就业类型
                    tvJYLX.setText(item.getJYLX());
                    TextView tvYGXS=holder.getView(R.id.tv7);//用工形式
                    tvYGXS.setText(item.getYGXS());
                    TextView tvYGYY=holder.getView(R.id.tv8);//退工原因
                    tvYGYY.setText(item.getYGYY());
                    TextView tvBZ=holder.getView(R.id.tv9);//备注
                    tvBZ.setText(item.getBZ());
                    TextView tvJYDJDATE=holder.getView(R.id.tv10);//就业登记日期
                    tvJYDJDATE.setText(item.getJYDJDATE());
                    TextView tvTGDJDATE=holder.getView(R.id.tv11);//退工登记日期
                    tvTGDJDATE.setText(item.getTGDJDATE());
                    TextView tvJYDJSZD=holder.getView(R.id.tv12);//就业登记所在地
                    tvJYDJSZD.setText(item.getJYDJSZD());
                    TextView tvTGDJSZD=holder.getView(R.id.tv13);//退工登记所在地
                    tvTGDJSZD.setText(item.getTGDJSZD());


                    LinearLayout ll = holder.getView(R.id.item_fragment_ldjl_ll);

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

    @Override
    public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {

        if(scrollView==contentHsv){
            titleHsv.scrollTo(x,y);
        }else if(scrollView==titleHsv){
            contentHsv.scrollTo(x,y);
        }

    }
}
