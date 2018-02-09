package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.FunctionPageAdapter;
import com.youli.zbetuch_huangpu.adapter.GridViewAdapter;
import com.youli.zbetuch_huangpu.naire.ShowWenJuanActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfhan on 2017/11/7.
 */

public class FunctionListActivity extends BaseActivity {

    private Context mContext=FunctionListActivity.this;
    private List<View> viewList;
    private ViewPager viewPager;
    private List<ImageView> pointViewList;
    private int[] myIcons;
    private TextView tvTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funtion_gridview_layout);

        initData();
        initUi();
    }

    private void initUi(){

        tvTitle= (TextView) findViewById(R.id.tv_title_function_list);
        //给标题设置字体
        Typeface type=Typeface.createFromAsset(getApplicationContext().getAssets(),"STXINGKA.TTF");
        tvTitle.setTypeface(type);


        viewPager= (ViewPager) findViewById(R.id.view_pager_function_list);
        viewPager.setAdapter(new FunctionPageAdapter(viewList));
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        for (int i = 0; i < viewList.size(); i++) {

            GridView gridView = (GridView) viewList.get(i);
            final int finalI = i;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent;

                    switch (finalI) {//判断哪一个 GridView
                        case 0:
                            switch (position) {//判断哪一个item

                                case 0:
                                    intent=new Intent(mContext,ZiyuandiaochaActivity.class);//资源调查
                                    startActivity(intent);
                                    break;

                                case 1:
                                    intent=new Intent(mContext,ShowWenJuanActivity.class);//专项调查
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent=new Intent(mContext,PolicyQueryActivity.class);//政策查询
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent=new Intent(mContext,OperationActivity.class);//操作说明
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent=new Intent(mContext, PersonalInfoQuery.class);//个人信息
                                    startActivity(intent);
                                    break;
//                                case 5:
//                                    intent=new Intent(mContext,ModifyPassword.class);//修改密码
//                                    startActivity(intent);
//                                    break;
                                case 5:
                                    intent=new Intent(mContext,DataTongjiActivity.class);//数据统计
                                    startActivity(intent);
                                    break;

                            }
                            break;

                    }
                }
            });
        }

        }



    private void initData(){

//        myIcons = new int[]{
//                R.drawable.gerenxinxi, R.drawable.zpxx, R.drawable.tjbb,
//                R.drawable.yjbys, R.drawable.zzdc, R.drawable.xcms,
//                R.drawable.sstj, R.drawable.ggrz, R.drawable.grsjzx,
//                R.drawable.jyzc, R.drawable.cczcfw, R.drawable.zzwd,
//                R.drawable.dbsy, R.drawable.hytz, R.drawable.gztz,
//                R.drawable.lyb, R.drawable.gggs, R.drawable.jjrd, R
//                .drawable.ggjl, R.drawable.wwxz, R.drawable.ggxx, R
//                .drawable.gglb, R.drawable.qqxx, R.drawable.yydbsy, R
//                .drawable.mmxg, R.drawable.ccsm, R.drawable.ssgf, R
//                .drawable.wjdc, R.drawable.ggwj};

        myIcons = new int[]{
                R.drawable.zydc,R.drawable.zxdc,R.drawable.zzcx,
                R.drawable.ccs,R.drawable.gerenxinxi,
                R.drawable.sjtj};
//                R.drawable.zpxx, R.drawable.tjbb,
//                R.drawable.yjbys, R.drawable.zzdc, R.drawable.xcms,
//                R.drawable.sstj,  R.drawable.grsjzx, R.drawable.gglb,
//                R.drawable.mmxg,  R.drawable.wjdc};

        viewList=new ArrayList<>();
        initGridView();
        initPoint();
    }

    private void initGridView(){

        boolean b=true;
        int temp=0;
        while (b){
            int result=temp+12;
            if(myIcons.length!=0&&result<myIcons.length){
                GridView gridView=new GridView(this);
                gridView.setNumColumns(3);
                gridView.setClickable(true);
                List<Integer> icons=new ArrayList<>();
                for(int i=temp;i<result;i++){
                    icons.add(myIcons[i]);
                }
                GridViewAdapter adapter=new GridViewAdapter(icons);
                gridView.setAdapter(adapter);
                temp=result;
                viewList.add(gridView);
            }else if(result-myIcons.length<=12){
                List<Integer> icons=new ArrayList<>();
                for(int i=temp;i<myIcons.length;i++){
                    icons.add(myIcons[i]);
                }
                GridView gridView=new GridView(this);
                gridView.setNumColumns(3);
                gridView.setClickable(true);
                GridViewAdapter adapter=new GridViewAdapter(icons);
                gridView.setAdapter(adapter);
                temp=myIcons.length-1;
                viewList.add(gridView);
                b=false;
            }else{
                b=false;
            }
        }
    }

    private void initPoint(){

        LinearLayout ll_point= (LinearLayout) findViewById(R.id.ll_point);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30,0,30,0);
        int pageNum=viewList.size();
        pointViewList=new ArrayList<>();
        for(int i=0;i<pageNum;i++){

            ImageView view=new ImageView(this);
            view.setLayoutParams(lp);
            if(i==0){
                view.setBackgroundResource(R.drawable.shape_point_selected);
            }else {
                view.setBackgroundResource(R.drawable.shape_point_unselected);
            }

            ll_point.addView(view);
            pointViewList.add(view);
        }
    }

    //页面滑动监听
    class MyPageChangeListener implements ViewPager.OnPageChangeListener{



        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int position) {

            for(int i=0;i<pointViewList.size();i++){

                if(i==position){
                    pointViewList.get(i).setBackgroundResource(R.drawable.shape_point_selected);

                }else{
                    pointViewList.get(i).setBackgroundResource(R.drawable.shape_point_unselected);
                }

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
