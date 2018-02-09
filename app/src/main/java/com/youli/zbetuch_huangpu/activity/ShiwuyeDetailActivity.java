package com.youli.zbetuch_huangpu.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.LastInvest;
import com.youli.zbetuch_huangpu.entity.PersonInfo;
import com.youli.zbetuch_huangpu.entity.ResourcesDetailInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.MyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Response;

/**
 * Created by liutao on 2017/8/11.
 */

public class ShiwuyeDetailActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private boolean RHFL;    //人户分离
    private String DCLX;   //调查类型
    private ArrayAdapter<String> currStaAdapter;
    private RadioGroup rag,textView_type;
    private RadioButton rb1, rb2,radioGroup_men,radioGroup_phone;
    private Context mContext = ShiwuyeDetailActivity.this;
    private boolean isCheck;
    private Button diaochaBtn, submitBtn, detailInfoBtn, resultBtn;
    private LinearLayout diaochaLl, shidengLi, shidengAndphenoLi, shoolLi, biyeLi,person_ren;
    private TextView titleTv, zjlxTv, hujiTypeTv, cjrTv, mdDateTv;
    private ResourcesDetailInfo RDInfo;
    private EditText sfzEt, nameEt, sexEt, birthdayEt, nationEt, educationEt, hujiAddressEt,
            streetEt, jwEt, presentStatusEt, masterDateEt, presentIntentionEt,
            shidengDateEt, shidengVidEt, phoneEt, diaocharenEt, remarksEt, diaochaDateEt, hujiTypeEt, isCjrEt, shoolEt, majorEt, biyeEt, qrzEt;

    String ZJTYPE;


    private Spinner currStaSp, currIntSp;
    private String currIntStr, currStaStr;
    private String[] currStaData, currIntData;

    private boolean isSave;//是否提交

    private final int SUCCESS = 10000;
    private final int PERSONINFO = 10001;
    private final int SUBMIT_ADDRESS=10002;
    private final int NOPERSONINFO = 10003;
    private final int PROBLEM = 10005;

    private List<PersonInfo> personInfos = new ArrayList<>();

    private List<LastInvest> lastInfo = new ArrayList<>();

    private String mark;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SUCCESS:


                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    submitInfo(mark,"address");//提交经纬度
                                }
                            }
                    ).start();

                    break;


                case SUBMIT_ADDRESS://提交经纬度
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                    isSave = true;
                    setResult(ZiyuanDetailListActivity.ResultCode, null);
                    finish();
                    // EventBus.getDefault().post(new ResourcesDetailInfo());
                    break;

                case PROBLEM:

                    Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();

                    break;


                case NOPERSONINFO:

                    Toast.makeText(mContext, "对不起,查无此人", Toast.LENGTH_SHORT).show();

                    break;

                case PERSONINFO:

                    personInfos = (List<PersonInfo>) msg.obj;

