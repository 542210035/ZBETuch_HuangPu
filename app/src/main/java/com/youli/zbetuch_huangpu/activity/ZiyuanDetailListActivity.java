package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.ResourcesDetailInfo;
import com.youli.zbetuch_huangpu.entity.ResourcesInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;
import com.youli.zbetuch_huangpu.view.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by sfhan on 2017/11/8.
 */

public class ZiyuanDetailListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener,AdapterView.OnItemClickListener{


    private Context mContext=ZiyuanDetailListActivity.this;
    private TextView typeTv;
    private EditText et;
    private String sfzStr;
    private Button queryBtn;
    private int typeId;//已查，未查的标记
    private int pageIndex;
    private ResourcesInfo rInfo;
    private TextView totalTv,noDataTv;

    private PullToRefreshScrollView hsv;
    private MyListView lv;
    private View header;

    private RadioGroup rg;
    private List<ResourcesDetailInfo> dInfo=new ArrayList<>();
    private CommonAdapter commonAdapter;

    private final int SUCCESS=10000;
    private final int NODATA=10001;
    private final int PROBLEM=10002;
    private final int OVERTIME=10003;//登录超时

    public final static int RequestCode=111111;
    public final static int ResultCode=222222;


    //public ResourcesDetailInfo eventBus;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            ProgressDialogUtils.dismissMyProgressDialog(mContext);

