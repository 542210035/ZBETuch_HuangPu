package com.youli.zbetuch_huangpu.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.AdminInfo;
import com.youli.zbetuch_huangpu.entity.EduInfo;
import com.youli.zbetuch_huangpu.entity.MyFollowInfo;
import com.youli.zbetuch_huangpu.entity.PersonJwInfo;
import com.youli.zbetuch_huangpu.entity.PersonStreetInfo;
import com.youli.zbetuch_huangpu.entity.PolicyTypeInfo;
import com.youli.zbetuch_huangpu.utils.FileUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SpinnerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Response;

public class PersonalInfoQuery extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_CAMERA = 9999;

    private final int  PROBLEM=10001;
    private final int SUCCEED_EDU=10002;//文化程度
    private final int SUCCEED_STREET=10003;//街道
    private final int SUCCEED_JUWEI=10004;//居委
    private final int OVERTIME=10005;//登录超时
    private Button btn_scanning;
    private EditText et_id_num;
    private Button btn_query_id_num;
    private EditText et_personName;
    private Spinner spinner_sex;
    private Spinner spinner_country;
    private Spinner spinner_street;
    private Spinner spinner_neighborhood_committee;
    private EditText et_age_from;
    private EditText et_age_to;
    private EditText et_shiye_from;
    private EditText et_shiye_to;
    private Spinner spinner_status;
    private Spinner spinner_situation;
    private Spinner spinner_edu;//文化程度
    private Spinner spinner_current_intent;
    private Context mContext = this;
    private CheckBox cb_resource;
    private Button btn_condition_query;

    private List<EduInfo> eduData=new ArrayList<>();
    private List<PersonStreetInfo> streetData=new ArrayList<>();
    private List<PersonJwInfo> jwData=new ArrayList<>();

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED_EDU:

                    eduData.addAll((List<EduInfo>)msg.obj);

                    SpinnerUtil.spSetAdapter(mContext,spinner_edu,eduData);
                    getNetWork("STREET",0);
                    break;

                case SUCCEED_STREET:

                    streetData.addAll((List<PersonStreetInfo>)msg.obj);

                    SpinnerUtil.spSetAdapter(mContext,spinner_street,streetData);

                    break;

                case SUCCEED_JUWEI:

                    jwData.clear();
                    jwData.addAll((List<PersonJwInfo>)msg.obj);

                    Log.e("2018-1-5","长度="+jwData.size());

                   // if(jwData.size()!=0) {
                        SpinnerUtil.spSetAdapter(mContext, spinner_neighborhood_committee, jwData);
                   // }else{
                      //  SpinnerUtil.setSpinner(mContext,spinner_neighborhood_committee,R.array.jwnull);
             //   }
                    break;

                case PROBLEM:
                    Toast.makeText(mContext, "网络不给力", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.personal_info_query_layout);

        // 初始化扫描身份证的功能
        initAccessTokenWithAkSk();

        initView();
    }

    private void initAccessTokenWithAkSk(){

        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                Log.e("2018-1-6", "onResult: " + result.toString());
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.e("2018-1-6", "onError: " + error.getMessage());
            }
        },getApplicationContext(),"SRpXfX1QtfW1eAySKj6XwIEP","65jfrjOTzzAz05oXospUd8n83UpAHIrS");

    }

    private void initView() {
        et_id_num = (EditText) findViewById(R.id.et_id_num);
        btn_scanning = (Button) findViewById(R.id.btn_scanning);
        btn_scanning.setOnClickListener(this);
        btn_query_id_num = (Button) findViewById(R.id.btn_query_id_num);
        et_personName = (EditText) findViewById(R.id.et_personal_name);
        spinner_sex = (Spinner) findViewById(R.id.spinner_sex);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_street = (Spinner) findViewById(R.id.spinner_street);
        spinner_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                boolean isContain=false;

                for(PersonStreetInfo info:streetData){

                    if(TextUtils.equals("全部",spinner_street.getSelectedItem()+"")){

                        isContain=true;
                        SpinnerUtil.setSpinner(mContext,spinner_neighborhood_committee,R.array.jwall);
                        break;
                    }

                    if(TextUtils.equals(info.getJDMC(),spinner_street.getSelectedItem()+"")){

                        isContain=true;
                        getNetWork("JUWEI",info.getJDID());
                        break;
                    }
                }

                if(!isContain){
                    SpinnerUtil.setSpinner(mContext,spinner_neighborhood_committee,R.array.jwnull);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_neighborhood_committee = (Spinner) findViewById(R.id
                .spinner_neighborhood_committee);
        et_age_from = (EditText) findViewById(R.id.et_age_from);
        et_age_to = (EditText) findViewById(R.id.et_age_to);
        et_shiye_from = (EditText) findViewById(R.id.et_shiye_from);
        et_shiye_to = (EditText) findViewById(R.id.et_shiye_to);
        spinner_status = (Spinner) findViewById(R.id.spinner_status);
        spinner_current_intent = (Spinner) findViewById(R.id.spinner_current_intent);
        spinner_situation = (Spinner) findViewById(R.id.spinner_current_situation);
        spinner_edu= (Spinner) findViewById(R.id.sp_edu);

        cb_resource = (CheckBox) findViewById(R.id.cb_resources);

        btn_condition_query = (Button) findViewById(R.id.btn_condition_query);
        btn_condition_query.setOnClickListener(this);

        getNetWork("EDU",0);//获取网络数据，文化程度，街道，居委的数据

        initSpinner();
    }


    private void getNetWork(final String mark, final int id){

        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = null;

                if(TextUtils.equals(mark,"EDU")) {//文化程度
                    //http://web.youli.pw:8088/Json/Get_WHCD.aspx
                    url = MyOkHttpUtils.BaseUrl + "/Json/Get_WHCD.aspx";
                }else if(TextUtils.equals(mark,"STREET")){//街道
                  //  http://web.youli.pw:8088/Json/Get_JD.aspx
                    url = MyOkHttpUtils.BaseUrl + "/Json/Get_JD.aspx";
                }else if(TextUtils.equals(mark,"JUWEI")){//居委
                    //http://web.youli.pw:8088/Json/Get_JW.aspx?JDID=3
                    url = MyOkHttpUtils.BaseUrl + "/Json/Get_JW.aspx?JDID="+id;
                    Log.e("2018-1-5","居委="+url);
                }

                Response response=MyOkHttpUtils.okHttpGet(url);

                Message msg=Message.obtain();

                if(response!=null){

                    if(response.body()!=null){

                        try {
                            String resStr=response.body().string();

                            if(!TextUtils.equals(resStr,"")){

                                try{

                                    Gson gson=new Gson();
                                    if(TextUtils.equals(mark,"EDU")){

                                        msg.obj=gson.fromJson(resStr,new TypeToken<List<EduInfo>>(){}.getType());
                                        msg.what = SUCCEED_EDU;
                                    }else if(TextUtils.equals(mark,"STREET")){

                                        msg.obj=gson.fromJson(resStr,new TypeToken<List<PersonStreetInfo>>(){}.getType());
                                        msg.what = SUCCEED_STREET;
                                    }else if(TextUtils.equals(mark,"JUWEI")){

                                        msg.obj=gson.fromJson(resStr,new TypeToken<List<PersonJwInfo>>(){}.getType());
                                        msg.what = SUCCEED_JUWEI;
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

    /**
     * 初始化 Spinner
     */
    private void initSpinner() {
        btn_query_id_num.setOnClickListener(this);
        //性别 Spinner
        SpinnerUtil.setSpinner(mContext,spinner_sex, R.array.spinner_sex);
        //状态 Spinner
        SpinnerUtil.setSpinner(mContext,spinner_status, R.array.spinner_status);
        //当前意向
        SpinnerUtil.setSpinner(mContext,spinner_current_intent, R.array.spinner_current_intent);
        //区县
        SpinnerUtil.setSpinner(mContext,spinner_country, R.array.quxian);

        //目前状况
        SpinnerUtil.setSpinner(mContext,spinner_situation, R.array.modi);
    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_id_num://查询
                final String sfzStr = et_id_num.getText().toString().trim();
                if (sfzStr.isEmpty()) {
                    Toast.makeText(this, "身份证号码不能为空", Toast.LENGTH_SHORT).show();

                } else if (sfzStr.length() < 18) {
                    Toast.makeText(this, "对不起，您所输入的身份证号码有误，请重新输入", Toast.LENGTH_SHORT).show();

                } else {

                    Intent i=new Intent(mContext,PersonInfoListActivity.class);
                    i.putExtra("sfz",sfzStr);
                    startActivity(i);

                }
                break;

            case R.id.btn_condition_query:

                String sexStr = null;//性别

                if(TextUtils.equals(spinner_sex.getSelectedItem().toString(),"全部")){
                    sexStr="";
                }else{
                    sexStr=spinner_sex.getSelectedItem().toString();
                }

                 String isOverdue;//是否过期

                if(cb_resource.isChecked()){
                    isOverdue="无效";
                }else{
                    isOverdue="有效";
                }

                String statusStr = null;//状态

                if(TextUtils.equals(spinner_status.getSelectedItem().toString(),"全部")){
                    statusStr="";
                }else{
                    statusStr=spinner_status.getSelectedItem().toString();
                }

                String intentStr = null;//当前意向

                if(TextUtils.equals(spinner_current_intent.getSelectedItem().toString(),"全部")){
                    intentStr="";
                }else{
                    intentStr=spinner_current_intent.getSelectedItem().toString();
                }

                String situationStr = null;//目前状况

                if(TextUtils.equals(spinner_situation.getSelectedItem().toString(),"全部")){
                    situationStr="";
                }else{
                    situationStr=spinner_situation.getSelectedItem().toString();
                }

                String eduStr = "";//文化程度


                if(spinner_edu.getSelectedItem()!=null) {
                    if (TextUtils.equals(spinner_edu.getSelectedItem().toString(), "全部")) {
                        eduStr = "";
                    } else {
                        eduStr = spinner_edu.getSelectedItem().toString();
                    }
                }
                String streetStr = "";//街道

                if(spinner_street.getSelectedItem()!=null) {
                    if (TextUtils.equals(spinner_street.getSelectedItem().toString(), "全部")) {
                        streetStr = "";
                    } else {
                        streetStr = spinner_street.getSelectedItem().toString();
                    }
                }
                String jwStr = "";//居委

                if(spinner_neighborhood_committee.getSelectedItem()!=null) {
                    if (TextUtils.equals(spinner_neighborhood_committee.getSelectedItem().toString(), "全部")) {
                        jwStr = "";
                    } else {
                        jwStr = spinner_neighborhood_committee.getSelectedItem().toString();
                    }
                }
                Intent i=new Intent(mContext,PersonInfoListActivity.class);
                i.putExtra("param","&xm="+et_personName.getText().toString().trim()+"&rows=20&gender="+sexStr+"&whcd="+eduStr+"&hjjd="+streetStr+"&hjjw="+jwStr+"&nld_ks="+et_age_from.getText().toString()+"&ndl_js="+et_age_to.getText().toString()+"&jyzt="+statusStr+"&dcmqzk_last="+situationStr+"&dcdqyx_last="+intentStr+"&rbl_gq="+isOverdue+"&syys_ks="+et_shiye_from.getText().toString()+"&syys_js="+et_shiye_to.getText().toString());
                startActivity(i);

                break;

            case R.id.btn_scanning://扫描
                Intent intent= new Intent(mContext, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtils.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_CAMERA&&resultCode== Activity.RESULT_OK){
            if(data!=null){
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtils.getSaveFile(getApplicationContext()).getAbsolutePath();
                if(!TextUtils.isEmpty(contentType)){
                    if(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)){
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    }
                }
            }
        }
    }

    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(String idCardSide, String filePath) {

        IDCardParams param=new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);

        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if(result!=null){

                    String num = "";
                    if (result.getIdNumber() != null) {
                        num = result.getIdNumber().toString();

                    }

                    Log.e("2018-1-6","身份证号="+num);
                    Intent i=new Intent(mContext,PersonInfoListActivity.class);
                    i.putExtra("sfz",num);
                    startActivity(i);

                }
            }

            @Override
            public void onError(OCRError error) {
                Toast.makeText(mContext, "识别出错,请重新扫描", Toast.LENGTH_SHORT).show();
                Log.e("2017-11-2", "onError: " + error.getMessage());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 释放内存资源
        OCR.getInstance().release();
    }

    }

