package com.youli.zbetuch_huangpu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.utils.MyDateUtils;

/**
 * Created by liutao on 2018/1/5.
 *
 * 基本信息
 */

public class BaseInfoFragment extends BaseVpFragment{

    private PersonListInfo pInfo;
    BaseInfoFragment fragment;

    private View view;
    private TextView tv_name,tv_sex,tv_Birthday,tv_FamilyName;
    private TextView tv_origin,tv_state,tv_Culture,tv_Telephone;
    private TextView tv_Street,tv_juwei,tv_id,tv_hkdz;
    private TextView tv_lxdz,tv_dqyx,tv_ldjlzzrq,tv_shiyeys;
    private TextView tv_mqdaszd,tv_dqcyx,tv_ldjlzczrq,tv_shicyeys;


    public static final BaseInfoFragment newInstance(PersonListInfo p){
        BaseInfoFragment fragment=new BaseInfoFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("pInfo",p);
        fragment.setArguments(bundle);
        return  fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pInfo=(PersonListInfo) getArguments().getSerializable("pInfo");

        view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_base_info,container,false);


        isViewCreated=true;//标记不能少

        return view;
    }

    @Override
    public void lazyLoadData() {
        if(isViewCreated){//逻辑都写这个里面
            tv_name= (TextView) view.findViewById(R.id.tv_person_info_name);//姓名
            tv_name.setText(pInfo.getXM());

            tv_sex= (TextView) view.findViewById(R.id.tv_fag_sex);//性别
            tv_sex.setText(pInfo.getGENDER());

            tv_Birthday= (TextView) view.findViewById(R.id.tv_person_info_birthday);//出生年月
            tv_Birthday.setText(MyDateUtils.stringToYMD(pInfo.getCSDATE1()));

            tv_FamilyName= (TextView) view.findViewById(R.id.tv_person_info_nation);//名族
            tv_FamilyName.setText(pInfo.getMINZU());

//            tv_origin= (TextView) view.findViewById(R.id.tv_person_info_jiguan);//籍贯
//            tv_origin.setText(pInfo.));

            tv_state= (TextView) view.findViewById(R.id.tv_person_info_state);//状态
            tv_state.setText(pInfo.getJYZT());

            tv_Culture= (TextView) view.findViewById(R.id.tv_person_info_edu);//文化程度
            tv_Culture.setText(pInfo.getWHCD());

            tv_Telephone= (TextView) view.findViewById(R.id.tv_person_info_md);//联系电话
            tv_Telephone.setText(pInfo.getLXDH());

            tv_Street= (TextView) view.findViewById(R.id.tv_person_info_jd);//街道
            tv_Street.setText(pInfo.getHJJDMC());

            tv_juwei= (TextView) view.findViewById(R.id.tv_person_info_jwh);//居委
            tv_juwei.setText(pInfo.getHJJWMC());

            tv_id= (TextView) view.findViewById(R.id.tv_person_info_sfzz);//身份证
            tv_id.setText(pInfo.getZJHM());

            tv_hkdz= (TextView) view.findViewById(R.id.tv_person_info_huji);//户口地址
            tv_hkdz.setText(pInfo.getHJDZ());

            tv_lxdz= (TextView) view.findViewById(R.id.tv_person_info_juzhu);//联系地址
            tv_lxdz.setText(pInfo.getLXDZ());

//            tv_mdzk= (TextView) view.findViewById(R.id.tv_person_info_phone);//摸底状况
//            tv_mdzk.setText(pInfo.getMD);

            tv_dqyx= (TextView) view.findViewById(R.id.tv_person_info_curr_intent);//当前意向
            tv_dqyx.setText(pInfo.getDCDQYX_LAST());

            tv_ldjlzzrq= (TextView) view.findViewById(R.id.tv_ldzzrq);//劳动经历终止日期
            tv_ldjlzzrq.setText(pInfo.getLDJLJYKSDATE());

            tv_shiyeys= (TextView) view.findViewById(R.id.tv_shiyeys);//失业月数
            tv_shiyeys.setText(pInfo.getSYDJDATE());

//            tv_mqdaszd= (TextView) view.findViewById(R.id.tv_mqdaszd);//目前档案所在地
//            tv_mqdaszd.setText(pInfo.get);
//
//            tv_FamilyName= (TextView) view.findViewById(R.id.tv_person_info_nation);//备注
//            tv_FamilyName.setText(pInfo.get);









        }

    }
}
