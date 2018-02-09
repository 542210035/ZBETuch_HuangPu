package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.youli.zbetuch_huangpu.entity.NaireListInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/11/13.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 专项调查
 */

public class SpecialDiaochaActivity extends BaseActivity{

    private Context mContext=this;

    private  final int SUCCESS=10000;
    private  final int FAIL=10001;
    private final int NODATA=10002;

    private TextView numTv;
    private PullToRefreshListView lv;
    private List<NaireListInfo> naireList=new ArrayList<>();
    private CommonAdapter adapter;

    private int pageIndex;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case SUCCESS:
                    if (lv.isRefreshing()) {
                        lv.onRefreshComplete();
                    }
                    if(pageIndex==0) {
                        naireList.clear();

                    }
                    naireList.addAll((List<NaireListInfo>)msg.obj);
                    if(naireList.size()!=0){
                        numTv.setText("共有"+naireList.get(0).getRecordCount()+"篇");
                    }
                    LvSetAdapter(naireList);
                    break;

                case  FAIL:
                    if (lv.isRefreshing()) {
                        lv.onRefreshComplete();
                    }
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;

                case NODATA:
                    if (lv.isRefreshing()) {
                        lv.onRefreshComplete();
                    }
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_diaocha);

        initViews();
    }

    private void initViews(){

        numTv= (TextView) findViewById(R.id.special_diaocha_num_tv);

        lv= (PullToRefreshListView) findViewById(R.id.lv_special_diaocha);
        lv.setMode(PullToRefreshBase.Mode.BOTH);

        initData();

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex=0;
                initData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                pageIndex++;
                initData();


            }
        });


    }

    private void initData(){

        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                       // http://web.youli.pw:8088/Json/Get_Qa_Master.aspx?page=0&rows=15

                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_Qa_Master.aspx?page="+pageIndex+"&rows=15";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            if(response.body()!=null){

                                try {
                                    String resStr=response.body().string();

                                    if(!TextUtils.equals(resStr,"")&&!TextUtils.equals(resStr,"[]")){
                                        Gson gson=new Gson();

                                        msg.obj=gson.fromJson(resStr,new TypeToken<List<NaireListInfo>>(){}.getType());

                                        msg.what=SUCCESS;

                                    }else{
                                        msg.what=NODATA;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                msg.what=FAIL;
                            }

                        }else{

                            msg.what=FAIL;

                        }

                        mHandler.sendMessage(msg);

                    }
                }

        ).start();

    }

    private void LvSetAdapter(List<NaireListInfo> data){

        if(adapter==null){

            adapter=new CommonAdapter<NaireListInfo>(mContext,data,R.layout.item_special_diaocha_lv) {
                @Override
                public void convert(CommonViewHolder holder, NaireListInfo item, int position) {

                    TextView numberTv=holder.getView(R.id.item_special_diaocha_number_tv);
                    numberTv.setText((position+1)+"");
                    TextView titleTv=holder.getView(R.id.item_special_diaocha_title_tv);
                    titleTv.setText(item.getTITLE());
                    TextView noTv=holder.getView(R.id.item_special_diaocha_no_tv);
                    noTv.setText(item.getNO());
                    TextView timeTv=holder.getView(R.id.item_special_diaocha_time_tv);
                    timeTv.setText(MyDateUtils.stringToYMD(item.getCREATE_TIME()));
                    LinearLayout ll = holder.getView(R.id.item_special_diaocha_ll);
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