//                    Intent intent=new Intent(mContext,PersonInfoActivity.class);
//                    intent.putExtra("personInfos", (Serializable) personInfos.get(0));
//                    startActivity(intent);

                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiwuye_detail);

        RDInfo = (ResourcesDetailInfo) getIntent().getSerializableExtra("RDInfo");
        isCheck = getIntent().getBooleanExtra("IsCheck", false);

        mark=getIntent().getStringExtra("mark");

        Log.e("2017-12-29","mark="+mark);

        initViews();
    }


    private void initViews() {
        person_ren= (LinearLayout) findViewById(R.id.person_ren);

        rag = (RadioGroup) findViewById(R.id.radioGroup1);
        rb1 = (RadioButton) findViewById(R.id.rd0);
        rb2 = (RadioButton) findViewById(R.id.rd1);
        rag.setOnCheckedChangeListener(this);

        textView_type = (RadioGroup) findViewById(R.id.textView_type);
        radioGroup_men = (RadioButton) findViewById(R.id.radioGroup_men);
        radioGroup_phone = (RadioButton) findViewById(R.id.radioGroup_phone);
        textView_type.setOnCheckedChangeListener(this);

        diaochaBtn = (Button) findViewById(R.id.shiwuye_detail_diaocha_btn);
        diaochaBtn.setOnClickListener(this);
        submitBtn = (Button) findViewById(R.id.shiwuye_detail_submit_btn);
        submitBtn.setOnClickListener(this);
        detailInfoBtn = (Button) findViewById(R.id.shiwuye_detail_info_btn);
        detailInfoBtn.setOnClickListener(this);
        resultBtn = (Button) findViewById(R.id.shiwuye_detail_result_btn);
        resultBtn.setOnClickListener(this);

        zjlxTv = (TextView) findViewById(R.id.shiwuye_detail_zjlx_tv);


        titleTv = (TextView) findViewById(R.id.shiwuye_detail_title_tv);
        hujiTypeTv = (TextView) findViewById(R.id.shiwuye_detail_huji_type_tv);
        cjrTv = (TextView) findViewById(R.id.shiwuye_detail_cjr_tv);
        mdDateTv = (TextView) findViewById(R.id.shiwuye_detail_master_date_tv);
        shidengLi = (LinearLayout) findViewById(R.id.shiwuye_detail_shideng_ll);
        shidengAndphenoLi = (LinearLayout) findViewById(R.id.shiwuye_detail_shideng_and_phone_ll);
        diaochaLl = (LinearLayout) findViewById(R.id.shiwuye_detail_diaocha_ll);
        shoolLi = (LinearLayout) findViewById(R.id.shiwuye_detail_shool_ll);
        biyeLi = (LinearLayout) findViewById(R.id.shiwuye_detail_biye_ll);

        sfzEt = (EditText) findViewById(R.id.shiwuye_detail_sfz_et);
        nameEt = (EditText) findViewById(R.id.shiwuye_detail_name_et);
        sexEt = (EditText) findViewById(R.id.shiwuye_detail_sex_et);
        birthdayEt = (EditText) findViewById(R.id.shiwuye_detail_birthday_et);
        nationEt = (EditText) findViewById(R.id.shiwuye_detail_nation_et);
        educationEt = (EditText) findViewById(R.id.shiwuye_detail_education_et);
        hujiAddressEt = (EditText) findViewById(R.id.shiwuye_detail_huji_address_et);
        streetEt = (EditText) findViewById(R.id.shiwuye_detail_street_et);
        jwEt = (EditText) findViewById(R.id.shiwuye_detail_jw_et);

        presentStatusEt = (EditText) findViewById(R.id.shiwuye_detail_present_status_et);
        masterDateEt = (EditText) findViewById(R.id.shiwuye_detail_master_date_et);
        presentIntentionEt = (EditText) findViewById(R.id.shiwuye_detail_present_intention_et);
        shidengDateEt = (EditText) findViewById(R.id.shiwuye_detail_shideng_date_et);
        shidengVidEt = (EditText) findViewById(R.id.shiwuye_detail_shideng_vid_et);
        phoneEt = (EditText) findViewById(R.id.shiwuye_detail_phone_et);
        diaocharenEt = (EditText) findViewById(R.id.shiwuye_detail_diaocharen_et);
        remarksEt = (EditText) findViewById(R.id.shiwuye_detail_remarks_et);
        shoolEt = (EditText) findViewById(R.id.shiwuye_detail_shool_et);
        majorEt = (EditText) findViewById(R.id.shiwuye_detail_major_et);
        biyeEt = (EditText) findViewById(R.id.shiwuye_detail_biye_et);
        qrzEt = (EditText) findViewById(R.id.shiwuye_detail_qrz_et);

        diaochaDateEt = (EditText) findViewById(R.id.shiwuye_detail_diaochadate_et);
        hujiTypeEt = (EditText) findViewById(R.id.shiwuye_detail_huji_type_et);
        isCjrEt = (EditText) findViewById(R.id.shiwuye_detail_is_cjr_et);

        currStaSp = (Spinner) findViewById(R.id.sp_shiwuye_detail_curr_sta);
        currIntSp = (Spinner) findViewById(R.id.sp_shiwuye_detail_curr_int);

        initData();

    }


    private void initData() {

        diaocharenEt.setText(HomePageActivity.adminName);

        if (isCheck) {
            diaochaLl.setVisibility(View.VISIBLE);
            submitBtn.setClickable(false);
            submitBtn.setBackgroundResource(R.drawable.button_enabled);

            remarksEt.setFocusable(false);
            remarksEt.setText(RDInfo.getDCBZ());
            remarksEt.setTextColor(Color.parseColor("#c0c0c0"));
        } else {
            remarksEt.setTextColor(Color.parseColor("#000000"));
            remarksEt.setFocusable(true);
            diaochaLl.setVisibility(View.GONE);
            submitBtn.setClickable(true);
        }

        if (TextUtils.equals("失业",mark)) {
           // person_ren.setVisibility(View.GONE);
            currStaData = getResources().getStringArray(R.array.resources_shiye_status);
            titleTv.setText("失业详细");
            shidengAndphenoLi.setVisibility(View.VISIBLE);
            shidengLi.setVisibility(View.VISIBLE);
            shoolLi.setVisibility(View.GONE);
            biyeLi.setVisibility(View.GONE);
            hujiTypeTv.setText("户籍类别");
            hujiTypeEt.setText(RDInfo.getHJLB());//户籍类别
            cjrTv.setText("是否残疾");
            isCjrEt.setText(RDInfo.getSFCJR());//是否残疾人
            mdDateTv.setText("摸底日期");
            masterDateEt.setText(RDInfo.getMDDATE());//摸底日期
            shidengDateEt.setText(RDInfo.getZJSYDJDATE().split("T")[0]);//最近失业登记日期
            shidengVidEt.setText(RDInfo.getSYDJYXQ().split("T")[0]);//失业登记有效期

        } else if (TextUtils.equals("无业",mark)) {
            currStaData = getResources().getStringArray(R.array.resources_wuye_status);
            titleTv.setText("无业详细");
            hujiTypeTv.setText("是否已核");
            hujiTypeEt.setText(RDInfo.getSFYH());//是否已核
            cjrTv.setText("是否残疾");
            isCjrEt.setText(RDInfo.getSFCJR());//是否残疾人
            shidengAndphenoLi.setVisibility(View.GONE);
            shoolLi.setVisibility(View.GONE);
            biyeLi.setVisibility(View.GONE);
            shidengLi.setVisibility(View.INVISIBLE);
            mdDateTv.setText("摸底日期");
            masterDateEt.setText(RDInfo.getMDDATE());//摸底日期
        } else if (TextUtils.equals("应届生",mark)) {
            currStaData = getResources().getStringArray(R.array.resources_wuye_status);
            titleTv.setText("应届生详细");
            shidengAndphenoLi.setVisibility(View.GONE);
            shoolLi.setVisibility(View.VISIBLE);
            biyeLi.setVisibility(View.VISIBLE);
            hujiTypeTv.setText("学习起始日期");
            hujiTypeEt.setText(RDInfo.getXXQSDATE());//学习起始日期
            cjrTv.setText("学习终止日期");
            isCjrEt.setText(RDInfo.getXXZZDATE());//学习终止日期
            mdDateTv.setText("就业状态");
            masterDateEt.setText(RDInfo.getJYZT());//就业状态
            shoolEt.setText(RDInfo.getXXMC());//学校名称
            majorEt.setText(RDInfo.getSXZY());//所学专业
            biyeEt.setText(RDInfo.getSFBYY());//是否毕肄业
            qrzEt.setText(RDInfo.getSFQRZ());//是否全日制
        }

        String sg = RDInfo.getZJLX();
        if (sg.length() <= 2) {
            char item1 = sg.charAt(0);
            char item2 = sg.charAt(1);
            ZJTYPE = item1 + "    " + item2;

        } else if (sg.length() <= 3) {
            char item1 = sg.charAt(0);
            char item2 = sg.charAt(1);
            char item3 = sg.charAt(2);
            ZJTYPE = item1 + "  " + item2 + "  " + item3;
        }
        zjlxTv.setText(ZJTYPE);//证件类型
        sfzEt.setText(RDInfo.getZJHM());//身份证
        nameEt.setText(RDInfo.getXM());//姓名
        sexEt.setText(RDInfo.getGENDER());//性别
        birthdayEt.setText(RDInfo.getCSDATE().split("T")[0]);//出生日期
        nationEt.setText(RDInfo.getMINZU());//民族
        educationEt.setText(RDInfo.getWHCD());//文化程度
        hujiAddressEt.setText(RDInfo.getHKDZ());//户口地址
        streetEt.setText(RDInfo.getHJJDMC());//街道
        jwEt.setText(RDInfo.getHJJWMC());//居委
        presentStatusEt.setText(RDInfo.getMQZK());//目前状况

        presentIntentionEt.setText(RDInfo.getDQYX());//当前意向
        phoneEt.setText(RDInfo.getLXDH());//联系电话
        Calendar c = Calendar.getInstance();
        diaochaDateEt.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));//调查日期

       if(currStaData!=null) {
           currStaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currStaData);
           currStaSp.setAdapter(currStaAdapter);
       }

        currIntData = getResources().getStringArray(R.array.resources_wuye_yixiang);
        ArrayAdapter<String> currIntAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currIntData);
        currIntSp.setAdapter(currIntAdapter);

        if (isCheck) {
            spinnerSetSelection("new");
            currIntSp.setEnabled(false);
            currStaSp.setEnabled(false);
            rb1.setEnabled(false);
            rb2.setEnabled(false);
            radioGroup_phone.setEnabled(false);
            radioGroup_men.setEnabled(false);
        } else {
            currIntSp.setEnabled(true);
            currStaSp.setEnabled(true);
            radioGroup_phone.setEnabled(true);
            radioGroup_men.setEnabled(true);
        }

        currIntSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currIntStr = (String) currIntSp.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        currStaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currStaStr = (String) currStaSp.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.shiwuye_detail_diaocha_btn://点击调查

                if (diaochaLl.getVisibility() == View.GONE) {
                    diaochaLl.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.shiwuye_detail_submit_btn://提交
             //   if (RDInfo.getDCID() !=1){
                    if (rag.getCheckedRadioButtonId() != rb1.getId() && rag.getCheckedRadioButtonId() != rb2.getId()) {
                        //Toast.makeText(this, "请选择是否人户分离", Toast.LENGTH_SHORT).show();
                        MyToast.makeText(this,"请选择是否人户分离",1000).show();

                        return;
                    }
             //   }



                if (textView_type.getCheckedRadioButtonId() != radioGroup_men.getId() && textView_type.getCheckedRadioButtonId() != radioGroup_phone.getId()) {
                  //  Toast.makeText(this, "请选择调查类型", Toast.LENGTH_SHORT).show();
                    MyToast.makeText(this,"请选择调查类型",1000).show();
                    return;
                }


                if (TextUtils.equals((String) currStaSp.getSelectedItem(), "请选择")) {
                //    Toast.makeText(this, "请选择目前状况!", Toast.LENGTH_SHORT).show();
                    MyToast.makeText(this,"请选择目前状况!",1000).show();
                    return;
                }
                if (TextUtils.equals((String) currIntSp.getSelectedItem(), "请选择")) {
                  //  Toast.makeText(this, "请选择当前意向!", Toast.LENGTH_SHORT).show();
                    MyToast.makeText(this,"请选择当前意向!",1000).show();
                    return;
                }


                if (isSave) {
                    Toast.makeText(this, "已经提交过了，不能重复提交", Toast.LENGTH_SHORT).show();
                   // MyToast.makeText(this,"请选择调查类型",1000).show();
                } else {
                    showSubmitDialog();
                }

                break;

            case R.id.shiwuye_detail_result_btn://调查结果同上

                spinnerSetSelection("old");


                break;

            case R.id.shiwuye_detail_info_btn://详细信息

                getPersonInfo();
                break;

        }

    }

    private void spinnerSetSelection(String type) {
        for (int i = 0; i < currStaData.length; i++) {
            if (TextUtils.equals("old", type)) {
                if (TextUtils.equals(RDInfo.getMQZK(), currStaData[i])) {
                    currStaSp.setSelection(i);
                }
            } else {
                if (TextUtils.equals(RDInfo.getMQZK_NEW(), currStaData[i])) {
                    currStaSp.setSelection(i);
                }
            }
        }

        currStaAdapter.notifyDataSetChanged();
//        currStaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currStaData);
//        currStaSp.setAdapter(currStaAdapter);

        for (int i = 0; i < currIntData.length; i++) {
            if (TextUtils.equals("old", type)) {
                if (TextUtils.equals(RDInfo.getDQYX(), currIntData[i])) {
                    currIntSp.setSelection(i);
                }
            } else {
                if (TextUtils.equals(RDInfo.getDQYX_NEW(), currIntData[i])) {
                    currIntSp.setSelection(i);
                }
            }
        }

        if (RDInfo.isRHFL()==true){
            rag.check(R.id.rd0);
        }else if (RDInfo.isRHFL()==false){
            rag.check(R.id.rd1);
        }

        if (TextUtils.equals(RDInfo.getDCLX(),"上门调查")){
            textView_type.check(R.id.radioGroup_men);

        }else if(TextUtils.equals(RDInfo.getDCLX(),"电话调查")){
            textView_type.check(R.id.radioGroup_phone);
        }




    }


    private void showSubmitDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定保存此信息吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                submitInfo(mark,"detail");
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

    private void submitInfo(String type,String mark) {
        String url = null;
        Response response=null;
        //Set_Resource_Survey_Detil_SY.aspx 参数 MDID 明细id ,DCBZ 调查备注,MQZK_NEW 调查的目前状况,DQYX_NEW  调查的当前意向

        if(TextUtils.equals(mark,"detail")) {

            if (TextUtils.equals("失业",type)) {
                url = MyOkHttpUtils.BaseUrl + "/Json/Set_Resource_Survey_Detil_SY.aspx";
            } else if (TextUtils.equals("无业",type)) {
                url = MyOkHttpUtils.BaseUrl + "/Json/Set_Resource_Survey_Detil_WY.aspx";
            } else if (TextUtils.equals("应届生",type)) {
                url = MyOkHttpUtils.BaseUrl + "/Json/Set_Resource_Survey_Detil_YJS.aspx";
            }



            response = MyOkHttpUtils.okHttpGet(url + "?MDID=" + RDInfo.getMDID() + "&DCBZ=" + remarksEt.getText().toString() +
                    "&MQZK_NEW=" + currStaStr + "&DQYX_NEW=" + currIntStr + "&DCLX=" + DCLX + "&RHFL=" + RHFL);
        }else if(TextUtils.equals(mark,"address")){

            if (TextUtils.equals("失业",type)) {
                url = MyOkHttpUtils.BaseUrl + "/Json/Set_GPS_Staff_Log.aspx?sfz="+RDInfo.getZJHM()+"&detail=失业调查&gps="+HomePageActivity.jDuStr+","+HomePageActivity.wDuStr;
            } else if (TextUtils.equals("无业",type)) {
                url = MyOkHttpUtils.BaseUrl + "/Json/Set_GPS_Staff_Log.aspx?sfz="+RDInfo.getZJHM()+"&detail=无业调查&gps="+HomePageActivity.jDuStr+","+HomePageActivity.wDuStr;
            } else if (TextUtils.equals("应届生",type)) {
                url = MyOkHttpUtils.BaseUrl + "/Json/Set_GPS_Staff_Log.aspx?sfz="+RDInfo.getZJHM()+"&detail=应届生调查&gps="+HomePageActivity.jDuStr+","+HomePageActivity.wDuStr;
            }
            response = MyOkHttpUtils.okHttpGet(url);

            Log.e("2017-12-18","url="+url);

        }


        try {
            Message msg = Message.obtain();
            if (response != null) {
                String resStr = response.body().string();
                Log.e("2017/11/9", "提交==" + resStr);
                if (TextUtils.equals("True", resStr)&&TextUtils.equals(mark,"detail")) {
                    msg.what = SUCCESS;
                    mHandler.sendMessage(msg);

                }
                if (TextUtils.equals("True", resStr)&&TextUtils.equals(mark,"address")) {
                    msg.what = SUBMIT_ADDRESS;
                    mHandler.sendMessage(msg);
                }

               if(!TextUtils.equals("True", resStr)) {
                    msg.what = PROBLEM;
                    mHandler.sendMessage(msg);
                }
            } else {
                msg.what = PROBLEM;
                mHandler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPersonInfo() {
        // http://web.youli.pw:89/Json/Get_BASIC_INFORMATION.aspx?sfz=310101198711030515
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String url = MyOkHttpUtils.BaseUrl + "/Json/Get_BASIC_INFORMATION.aspx?sfz=" + RDInfo.getZJHM();
                        Response response = MyOkHttpUtils.okHttpGet(url);
                        if (response != null) {
                            try {
                                String strRes = response.body().string();
                                Message msg = Message.obtain();
                                if (TextUtils.equals(strRes, "[null]")) {
                                    msg.what = NOPERSONINFO;
                                    mHandler.sendMessage(msg);
                                } else {
                                    Gson gson = new Gson();
                                    msg.obj = gson.fromJson(strRes, new TypeToken<List<PersonInfo>>() {
                                    }.getType());
                                    msg.what = PERSONINFO;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        if(!isCheck){

        switch (group.getId()) {
            case R.id.radioGroup1:

                    if (rag.getCheckedRadioButtonId() == rb1.getId()) {
                        if (!TextUtils.equals("失业",mark)) {
                            currStaData = getResources().getStringArray(R.array.resources_wuye_status);
                        }
                        RHFL = true;
                    } else if (rag.getCheckedRadioButtonId() == rb2.getId()) {
                        if (!TextUtils.equals("失业",mark)) {
                            currStaData = getResources().getStringArray(R.array.resources_wuye_status_ren);
                        }
                        RHFL = false;
                    }
                    currStaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currStaData);
                    currStaSp.setAdapter(currStaAdapter);

                break;
            case R.id.textView_type:
                if (textView_type.getCheckedRadioButtonId() == radioGroup_men.getId()) {
                    DCLX = "上门调查";
                } else if (textView_type.getCheckedRadioButtonId() == radioGroup_phone.getId()) {
                    DCLX = "电话调查";
                }
                break;
        }
        }

    }
}