            switch (msg.what){

                case SUCCESS:
                    if(pageIndex==0){
                        dInfo.clear();
                    }

                        dInfo.addAll((List<ResourcesDetailInfo>)msg.obj);

                    if(dInfo.size()>0){
                        noDataTv.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        lvSetAdapter(dInfo);
                    }else {
                        totalTv.setText("总共0条数据");
                        noDataTv.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }


                    break;

                case PROBLEM:

                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();

                    break;
                case NODATA:

                    if (hsv.isRefreshing()) { //isRefreshing boolean类型 判断listview是否在刷新
                        hsv.onRefreshComplete();  //onRefreshComplete如果listview在刷新就停止
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
        setContentView(R.layout.activity_ziyuan_detail);

        //注册事件
        //EventBus.getDefault().register(this);

        initViews();
    }

    private void initViews(){

        rInfo= (ResourcesInfo) getIntent().getSerializableExtra("RInfo");
        et= (EditText) findViewById(R.id.ziyuan_detail_et);
        queryBtn= (Button) findViewById(R.id.ziyuan_detail_query_btn);
        queryBtn.setOnClickListener(this);
        rg= (RadioGroup) findViewById(R.id.ziyuan_detail_rg);
        rg.check(R.id.ziyuan_detail_weicha_rb);
        setTypeId();
        rg.setOnCheckedChangeListener(this);
        typeTv= (TextView) findViewById(R.id.ziyuan_detail_type_tv);
        noDataTv= (TextView) findViewById(R.id.ziyuan_detail_tv_nodata);
        totalTv= (TextView) findViewById(R.id.ziyuan_detail_num_tv);
        if(TextUtils.equals("失业",rInfo.getDclx())){
            typeTv.setText("失业调查");
        }else if(TextUtils.equals("无业",rInfo.getDclx())){
            typeTv.setText("无业调查");
        }else if(TextUtils.equals("应届生",rInfo.getDclx())){
            typeTv.setText("应届生调查");
        }

        hsv= (PullToRefreshScrollView) findViewById(R.id.hsv_job_info);
        hsv.setMode(PullToRefreshBase.Mode.BOTH);


        getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,"null",pageIndex);

        hsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex=0;
                if(sfzStr==null){
                    getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,"null",pageIndex);
                }else{
                    getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,sfzStr,pageIndex);
                }
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex++;
                if(sfzStr==null){
                    getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,"null",pageIndex);
                }else{
                    getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,sfzStr,pageIndex);
                }
            }
        });
        lv= (MyListView) findViewById(R.id.lv_job_info);
        header= LayoutInflater.from(this).inflate(R.layout.ziyuanlist_hear,lv,false);
        lv.addHeaderView(header);
        lv.setOnItemClickListener(this);
    }

    //身份证查询:
   // http://web.youli.pw:8088/Json/Get_Resource_Survey_Detil_SY.aspx?page=0&rows=15&dcId=1&type=1&sfz=
  //  @Subscribe(threadMode = ThreadMode.MAIN)
    private void getNetWorkData(final int dcid, final String dclx, final int typeId, final String sfz, final int page){

        ProgressDialogUtils.showMyProgressDialog(this);

        if(page==0) {
            if (dInfo != null && dInfo.size() > 0 ) {
                dInfo.clear();
            }
        }

        if(!TextUtils.equals(sfz,"null")) {
            dInfo.clear();
        }


        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                        String typeMark = null;

                        if(TextUtils.equals("失业",dclx)){
                            typeMark="SY";
                        }else if(TextUtils.equals("无业",dclx)){
                            typeMark="WY";
                        }else if(TextUtils.equals("应届生",dclx)){
                            typeMark="YJS";
                        }
                        String url = null;
                        if(TextUtils.equals(sfz,"null")) {
                            //http://web.youli.pw:8088/Json/Get_Resource_Survey_Detil_WY.aspx?page=0&rows=15&dcId=2&type=1&sfz=
                            url = MyOkHttpUtils.BaseUrl + "/Json/Get_Resource_Survey_Detil_" + typeMark + ".aspx?page="+page+"&rows=20&dcId="+dcid+ "&type=" + typeId;
                        }else{
                            url = MyOkHttpUtils.BaseUrl + "/Json/Get_Resource_Survey_Detil_" + typeMark + ".aspx?page=0&rows=20&&dcId="+dcid+ "&type=" + typeId+ "&sfz=" + sfz;
                        }

                        Log.e("2017/8/100","url=="+url);

                        Response response= MyOkHttpUtils.okHttpGet(url);

                        try {

                            Message msg=Message.obtain();

                            if(response!=null){

                                String infoStr=response.body().string();
                                Gson gson=new Gson();
                                try{

                                    msg.what=SUCCESS;
                                    msg.obj=gson.fromJson(infoStr,
                                            new TypeToken<List<ResourcesDetailInfo>>(){}.getType());
                                    mHandler.sendMessage(msg);
                                }catch(Exception e){
                                    Log.e("2017/11/13","登录超时了");
                                    msg.what=OVERTIME;
                                    mHandler.sendMessage(msg);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        pageIndex=0;
        switch (checkedId){
            case R.id.ziyuan_detail_weicha_rb:
                rg.check(R.id.ziyuan_detail_weicha_rb);
                setTypeId();
                sfzStr="null";
                getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,"null",pageIndex);
                break;
            case R.id.ziyuan_detail_yicha_rb:
                rg.check(R.id.ziyuan_detail_yicha_rb);
                setTypeId();
                sfzStr="null";
                getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,"null",pageIndex);
                break;
        }

    }




    private void lvSetAdapter(final List<ResourcesDetailInfo> data){

        if(commonAdapter==null) {

            commonAdapter = new CommonAdapter<ResourcesDetailInfo>(this, dInfo, R.layout.item_ziyuan_detail_lv) {
                @Override
                public void convert(CommonViewHolder holder, ResourcesDetailInfo item, int position) {

                    TextView numTv = holder.getView(R.id.item_ziyuan_detail_num_tv);
                    numTv.setText(position + 1 + "");
                    TextView nameTv = holder.getView(R.id.item_ziyuan_detail_name_tv);
                    nameTv.setText(data.get(position).getXM());
                    TextView idCardTv = holder.getView(R.id.item_ziyuan_detail_idcard_tv);
                    idCardTv.setText(data.get(position).getZJHM());
                    TextView juWeiTv = holder.getView(R.id.item_ziyuan_detail_juwei_tv);
                    juWeiTv.setText(data.get(position).getHJJWMC());
                    TextView hkdzTv = holder.getView(R.id.item_ziyuan_detail_hkdz_tv);
                    hkdzTv.setText(data.get(position).getHKDZ());
                    TextView jiezhi_tv=holder.getView(R.id.item_ziyuan_detail_jiezhiriqi_tv);
                    jiezhi_tv.setText(MyDateUtils.stringToYMD(data.get(position).getJJ_DATE()));


                    LinearLayout ll = holder.getView(R.id.item_ziyuan_detail_ll);

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
        if (hsv.isRefreshing()) {
            hsv.onRefreshComplete();
        }
        totalTv.setText("总共" + data.get(0).getRecordCount() + "条数据");
    }

    private void setTypeId(){

        if(rg.getCheckedRadioButtonId()==R.id.ziyuan_detail_weicha_rb){
            typeId=1;
        }else if(rg.getCheckedRadioButtonId()==R.id.ziyuan_detail_yicha_rb){
            typeId=0;
        }

    }

    private void sendProblemMessage(Message msg) {
        msg.what = PROBLEM;
        mHandler.sendMessage(msg);
    }


    @Override
    public void onClick(View v) {

        sfzStr=et.getText().toString().trim();

        switch (v.getId()){

            case R.id.ziyuan_detail_query_btn:

                if(sfzStr.length()==0) {

                    Toast.makeText(mContext, "身份证不能为空", Toast.LENGTH_SHORT).show();

                }else if(sfzStr.length()<18){
                    Toast.makeText(mContext, "请输入18位身份证号",
                            Toast.LENGTH_LONG).show();
                }else{



                    getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,sfzStr,pageIndex);
                }

                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        boolean isCheck = false;

        if(rg.getCheckedRadioButtonId()==R.id.ziyuan_detail_weicha_rb){
            isCheck=false;
        }else if(rg.getCheckedRadioButtonId()==R.id.ziyuan_detail_yicha_rb){
            isCheck=true;
        }

        Intent intent=new Intent(this,ShiwuyeDetailActivity.class);
        intent.putExtra("RDInfo",dInfo.get(position-1));
        intent.putExtra("IsCheck",isCheck);
        intent.putExtra("mark",rInfo.getDclx());
       startActivityForResult(intent,RequestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==RequestCode&&resultCode==ResultCode){

            // Toast.makeText(mContext,"回传",Toast.LENGTH_SHORT).show();
            if(sfzStr==null){
                getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,"null",0);
            }else{
                getNetWorkData(rInfo.getDcid(),rInfo.getDclx(),typeId,sfzStr,0);
            }
        }
}

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //取消注册事件
//        EventBus.getDefault().unregister(this);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        		if(keyCode == KeyEvent.KEYCODE_BACK){
                    setResult(ZiyuandiaochaActivity.ResultCode,null);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
    }
}
