package com.youli.zbetuch_huangpu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liutao on 2018/1/5.
 *
 * 配合viewPager时候，使用的懒加载
 */

public class BaseVpFragment extends Fragment{

    public boolean isViewCreated=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            lazyLoadData();
            if( isViewCreated){
                isViewCreated=false;
            }
        }
    }

    public  void lazyLoadData(){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()) {
            lazyLoadData();
            if( isViewCreated){
                isViewCreated=false;
            }

        }
    }

}
