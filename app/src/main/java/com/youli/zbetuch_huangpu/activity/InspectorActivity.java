package com.youli.zbetuch_huangpu.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.youli.zbetuch_huangpu.adapter.SimpleTreeListViewAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.InspectorInfo;
import com.youli.zbetuch_huangpu.entity.MeetInfo;
import com.youli.zbetuch_huangpu.entity.MeetPersonInfo;
import com.youli.zbetuch_huangpu.entity.ZhiwuInfo;
import com.youli.zbetuch_huangpu.naire.WenJuanDetailActivity;
import com.youli.zbetuch_huangpu.treeview.Node;
import com.youli.zbetuch_huangpu.treeview.TreeListViewAdapter;
import com.youli.zbetuch_huangpu.utils.AlertDialogUtils;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/12/2.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 督查督办界面
 */

public class InspectorActivity extends BaseActivity{


    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时
    private final int SUCCEED_ZHIWU=10005;//职务信息

    private Context mContext=this;

    private AlertDialogUtils adu;


    private TextView personTv;

    private PullToRefreshListView lv;
    private List<InspectorInfo> data=new ArrayList<>();
    private List<ZhiwuInfo> zhiWuData=new ArrayList<>();
    private SimpleTreeListViewAdapter<ZhiwuInfo> zhiwuAdapter;
    private CommonAdapter adapter;

    private Button newBtn;

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
                    data.addAll((List<InspectorInfo>)msg.obj);
                    LvSetAdapter(data);
                    getZhiwuDatas();

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

                    Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(i);


                    break;

                case SUCCEED_ZHIWU:

                    zhiWuData.addAll((List<ZhiwuInfo>)msg.obj);

                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector);

        initViews();

    }

    private void initViews(){

        newBtn= (Button) findViewById(R.id.inspector_new_btn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showNewOrModifyDialog("new",-1);
            }
        });

        lv= (PullToRefreshListView) findViewById(R.id.lv_inspector);
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(mContext,InspectorDetailActivity.class);
                i.putExtra("InInfo",data.get(position-1));
                startActivity(i);
            }
        });

        //2017-12-8 功能暂时不要了
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                showNewOrModifyDialog("modify",position);
//                return true;
//            }
//        });





    }


    private void initDatas(final int pIndex){

        //http://web.youli.pw:8088/Json/First/Get_ManageWork.aspx?page=0&rows=20
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/First/Get_ManageWork.aspx?page="+pIndex+"&rows=20";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<InspectorInfo>>(){}.getType());
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

    private void LvSetAdapter(List<InspectorInfo> list){


        if(adapter==null){

            adapter=new CommonAdapter<InspectorInfo>(mContext,list,R.layout.item_inspector_lv) {
                @Override
                public void convert(CommonViewHolder holder, InspectorInfo item, int position) {

                    TextView noTv=holder.getView(R.id.item_inspector_number_tv);//编号
                    noTv.setText((position+1)+"");
                    TextView nameTv=holder.getView(R.id.item_inspector_name_tv);//工作名称
                    nameTv.setText(item.getTitle());
                    TextView createTimeTv=holder.getView(R.id.item_inspector_create_time_tv);//创建时间
                    createTimeTv.setText(MyDateUtils.stringToYMD(item.getCreate_date()));
                    TextView comTimeTv=holder.getView(R.id.item_inspector_com_time_tv);//完成时间
                    comTimeTv.setText(MyDateUtils.stringToYMD(item.getNotice_time()));
                    TextView stateTv=holder.getView(R.id.item_inspector_state_tv);//完成状态
                    stateTv.setText(item.getType());

                    LinearLayout ll = holder.getView(R.id.item_inspector_ll);
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


    private void getZhiwuDatas(){//获取职务数据

        //http://web.youli.pw:8088/Json/Get_Line_level.aspx?window=员工待办事宜
        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_Line_level.aspx?window=员工待办事宜";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String meetStr=response.body().string();

                                if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

                                    Gson gson=new Gson();
                                    msg.obj=gson.fromJson(meetStr,new TypeToken<List<ZhiwuInfo>>(){}.getType());
                                    msg.what=SUCCEED_ZHIWU;
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

    //新建和修改的对话框
    private void showNewOrModifyDialog(String mark,int position){

        adu=new AlertDialogUtils(this,R.layout.dialog_inspector);

        adu.showAlertDialog();

        TextView titleTv= (TextView) adu.getAduView().findViewById(R.id.dialog_inspector_title_tv);//标题
        final EditText nameEt= (EditText) adu.getAduView().findViewById(R.id.dialog_inspector_name_et);//工作名称
        final TextView timeTv= (TextView) adu.getAduView().findViewById(R.id.dialog_inspector_time_tv);//完成时间
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c=Calendar.getInstance();
                new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                timeTv.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                            }
                        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

            }

        });
        personTv = (TextView) adu.getAduView().findViewById(R.id.dialog_inspector_person_tv);//负责人员
        personTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChoosePerson();//选择负责人员

            }
        });
       final EditText contentEt= (EditText) adu.getAduView().findViewById(R.id.dialog_inspector_content_et);//工作内容

        if(TextUtils.equals("new",mark)){
            titleTv.setText("新建待办工作");
        }else if(TextUtils.equals("modify",mark)){
            titleTv.setText("修改待办工作");
            nameEt.setText(data.get(position-1).getTitle());
            timeTv.setText(MyDateUtils.stringToYMD(data.get(position-1).getNotice_time()));
        }

        Button sureBtn= (Button) adu.getAduView().findViewById(R.id.dialog_inspector_ensure_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//确定

                String nameStr=nameEt.getText().toString().trim();//工作名称
                String timeStr=timeTv.getText().toString().trim();//完成时间
                String personStr=personTv.getText().toString().trim();//负责人员
                String contentStr=contentEt.getText().toString().trim();//工作内容

                Log.e("2017/12/5","工作名称=="+nameStr);
                Log.e("2017/12/5","完成时间=="+timeStr);
                Log.e("2017/12/5","负责人员=="+personStr);
                Log.e("2017/12/5","工作内容=="+contentStr);
                adu.dismissAlertDialog();
            }
        });

        Button cancelBtn= (Button) adu.getAduView().findViewById(R.id.dialog_inspector_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消

                adu.dismissAlertDialog();
            }
        });

    }

    private void showChoosePerson() {//选择负责人员

        final AlertDialogUtils  personAdu=new AlertDialogUtils(this,R.layout.dialog_inspector_person);

        personAdu.showAlertDialog();
        final List<MeetPersonInfo>  personData=new ArrayList<>();//会议人员的集合
        CommonAdapter pAdapter=null;

        ListView presonLv= (ListView) personAdu.getAduView().findViewById(R.id.dialog_inspector_person_lv);//会议人员列表

        pAdapter=new CommonAdapter<MeetPersonInfo>(mContext,personData,R.layout.item_inspector_person) {
            @Override
            public void convert(CommonViewHolder holder, MeetPersonInfo item, final int position) {

                TextView tvName=holder.getView(R.id.item_inspector_person_text);
                tvName.setText(item.getName());
                ImageView iv=holder.getView(R.id.item_inspector_person_iv);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        personData.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        };

        presonLv.setAdapter(pAdapter);

        ListView zhiwuLv= (ListView) personAdu.getAduView().findViewById(R.id.dialog_inspector_zhiwu_lv);//职务列表

        try {
            zhiwuAdapter = new SimpleTreeListViewAdapter<ZhiwuInfo>(zhiwuLv, mContext, zhiWuData, 0,personData,pAdapter);
            zhiwuLv.setAdapter(zhiwuAdapter);


        }catch (IllegalAccessException e){

        }



        Button sureBtn= (Button) personAdu.getAduView().findViewById(R.id.dialog_inspector_person_ensure_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//确定

                String personStr = "";

                for(MeetPersonInfo pInfo:personData){
                    personStr+=pInfo.getName()+",";
                }

                personStr=personStr.substring(0,personStr.length()-1);

                personTv.setText(personStr);

                personAdu.dismissAlertDialog();
            }
        });

        Button cancelBtn= (Button)personAdu.getAduView().findViewById(R.id.dialog_inspector_person_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消

                personAdu.dismissAlertDialog();
            }
        });
    }

}
