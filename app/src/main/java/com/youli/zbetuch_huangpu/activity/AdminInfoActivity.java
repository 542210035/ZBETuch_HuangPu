package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.AdminInfo;
import com.youli.zbetuch_huangpu.entity.MyFollowInfo;
import com.youli.zbetuch_huangpu.utils.IOUtil;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;
import com.youli.zbetuch_huangpu.view.CircleImageView;

import java.io.InputStream;
import java.util.List;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/9/22.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 */

public class AdminInfoActivity extends BaseActivity implements View.OnClickListener{


    private Context mContext=this;


    private final int SUCCESS_ADMIN_PIC=10001;//头像
    private final int PROBLEM=10002;
    private final int OVERTIME=10003;//登录超时


    private ImageView ivBack;
    private CircleImageView ivHead;//头像
    private TextView tvOperatorNo;//操作员工号
    private TextView tvName;//姓名
    private TextView tvPhone;//联系电话
    private TextView tvEmail;//电子邮箱
    private TextView tvDeviceNo;//设备号
    private TextView tvState;//状态
    private TextView tvSfz;//身份证
    private TextView tvDepart;//所属部门
    private TextView tvStreet;//街道
    private TextView tvIMEI;//IMEI
    private TextView tvJdu;//经度
    private TextView tvWdu;//纬度
    private TextView tvWx;//卫星数量
    private RelativeLayout  password_xg;

    private AdminInfo aInfo;

    private byte [] picByte;

    private Handler gpsHandler;//

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){



                case PROBLEM:
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;
                case OVERTIME:

                    Intent i=new Intent(mContext,OvertimeDialogActivity.class);
                    startActivity(i);

                    break;


                case SUCCESS_ADMIN_PIC://头像

                    ivHead.setImageBitmap((Bitmap) msg.obj);

                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);

        aInfo=(AdminInfo)getIntent().getSerializableExtra("ADMININFO");

        initViews();
        getPic();//获取头像
        gpsHandler=new Handler();
        gpsHandler.post(r);
    }

    private void initViews(){
        password_xg= (RelativeLayout) findViewById(R.id.rela_mmxg);
        password_xg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(mContext,ModifyPassword.class);//修改密码
                startActivity(intent);
            }
        });
        ivBack= (ImageView) findViewById(R.id.iv_admin_info_back);
        ivBack.setOnClickListener(this);
        ivHead= (CircleImageView) findViewById(R.id.iv_admin_info_head);
        ivHead.setOnClickListener(this);
        tvOperatorNo= (TextView) findViewById(R.id.tv_admin_info_operator_no);
        tvOperatorNo.setText(aInfo.getINPUT_CODE());
        tvName= (TextView) findViewById(R.id.tv_admin_info_name);
        tvName.setText(aInfo.getNAME());
        tvPhone= (TextView) findViewById(R.id.tv_admin_info_phone);
        tvPhone.setText(aInfo.getPHONE());
        tvEmail= (TextView) findViewById(R.id.tv_admin_info_email);
        tvEmail.setText(aInfo.getEMAIL());

        tvDeviceNo= (TextView) findViewById(R.id.tv_admin_info_device_no);
        tvDeviceNo.setText(aInfo.getDEVICE_NUMBER());
        tvState= (TextView) findViewById(R.id.tv_admin_info_state);

        if(!aInfo.isSTOP()){
            tvState.setText("启用");
        }else{
            tvState.setText("停用");
        }

        tvSfz= (TextView) findViewById(R.id.tv_admin_info_sfz);
        tvSfz.setText(aInfo.getSFZ());
        tvDepart= (TextView) findViewById(R.id.tv_admin_info_department);
        tvDepart.setText(aInfo.getDEPT());
        tvStreet= (TextView) findViewById(R.id.tv_admin_info_street);
        tvStreet.setText(aInfo.getJD());
        tvIMEI= (TextView) findViewById(R.id.tv_admin_info_imei);
        tvIMEI.setText(aInfo.getIMEI());

        tvJdu= (TextView) findViewById(R.id.tv_admin_info_jdu);
        tvJdu.setText("经度:    ");
        tvWdu= (TextView) findViewById(R.id.tv_admin_info_wdu);
        tvWdu.setText("纬度:    ");
        tvWx= (TextView) findViewById(R.id.tv_admin_info_wx);
        //tvWx.setText("卫星:    ");


        tvWx.setText("卫星:"+SharedPreferencesUtils.getString("wx")+"颗");
        if(HomePageActivity.jDuStr.contains(".")&&HomePageActivity.wDuStr.contains(".")) {

            tvJdu.setText("经度:" + HomePageActivity.jDuStr.substring(0, HomePageActivity.jDuStr.indexOf(".")));//截取小数点前面的
            tvWdu.setText("纬度:" + HomePageActivity.wDuStr.substring(0, HomePageActivity.wDuStr.indexOf(".")));
            tvJdu.setText("经度:" + HomePageActivity.jDuStr);
            tvWdu.setText("纬度:" + HomePageActivity.wDuStr);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_admin_info_back:
                //移除Handler
                gpsHandler.removeCallbacks(r);
                finish();

                break;


            case R.id.iv_admin_info_head:

                Intent i=new Intent(mContext,AdminPicActivity.class);
                i.putExtra("pic",picByte);
                startActivity(i);

                break;

        }

    }

    Runnable r=new Runnable() {
        @Override
        public void run() {

            gpsHandler.postDelayed(r,5000);

            if(HomePageActivity.jDuStr.contains(".")&&HomePageActivity.wDuStr.contains(".")) {

                tvJdu.setText("经度:" + HomePageActivity.jDuStr.substring(0, HomePageActivity.jDuStr.indexOf(".")));//截取小数点前面的
                tvWdu.setText("纬度:" + HomePageActivity.wDuStr.substring(0, HomePageActivity.wDuStr.indexOf(".")));
                tvJdu.setText("经度:" + HomePageActivity.jDuStr);//截取小数点前面的
                tvWdu.setText("纬度:" + HomePageActivity.wDuStr);
            }

            tvWx.setText("卫星:"+SharedPreferencesUtils.getString("wx")+"颗");
        }
    };

    private void  getPic(){

        // http://web.youli.pw:8088/Json/GetStaffPic.aspx?staff=1
        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                        String urlPic = MyOkHttpUtils.BaseUrl+"/Json/GetStaffPic.aspx?staff="+aInfo.getID();
                        Response response = MyOkHttpUtils.okHttpGet(urlPic);
                        try {
                            Message msg = Message.obtain();

                            if (response != null) {
                                InputStream is = response.body().byteStream();

                                byte[] picData = IOUtil.getBytesByStream(is);
                                picByte=picData;
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

}
