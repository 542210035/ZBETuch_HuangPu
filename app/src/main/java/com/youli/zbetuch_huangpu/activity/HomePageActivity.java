package com.youli.zbetuch_huangpu.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.AdminInfo;
import com.youli.zbetuch_huangpu.entity.MyFollowInfo;
import com.youli.zbetuch_huangpu.utils.IOUtil;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;
import com.youli.zbetuch_huangpu.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Response;


//首页
public class HomePageActivity extends CheckPermissionsActivity implements View.OnClickListener{

    private Context mContext=this;


    private final int SUCCESS_ADMIN_INFO=10001;
    private final int PROBLEM=10002;
    private final int OVERTIME=10003;//登录超时
    private final int SUCCESS_TZGG_NUM=10004;//通知公告的数量
    private final int SUCCESS_HYGL_NUM=10005;//会议管理的数量
    private final int SUCCESS_DBGZ_NUM=10006;//待办工作的数量
    private final int SUCCESS_DCDB_NUM=10007;//督察督办的数量
    private final int SUCCESS_WDGZ_NUM=10008;//我的关注的数量
    private final int SUCCESS_ADMIN_PIC=10009;//头像
    private final int SUCCESS_YZY=10010;//援助员

    private List<MyFollowInfo> followData=new ArrayList<>();//我的关注的数据

    public static AdminInfo adminInfo;


    private Intent intent;
    private ImageView notice;
    private CircleImageView ivHead;//头像
    public static String adminName;//调查人姓名
    private Button workBtn;

    private ImageView meetManageIv;//会议管理
    private ImageView needWorkIv;//待办工作
    private ImageView inspectorIv;//督察督办
    private ImageView myFollowIv;//我的关注
    private ImageView zydcIv;//资源调查
    private ImageView zyjsIv;//职业介绍
    private TextView tvJdu,tvWdu,tvGdu;//经度，纬度，高度

    private PullToRefreshScrollView psv;
    private RelativeLayout inspectorRl;
    private TextView titieTv,wdgzNumTv;//我的关注的数量
    private TextView tzggNumTv;//通知公告的数量
    private TextView hyglNumTv;//会议管理的数量
    private TextView dbgzNumTv;//待办工作的数量
    private TextView dcdbNumTv;//督察督办的数量
    private ImageView  im_mail;//邮件管理

    private LocationManager locationManager;
    private String provider;

    private Handler gpsHandler;//用来检测gps是否打开

    private int getGPSTime=500;//获取GPS经纬度的时间间隔，默认是500ms

    public  static String jDuStr,wDuStr;//要上传的经度和纬度

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCESS_ADMIN_INFO:

                    adminInfo=(AdminInfo)msg.obj;
                    adminName=adminInfo.getNAME();


                    if(adminInfo.isSTOP()){//判断账号是否被启用
                        gpsHandler.removeCallbacks(rState);
                        Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                        i.putExtra("type","state");
                        startActivity(i);
                        return;
                    }

//                    if(!getImei(adminInfo.getIMEI())){//获取IMEI号
//                        gpsHandler.removeCallbacks(rState);
//                        return;
//                    }

                    getPic();//获取头像
                    getNum("WDGZ");//我的关注

                    break;

                case PROBLEM:
                    if(psv.isRefreshing()) {
                        psv.onRefreshComplete();//停止刷新或加载更多
                    }
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;
                case OVERTIME:
                    if(psv.isRefreshing()) {
                        psv.onRefreshComplete();//停止刷新或加载更多
                    }
                    Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(i);

                    break;

                case SUCCESS_WDGZ_NUM://我的关注的数量

                    followData=((List<MyFollowInfo>)(msg.obj));

                    if(followData.size()!=0) {
                        wdgzNumTv.setVisibility(View.VISIBLE);
                        wdgzNumTv.setText(followData.get(0).getRecordCount()+"");
                    }else{
                        wdgzNumTv.setVisibility(View.GONE);
                    }
                    getNum("TZGG");//通知公告

                    break;

