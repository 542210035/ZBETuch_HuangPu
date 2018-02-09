package com.youli.zbetuch_huangpu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.OvertimeDialogActivity;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.BtInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
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
 * 学习经历
 */

public class StudyJlFragment extends BaseVpFragment implements ScrollViewListener{

    private View view;

    private PersonListInfo pInfo;

    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时

    private MyHorizontalScrollView titleHsv,contentHsv;

    private PullToRefreshScrollView psv;

    private List<StudyJlInfo> data=new ArrayList<>();
    private CommonAdapter adapter;
    private MyListView lv;

    private int PageIndex=0;


    public static final StudyJlFragment newInstance(PersonListInfo p){

        StudyJlFragment fragment=new StudyJlFragment();
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
                    data.addAll((List<StudyJlInfo>)msg.obj);
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

        view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_study_jl,container,false);

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

        titleHsv= (MyHorizontalScrollView) view.findViewById(R.id.hsv_fragment_study_jl_title);
        contentHsv= (MyHorizontalScrollView) view.findViewById(R.id.hsv_fragment_study_jl_content);
        psv= (PullToRefreshScrollView) view.findViewById(R.id.psv_study_jl);

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
        titleHsv.setOnScrollViewListener(this);
        contentHsv.setOnScrollViewListener(this);

        lv= (MyListView) view.findViewById(R.id.lv_fragment_study_jl);
        initDatas(PageIndex);
    }

    private void initDatas(final int pIndex){
        if(pInfo==null){
            if(psv.isRefreshing()) {
                psv.onRefreshComplete();//停止刷新或加载更多
            }
            return;
        }
        //http://web.youli.pw:8088/Json/Get_grxxjlxx.aspx?sfz=110101196212083069
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_grxxjlxx.aspx?sfz="+ pInfo.getZJHM();

                        Log.e("2018-1-9","学习经历url=="+url);

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<StudyJlInfo>>(){}.getType());
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

    private void LvSetAdapter(List<StudyJlInfo> list){

        if(adapter==null){

            adapter=new CommonAdapter<StudyJlInfo>(getContext(),list,R.layout.item_fragment_study_jl) {
                @Override
                public void convert(CommonViewHolder holder, StudyJlInfo item, int position) {

                    TextView tvNo=holder.getView(R.id.tv_item_fragment_study_jl_no);//编号
                    tvNo.setText((position+1)+"");
                    TextView tvStartTime=holder.getView(R.id.tv_item_fragment_study_jl_start_time);//起始日期
                    tvStartTime.setText(item.getQSDATE());
                    TextView tvEndTime=holder.getView(R.id.tv_item_fragment_study_jl_end_time);//终止日期
                    tvEndTime.setText(item.getZZDATE());
                    TextView tvShool=holder.getView(R.id.tv_item_fragment_study_shool_name);//学校名称
                    tvShool.setText(item.getXXMC());
                    TextView tvEdu=holder.getView(R.id.tv_item_fragment_study_edu);//文化程度
                    tvEdu.setText(item.getWHCD());
                    TextView tvMajor=holder.getView(R.id.tv_item_fragment_study_major);//所学专业
                    tvMajor.setText(item.getSXZY());
                    TextView tvGrad=holder.getView(R.id.tv_item_fragment_study_is_grad);//是否毕业
                    tvGrad.setText(item.getSFBYY());
                    TextView tvFullTime=holder.getView(R.id.tv_item_fragment_study_full_time);//是否全日制
                    tvFullTime.setText(item.getSFQRZ());
                    TextView tvMark=holder.getView(R.id.tv_item_fragment_study_mark);//备注
                    tvMark.setText(item.getBZ());

                    LinearLayout ll = holder.getView(R.id.item_fragment_study_jl_ll);

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
        if (scrollView == titleHsv) {
            contentHsv.scrollTo(x, y);

        } else if (scrollView == contentHsv) {
            titleHsv.scrollTo(x, y);

        }
    }
}
