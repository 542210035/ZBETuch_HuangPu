package com.youli.zbetuch_huangpu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.fragment.CurrentMeetFragment;
import com.youli.zbetuch_huangpu.fragment.HistoryMeetFragment;

/**
 * 作者: zhengbin on 2017/12/1.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 会议管理
 */

public class MeetManageActivity extends FragmentActivity {

    private FragmentManager fm=this.getSupportFragmentManager();

    private CurrentMeetFragment cmF;//当前会议的fragment
    private HistoryMeetFragment hmF;//历史会议的fragment

    private TextView readnumTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_manage);

        cmF=new CurrentMeetFragment();
        hmF=new HistoryMeetFragment();

        fm.beginTransaction().add(R.id.fl_meet_manage,cmF)
                .add(R.id.fl_meet_manage,hmF).commit();

        fm.beginTransaction().hide(hmF).show(cmF).commit();//隐藏历史会议，显示当前会议

        readnumTv= (TextView) findViewById(R.id.tv_meet_manage_readnum);
        readnumTv.setText("本月0条，已读0条读取率0%");
    }

    public void onChange(View v){

        switch (v.getId()){

            case R.id.rb_meet_manage_current:
                fm.beginTransaction().hide(hmF).show(cmF).commit();//隐藏历史会议，显示当前会议
                break;


            case R.id.rb_meet_manage_history:
                fm.beginTransaction().hide(cmF).show(hmF).commit();//显示历史会议，隐藏当前会议
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