                case SUCCESS_TZGG_NUM://通知公告的数量

                    if(!TextUtils.equals("0",""+msg.obj)) {
                        tzggNumTv.setText("" + msg.obj);
                        tzggNumTv.setVisibility(View.VISIBLE);
                    }else{
                        tzggNumTv.setVisibility(View.GONE);
                    }
                    getNum("HYGL");//会议管理

                    break;

                case SUCCESS_HYGL_NUM://会议管理的数量
                    if(!TextUtils.equals("0",""+msg.obj)) {
                        hyglNumTv.setText("" + msg.obj);
                        hyglNumTv.setVisibility(View.VISIBLE);
                    }else {
                        hyglNumTv.setVisibility(View.GONE);
                    }
                    getNum("DBGZ");//待办工作
                    break;

                case SUCCESS_DBGZ_NUM://待办工作的数量
                    if(!TextUtils.equals("0",""+msg.obj)) {
                        dbgzNumTv.setText("" + msg.obj);
                        dbgzNumTv.setVisibility(View.VISIBLE);
                    }else{
                        dbgzNumTv.setVisibility(View.GONE);
                    }
                    getNum("DCDB");//督察督办
                    break;

                case SUCCESS_DCDB_NUM://督察督办的数量

                    if(psv.isRefreshing()) {
                        psv.onRefreshComplete();//停止刷新或加载更多
                    }

                    if(!TextUtils.equals("0",""+msg.obj)) {
                        dcdbNumTv.setText("" + msg.obj);
                        dcdbNumTv.setVisibility(View.VISIBLE);
                    }else{
                        dcdbNumTv.setVisibility(View.GONE);
                    }

                    break;

                case SUCCESS_ADMIN_PIC://头像

                    ivHead.setImageBitmap((Bitmap) msg.obj);

                    break;

                case SUCCESS_YZY://援助员

                    if(TextUtils.equals(msg.obj+"","True")){
                    inspectorRl.setVisibility(View.GONE);
                    }else if(TextUtils.equals(msg.obj+"","False")){
                        inspectorRl.setVisibility(View.VISIBLE);
                    }


                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //注册事件
        EventBus.getDefault().register(this);

