package com.youli.zbetuch_huangpu.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.CreateActivityIndo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;

public class GuidanceManagerActivity extends BaseActivity {
    private EditText et_activity_1,et_place_2,et_startdate_3,
            et_enddate_4,et_Contacts_5,et_phone_6,
            et_number_7,et_brief_8,et_remarks_9;

    private CreateActivityIndo createa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance_manager);
        createa= (CreateActivityIndo) getIntent().getSerializableExtra("ZDHD");
        initView();
    }
    private void initView(){
        et_activity_1= (EditText) findViewById(R.id.edit_zd_1);
        et_place_2= (EditText) findViewById(R.id.edit_zd_2);
        et_startdate_3= (EditText) findViewById(R.id.edit_zd_3);
        et_enddate_4= (EditText) findViewById(R.id.edit_zd_4);
        et_Contacts_5= (EditText) findViewById(R.id.edit_zd_5);
        et_phone_6= (EditText) findViewById(R.id.edit_zd_6);
        et_number_7= (EditText) findViewById(R.id.edit_zd_7);
        et_brief_8= (EditText) findViewById(R.id.edit_zd_8);
        et_remarks_9= (EditText) findViewById(R.id.edit_zd_9);

        et_activity_1.setText(createa.getHDTHEME());
        et_place_2.setText(createa.getHDADDRESS());
        et_startdate_3.setText(MyDateUtils.stringToYMD(createa.getHDKSDATE()));
        et_enddate_4.setText(MyDateUtils.stringToYMD(createa.getHDJSDATE()));
        et_Contacts_5.setText(createa.getLXRXM());
        et_phone_6.setText(createa.getLXRDH());
        if (createa.getHDKBMRS()>0) {
            et_number_7.setText(createa.getHDKBMRS()+"");
        }else {
            et_number_7.setText(" ");
        }
        et_brief_8.setText(createa.getHDJJ());
        et_remarks_9.setText(createa.getBZSM());
        Log.e("2018-3-5",createa.getHDTHEME()+"");
    }
}
