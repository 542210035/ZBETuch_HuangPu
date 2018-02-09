package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.MyFragmentPagerAdapter;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.fragment.BaseInfoFragment;
import com.youli.zbetuch_huangpu.fragment.BtInfoFragment;
import com.youli.zbetuch_huangpu.fragment.CyInfoFragment;
import com.youli.zbetuch_huangpu.fragment.LdjlFragment;
import com.youli.zbetuch_huangpu.fragment.PxInfoFragment;
import com.youli.zbetuch_huangpu.fragment.SameHjInfoFragment;
import com.youli.zbetuch_huangpu.fragment.SbInfoFragment;
import com.youli.zbetuch_huangpu.fragment.StudyJlFragment;
import com.youli.zbetuch_huangpu.fragment.YbInfoFragment;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


/**
 * Created by liutao on 2018/1/5.
 *
 * 个人信息界面
 */

public class PersonDetaileInfoActivity extends FragmentActivity {

    private Context mContext=this;


    private final int SUCCEED=10001;
    private final int SUCCEED_NODATA=10002;
    private final int  PROBLEM=10003;
    private final int OVERTIME=10004;//登录超时

    private PersonListInfo pInfo;
    private String zjhm;

    private MyViewPager viewPager;
    private List<Fragment> fragmentList;
    private TabLayout tl;
    private String [] title={"基本信息","同户籍信息","劳动经历","学习经历","补贴信息","医保信息","培训信息","创业信息","社保信息"};

    private Button btnFollow;//关注

    private TextView nameTv;//姓名
    private TextView sexTv;//性别
    private TextView stateTv;//状态
    private TextView sfzTv;//证件号码

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED:


                    if(TextUtils.equals(msg.obj+"","True")){
                        Toast.makeText(mContext,"关注成功!",Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.equals(msg.obj+"","False")){
                        Toast.makeText(mContext,"您已经关注过了，不能重复关注",Toast.LENGTH_SHORT).show();
                    }
                    btnFollow.setEnabled(false);

                    break;
                case PROBLEM:
                    Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();

                    break;
                case SUCCEED_NODATA:

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
        setContentView(R.layout.activity_person_detail_info);

        pInfo=(PersonListInfo) getIntent().getSerializableExtra("pInfo");


        nameTv= (TextView) findViewById(R.id.tv_person_detail_info_name);
        sexTv= (TextView) findViewById(R.id.tv_person_detail_info_sex);
        stateTv= (TextView) findViewById(R.id.tv_person_detail_info_status);
        sfzTv= (TextView) findViewById(R.id.tv_person_detail_info_sfz);



        if(pInfo!=null) {

            nameTv.setText(appendSpace(pInfo.getXM()));
            sexTv.setText(pInfo.getGENDER());
            stateTv.setText(appendSpace(pInfo.getJYZT()));
            sfzTv.setText(pInfo.getZJHM());

        }

        btnFollow= (Button) findViewById(R.id.btn_person_info_follow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pInfo!=null) {
                    setFollow();
                }
            }
        });

//        viewPager= (MyViewPager) findViewById(R.id.vp_person_detail_info);
//        tl= (TabLayout) findViewById(R.id.tl_person_detail_info);
//        fragmentList=new ArrayList<>();
//        fragmentList.add(new BaseInfoFragment());//基本信息
//        fragmentList.add(new SameHjInfoFragment());//同户籍信息
//        fragmentList.add(LdjlFragment.newInstance(pInfo));//劳动经历

        viewPager= (MyViewPager) findViewById(R.id.vp_person_detail_info);
        tl= (TabLayout) findViewById(R.id.tl_person_detail_info);
        fragmentList=new ArrayList<>();
        fragmentList.add(BaseInfoFragment.newInstance(pInfo));//基本信息
        fragmentList.add(SameHjInfoFragment.newInstance(pInfo));//同户籍信息
        fragmentList.add(LdjlFragment.newInstance(pInfo));//劳动经历

        fragmentList.add(StudyJlFragment.newInstance(pInfo));//学习经历
        fragmentList.add(BtInfoFragment.newInstance(pInfo));//补贴信息
        fragmentList.add(new YbInfoFragment());//医保信息
        fragmentList.add(new PxInfoFragment());//培训信息
        fragmentList.add(new CyInfoFragment());//创业信息
        fragmentList.add(new SbInfoFragment());//社保信息

        FragmentManager fm=getSupportFragmentManager();
        MyFragmentPagerAdapter fpAdapter=new MyFragmentPagerAdapter(fm,fragmentList);
        viewPager.setAdapter(fpAdapter);
        viewPager.setOffscreenPageLimit(title.length);

        tl.setupWithViewPager(viewPager);

        for(int i=0;i<tl.getTabCount();i++){
            TabLayout.Tab tab=tl.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
    }

    private View getTabView(int position){

        View view= LayoutInflater.from(this).inflate(R.layout.tab_item,null,false);

        ImageView iv= (ImageView) view.findViewById(R.id.tab_item_iv);
        TextView tv= (TextView) view.findViewById(R.id.tab_item_tv);
        //iv.setImageResource(R.drawable.sel_image);
        tv.setText(title[position]);
        return view;

    }


    //给每个字符串后面加两个空格
    private String appendSpace(String para){

        String regex = "(.{1})";
        para = para.replaceAll(regex,"$1\t\t");

        return  para;

    }


    //设置关注
    private void setFollow(){


        new Thread(


                new Runnable() {
                    @Override
                    public void run() {


                            //  http://web.youli.pw:8088/Json/Set_Attention.aspx?sfz=000000196012110010&name=王建成&type=0
                       String    url=MyOkHttpUtils.BaseUrl+"/Json/Set_Attention.aspx?sfz="+pInfo.getZJHM()+"&name="+pInfo.getXM()+"&type=0";

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Log.e("2018-01-10","关注=="+url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String followStr=response.body().string();

                                    msg.obj=followStr;
                                    msg.what=SUCCEED;

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
