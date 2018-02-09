package com.youli.zbetuch_huangpu.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.youli.zbetuch_huangpu.entity.MyFollowInfo;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.entity.ZhiwuInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/12/5.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 我的关注
 */

public class MyFollowActivity extends BaseActivity{

    private Context mContext=this;

    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int SUCCEED_FOLLOW=10003;
    private final int SUCCEED_PINFO=10004;
    private final int  PROBLEM=10005;
    private final int OVERTIME=10006;//登录超时


    private PullToRefreshListView lv;
    private List<MyFollowInfo> data=new ArrayList<>();
    private CommonAdapter adapter;

    private int PageIndex=0;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent i;

            switch (msg.what){

                case SUCCEED:

                    if(PageIndex==0) {
                        data.clear();
                    }
                    data.addAll((List<MyFollowInfo>)msg.obj);
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

                case SUCCEED_FOLLOW:
                    if(TextUtils.equals(msg.obj+"","True")){
                        data.remove(msg.arg1);
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case SUCCEED_PINFO:
                            i=new Intent(mContext,PersonDetaileInfoActivity.class);
                            i.putExtra("pInfo",((List<PersonListInfo>)msg.obj).get(0));
                            startActivity(i);

                    break;

            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);

        initViews();
    }

    private void initViews(){

        lv= (PullToRefreshListView) findViewById(R.id.lv_my_follow);
        lv.setMode(PullToRefreshBase.Mode.BOTH);

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

        //http://web.youli.pw:8088/Json/First/Get_Attention.aspx?page=0&rows=20
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/First/Get_Attention.aspx?page="+pIndex+"&rows=20";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<MyFollowInfo>>(){}.getType());
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

    private void LvSetAdapter(List<MyFollowInfo> list){


        if(adapter==null){

            adapter=new CommonAdapter<MyFollowInfo>(mContext,list,R.layout.item_my_follow_lv) {
                @Override
                public void convert(CommonViewHolder holder, MyFollowInfo item, final int position) {

                    TextView noTv=holder.getView(R.id.item_my_follow_number_tv);//编号
                    noTv.setText((position+1)+"");
                    TextView nameTv=holder.getView(R.id.item_my_follow_name_tv);//姓名
                    nameTv.setText(item.getNAME());
                    TextView sfzTv=holder.getView(R.id.item_my_follow_sfz_tv);//身份证
                    sfzTv.setText(item.getSFZ());

                    Button detailsBtn=holder.getView(R.id.btn_item_follow_details);//查看详情
                    detailsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //获取个人信息
                            getPinfo(position);
                        }
                    });

                    Button cancelBtn=holder.getView(R.id.btn_item_follow_follow);//取消关注
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noFollowDialog(position);
                        }
                    });

                    LinearLayout ll = holder.getView(R.id.item_my_follow_ll);
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


    private void noFollowDialog(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定要取消关注吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                noFollow(position);
                            }
                        }
                ).start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    //取消关注
    private void noFollow(final int position){


        new Thread(


                new Runnable() {
                    @Override
                    public void run() {


                        //  http://web.youli.pw:8088/Json/Set_Attention.aspx?sfz=000000196012110010&name=王建成&type=1
                        String    url=MyOkHttpUtils.BaseUrl+"/Json/Set_Attention.aspx?sfz="+data.get(position).getSFZ()+"&name="+data.get(position).getNAME()+"&type=1";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String followStr=response.body().string();

                                msg.obj=followStr;
                                msg.what=SUCCEED_FOLLOW;
                                msg.arg1=position;

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



    //获取个人信息
    private void getPinfo(final int position){


        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                            //  http://web.youli.pw:8088/Json/Get_BASIC_INFORMATION.aspx?sfz=110102196901201936
                        String    url=MyOkHttpUtils.BaseUrl+"/Json/Get_BASIC_INFORMATION.aspx?sfz="+data.get(position).getSFZ();

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String pinfoStr=response.body().string();

                                if(!TextUtils.equals(pinfoStr,"[]")&&!TextUtils.equals(pinfoStr,"[null]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(pinfoStr,new TypeToken<List<PersonListInfo>>(){}.getType());
                                    msg.what=SUCCEED_PINFO;
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
}