        markStr="HomePageActivity";//通知权限的标记， 这句一定不能少

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gpsHandler=new Handler();
        gpsHandler.post(r);
        gpsHandler.post(rGps);
        gpsHandler.post(rState);
        initViews();

    }


    private void initViews(){
        im_mail= (ImageView) findViewById(R.id.homepage_yjgl_iv); //邮件
        im_mail.setOnClickListener(this);
        titieTv= (TextView) findViewById(R.id.main_layout_title_tv);
        //给标题设置字体
        Typeface type=Typeface.createFromAsset(getApplicationContext().getAssets(),"STXINGKA.TTF");
        titieTv.setTypeface(type);

        notice= (ImageView) findViewById(R.id.notice);
        notice.setOnClickListener(this);
        ivHead= (CircleImageView) findViewById(R.id.iv_activity_homepage_head);
        ivHead.setOnClickListener(this);
        meetManageIv= (ImageView) findViewById(R.id.homepage_meet_manage_iv);
        meetManageIv.setOnClickListener(this);
        needWorkIv= (ImageView) findViewById(R.id.homepage_need_work_iv);
        needWorkIv.setOnClickListener(this);
        inspectorIv= (ImageView) findViewById(R.id.homepage_inspector_iv);
        inspectorIv.setOnClickListener(this);
        myFollowIv= (ImageView) findViewById(R.id.homepage_my_follow_iv);
        myFollowIv.setOnClickListener(this);
        zydcIv= (ImageView) findViewById(R.id.homepage_zydc_iv);
        zydcIv.setOnClickListener(this);
        zyjsIv= (ImageView) findViewById(R.id.homepage_my_follow_zyjs);
        zyjsIv.setOnClickListener(this);
        workBtn = (Button) findViewById(R.id.main_layout_work_btn);
        workBtn.setOnClickListener(this);
        tvJdu= (TextView) findViewById(R.id.main_layout_tv_jdu);
        tvWdu= (TextView) findViewById(R.id.main_layout_tv_wdu);
        tvGdu= (TextView) findViewById(R.id.main_layout_tv_gdu);

        wdgzNumTv= (TextView) findViewById(R.id.wdgz_num_tv);
        tzggNumTv= (TextView) findViewById(R.id.tzgg_num_tv);
        hyglNumTv= (TextView) findViewById(R.id.hygl_num_tv);
        dbgzNumTv= (TextView) findViewById(R.id.dbgz_num_tv);
        dcdbNumTv= (TextView) findViewById(R.id.dcdb_num_tv);
        inspectorRl= (RelativeLayout) findViewById(R.id.homepage_inspector_rl);
        //getAdminInfo();

        getYzy();//获取是否援助员


        psv= (PullToRefreshScrollView) findViewById(R.id.psv_homepage);
        psv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

                getNum("WDGZ");//我的关注

            }
        });
    }

    private void getYzy(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                    //http://web.youli.pw:8088/Json/Get_Check_YZY.aspx
                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_Check_YZY.aspx";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String str=response.body().string();

                                if(!TextUtils.equals(str,"")){
                                    msg.obj=str;
                                    msg.what=SUCCESS_YZY;
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

    //获取管理员的信息
    //http://web.youli.pw:8088/Json/Get_Staff.aspx
    private void getAdminInfo(){

        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_Staff.aspx";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            if(response.body()!=null){

                                try {
                                    String resStr=response.body().string();

                                    if(!TextUtils.equals(resStr,"")){

                                        Gson gson=new Gson();

                                        try{
                                            msg.obj=gson.fromJson(resStr,AdminInfo.class);

                                            msg.what=SUCCESS_ADMIN_INFO;
                                        }catch(Exception e){
                                            msg.what=OVERTIME;

                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                msg.what=PROBLEM;
                            }
                        }else{
                            msg.what=PROBLEM;
                        }
                        mHandler.sendMessage(msg);
                    }
                }
        ).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消注册事件
        EventBus.getDefault().unregister(this);



        if (locationManager != null) {
            //关闭程序时将监听器移除

            locationManager.removeUpdates(locationListener);
        }
    }

    //这里我们的ThreadMode设置为MAIN，事件的处理会在UI线程中执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNum(final String mark){//获取通知公告，会议管理，待办工作，督察督办的数量
        //我的关注的数量:
       // http://web.youli.pw:8088/Json/First/Get_Attention.aspx?page=0&rows=1
        //通知公告的数量:
        //http://web.youli.pw:8088/Json/First/Get_News_Count.aspx
        //会议管理的数量：
        //http://web.youli.pw:8088/Json/First/Get_Meeting_Check.aspx
        //待办工作：
        //http://web.youli.pw:8088/Json/First/Get_Work_Notice_Check.aspx
        //督察督办
        //http://web.youli.pw:8088/Json/First/Get_Work_Notice_Done.aspx
        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = null;

                if(TextUtils.equals(mark,"TZGG")) {

                    url = MyOkHttpUtils.BaseUrl + "/Json/First/Get_News_Count.aspx";
                }else if(TextUtils.equals(mark,"HYGL")){
                    url = MyOkHttpUtils.BaseUrl + "/Json/First/Get_Meeting_Check.aspx";
                }else if(TextUtils.equals(mark,"DBGZ")){
                    url = MyOkHttpUtils.BaseUrl + "/Json/First/Get_Work_Notice_Check.aspx";
                }else if(TextUtils.equals(mark,"DCDB")){
                    url = MyOkHttpUtils.BaseUrl + "/Json/First/Get_Work_Notice_Done.aspx";
                }else if(TextUtils.equals(mark,"WDGZ")){
                    url = MyOkHttpUtils.BaseUrl + "/Json/First/Get_Attention.aspx?page=0&rows=1";
                }



                Response response=MyOkHttpUtils.okHttpGet(url);

                Message msg=Message.obtain();

                if(response!=null){

                    if(response.body()!=null){

                        try {
                            String resStr=response.body().string();

                            if(!TextUtils.equals(resStr,"")){

                                try{


                                    if(TextUtils.equals(mark,"WDGZ")){
                                        Gson gson=new Gson();
                                        msg.obj=gson.fromJson(resStr,new TypeToken<List<MyFollowInfo>>(){}.getType());
                                        msg.what = SUCCESS_WDGZ_NUM;
                                    }else {
                                        msg.obj=resStr;
                                        if (TextUtils.equals(mark, "TZGG")) {
                                            msg.what = SUCCESS_TZGG_NUM;
                                        } else if (TextUtils.equals(mark, "HYGL")) {
                                            msg.what = SUCCESS_HYGL_NUM;
                                        } else if (TextUtils.equals(mark, "DBGZ")) {
                                            msg.what = SUCCESS_DBGZ_NUM;
                                        } else if (TextUtils.equals(mark, "DCDB")) {
                                            msg.what = SUCCESS_DCDB_NUM;
                                        }
                                    }
                                }catch(Exception e){
                                    msg.what=OVERTIME;

                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{
                        msg.what=PROBLEM;
                    }

                }else{
                    msg.what=PROBLEM;

                }
                mHandler.sendMessage(msg);
            }


        }).start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_activity_homepage_head:
                if(adminInfo!=null) {
                    intent = new Intent(this, AdminInfoActivity.class);
                    intent.putExtra("ADMININFO", adminInfo);
                    startActivity(intent);
                }
                break;

            //九宫格的Button按钮点击
            case R.id.main_layout_work_btn:
                intent=new Intent(this,FunctionListActivity.class);
                startActivity(intent);
                break;

            //通知公告
            case R.id.notice:
                intent=new Intent(this,NoticeBulletin.class);
                startActivity(intent);
                break;

            case R.id.homepage_meet_manage_iv://会议管理
                intent = new Intent(this,MeetManageActivity.class);
                startActivity(intent);
                break;
            case R.id.homepage_need_work_iv://待办工作
                intent = new Intent(this,NeedWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.homepage_inspector_iv://督察督办
                intent = new Intent(this,InspectorActivity.class);
                startActivity(intent);
                break;
            case R.id.homepage_my_follow_iv://我的关注
                intent = new Intent(this,MyFollowActivity.class);
                startActivity(intent);
                break;

            case R.id.homepage_zydc_iv://资源调查
                intent=new Intent(this,FunctionListActivity.class);
                startActivity(intent);
                break;

            case R.id.homepage_my_follow_zyjs://职业介绍
                intent=new Intent(this,OccupationActivity.class);
                startActivity(intent);
                break;

            case R.id.homepage_yjgl_iv://邮件管理
                intent=new Intent(this,Mail_Activity.class);
                startActivity(intent);
                break;
        }

    }


    private void getAddress(){

        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
//            Toast.makeText(MainActivity.this, "no Location provider to use",
//                    Toast.LENGTH_SHORT).show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            getGPSTime=1000*60*5;//5分钟
            //显示位置
            showLocations(location);

        }else{

            getGPSTime=500;//500ms

            String spJdu=SharedPreferencesUtils.getString("jDu");//sp取经度
            String spWdu=SharedPreferencesUtils.getString("wDu");//sp取纬度


            Log.e("2018-1-3111","经度="+spJdu);
            Log.e("2018-1-3111","纬度="+spWdu);

            if(!TextUtils.equals(spJdu,"")&&!TextUtils.equals(spWdu,"")) {

                tvJdu.setText("经度:" + (new Double(Double.parseDouble(spJdu))).intValue());//String先转Double，再转int
                tvWdu.setText("纬度:" + (new Double(Double.parseDouble(spWdu))).intValue());//String先转Double，再转int
                tvJdu.setText("经度:" + spJdu);//String先转Double，再转int
                tvWdu.setText("纬度:" + spWdu);//String先转Double，再转int
            }
            jDuStr=spJdu;//要上传的经度
            wDuStr=spWdu;//要上传的纬度

        }
        locationManager.requestLocationUpdates(provider, getGPSTime, 0, locationListener);
        //绑定监听状态
        locationManager.addGpsStatusListener(listener);//可以获取卫星的数量
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //更新当前位置
            showLocations(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocations(Location location) {

        tvJdu.setText("经度:"+location.getLongitude());
        tvWdu.setText("纬度:"+location.getLatitude());

        tvGdu.setText("高度:"+location.getAltitude()+"米");


        jDuStr=location.getLongitude()+"";//要上传的经度
        wDuStr=location.getLatitude()+"";//要上传的纬度

        Log.e("2018-1-3","经度="+location.getLongitude());
        Log.e("2018-1-3","纬度="+location.getLatitude());

        SharedPreferencesUtils.putString("jDu",String.valueOf(location.getLongitude()));//sp存经度
        SharedPreferencesUtils.putString("wDu",String.valueOf(location.getLatitude()));//sp存纬度
    }


    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("您确定退出吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //移除Handler
                gpsHandler.removeCallbacks(r);
                gpsHandler.removeCallbacks(rGps);
                gpsHandler.removeCallbacks(rState);
                ActivityController.finishAll();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    private  boolean isOPen(final Context context) {

        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps) {
            return true;
        }

        return false;
    }


    Runnable rState=new Runnable() {
        @Override
        public void run() {
            gpsHandler.postDelayed(this,5*60*1000);//5分钟检测一次状态
            getAdminInfo();
        }
    };

    Runnable r=new Runnable() {
        @Override
        public void run() {
            gpsHandler.postDelayed(this,2000);//2秒钟检测一次gps
            if(isOPen(getApplicationContext())){
              //  Toast.makeText(getApplicationContext(),"GPS可用",Toast.LENGTH_SHORT).show();
            }else{
              //  Toast.makeText(getApplicationContext(),"GPS不可用",Toast.LENGTH_SHORT).show();
                //移除Handler
                gpsHandler.removeCallbacks(r);
                Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                i.putExtra("type","gps");
                startActivity(i);

            }
        }
    };

    Runnable rGps=new Runnable() {
        @Override
        public void run() {


            gpsHandler.postDelayed(this,getGPSTime);//刷新gps

            getAddress();

        }
    };

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                  //  Log.i("2017-12-17", "第一次定位");
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                  //  Log.i("2017-12-17", "卫星状态改变");
                    //获取当前状态
                    GpsStatus gpsStatus=locationManager.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                   Log.e("2018-1-3", "搜索到："+count+"颗卫星");
                    SharedPreferencesUtils.putString("wx",String.valueOf(count));//卫星
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                   // Log.i("2017-12-17", "定位启动");
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                  //  Log.i("2017-12-17", "定位结束");
                    break;
            }
        };
    };


    private void  getPic(){

       // http://web.youli.pw:8088/Json/GetStaffPic.aspx?staff=1
        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                        String urlPic =MyOkHttpUtils.BaseUrl+"/Json/GetStaffPic.aspx?staff="+adminInfo.getID();
                        Response response = MyOkHttpUtils.okHttpGet(urlPic);
                        try {
                            Message msg = Message.obtain();

                            if (response != null) {
                                InputStream is = response.body().byteStream();

                                byte[] picData = IOUtil.getBytesByStream(is);

                                Bitmap btp = BitmapFactory.decodeByteArray(picData, 0, picData.length);

                                msg.obj = btp;
                                msg.what = SUCCESS_ADMIN_PIC;
                                mHandler.sendMessage(msg);

                            } else {

                               // sendProblemMessage(msg);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).start();

    }

    private boolean getImei(String adminImei){

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();

        if(!TextUtils.equals(adminImei,imei)){
            Intent i=new Intent(mContext,OvertimeDialogActivity.class);
            i.putExtra("type","imei");
            startActivity(i);

            return  false;
        }

        return  true;
    }

}
