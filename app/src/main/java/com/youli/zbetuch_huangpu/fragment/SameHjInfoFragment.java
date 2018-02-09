package com.youli.zbetuch_huangpu.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youli.zbetuch_huangpu.R;

import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.HouseholdInfo;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by liutao on 2018/1/5.
 *
 * 同户籍信息
 */
//implements View.OnClickListener,AdapterView.OnItemLongClickListener
public class SameHjInfoFragment extends BaseVpFragment  {
    private PersonListInfo pInfo;
    private View view;
    private CommonAdapter adapderm1,adapderm2;
    private boolean a=true;
    private boolean b=true;

    private  PullToRefreshListView lv1,lv2;
    private TextView tv1,tv2;
    private ImageView iv1,iv2;
    private TextView tv_wu;
    private  LinearLayout layout1,layout2;
    private int index1;
    private int index2;

    private final int SUCCESS_HUJI=10000;
    private final int SUCCESS_JUZHU=10001;
    private final int SUCCESS_PINFO=10002;
    private  final int SUCCESS_NODATA=10003;
    private final int  PROBLEM=10004;

    private List<HouseholdInfo> hujiData=new ArrayList<>();
    private List<HouseholdInfo> lianxiData=new ArrayList<>();



    public static final SameHjInfoFragment newInstance(PersonListInfo zjhm){
        SameHjInfoFragment fragment=new SameHjInfoFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("zjhm",zjhm);
        fragment.setArguments(bundle);
        return  fragment;
    }


//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            ProgressDialogUtils.dismissMyProgressDialog(getActivity());
//            switch (msg.what){
//                case SUCCESS_HUJI:
//                    if(index1==0){
//                        hujiData.clear();
//                    }else if (index1>=1){
//                        hujiData.clear();
//                    }
//                    hujiData.addAll((List<HouseholdInfo>)msg.obj);
//                    if(hujiData.size()>0){
//                        getAdapder1(hujiData);
//                    }
//                break;
//
//                case SUCCESS_JUZHU:
//                    if(index2==0){
//                        lianxiData.clear();
//                    }else if (index2>=1){
//                        lianxiData.clear();
//                    }
//                    lianxiData.addAll((List<HouseholdInfo>)msg.obj);
//                    if(lianxiData.size()>0){
//                        getAdapder2(lianxiData);
//                    }
//                    break;
//
//                case PROBLEM:
//                    if(getActivity()!=null) {
//                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
////
////                case SUCCESS_PINFO:
////                    if(getActivity()!=null) {
////                        Intent intent = new Intent(getActivity(), PersonDetaileInfoActivity.class);
////                        intent.putExtra("personInfos", (Serializable) ((List<PersonListInfo>) msg.obj).get(0));
////                        startActivity(intent);
////                        getActivity().finish();
////                    }
////                    break;
//
//
//            }
//        }
//    };
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_same_hj_info,container,false);
//        isViewCreated=true;//标记不能少
//        pInfo=(PersonListInfo) getArguments().getSerializable("zjhm");
//        Log.e("--1--",""+pInfo);
//        return view;
//    }
//
//    @Override
//    public void lazyLoadData() {
//        if(isViewCreated){//逻辑都写这个里面
//            if(pInfo!=null) {
////                initView(view);
//            }
//        }
//
//    }
////
////    private void initView(View view){
////         layout1= (LinearLayout) view.findViewById(R.id.linear_item_geren1);
////         layout1.setOnClickListener(this);
////         layout2=(LinearLayout) view.findViewById(R.id.linear_item_geren2);
////         layout2.setOnClickListener(this);
////         iv1= (ImageView) view.findViewById(R.id.iv_item_geren1);
////         iv2= (ImageView) view.findViewById(R.id.iv_item_geren2);
////         tv1= (TextView) view.findViewById(R.id.tv_item_geren1);
////         tv2= (TextView) view.findViewById(R.id.tv_item_geren1);
////         tv_wu= (TextView) view.findViewById(R.id.tv_wu);
////         tv_wu.setVisibility(View.GONE);
////
////         lv1= (PullToRefreshListView) view.findViewById(R.id.lv_item_geren1);
////         lv1.setVisibility(View.GONE);
////         lv1.setMode(PullToRefreshBase.Mode.BOTH);
////
////         networkData(index1);
////         getJuzhuInfo(index2);
////         lv1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
////            @Override
////            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
////                ProgressDialogUtils.dismissMyProgressDialog(getActivity());
////                index1=0;
////                networkData(index1);
////            }
////
////            @Override
////            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
////                ProgressDialogUtils.dismissMyProgressDialog(getActivity());
////                index1++;
////                networkData(index1);
////            }
////        });
////
////         lv2= (PullToRefreshListView) view.findViewById(R.id.lv_item_geren2);
////         lv2.setVisibility(View.GONE);
////         lv2.setMode(PullToRefreshBase.Mode.BOTH);
////         lv2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
////            @Override
////            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
////                index2=0;
////                getJuzhuInfo(index2);
////            }
////            @Override
////            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
////                index2++;
////                getJuzhuInfo(index2);
////
////            }
////        });
////
////        if (TextUtils.equals(pInfo.getHJDZ(),"") && TextUtils.equals(pInfo.getLXDZ(),"")){
////            tv_wu.setVisibility(View.VISIBLE);
////            layout1.setVisibility(View.GONE);
////            layout2.setVisibility(View.GONE);
////        }
////        if (TextUtils.equals(pInfo.getHJDZ(),"")) {
////            layout1.setVisibility(View.GONE);
////        }
////        if (TextUtils.equals(pInfo.getLXDZ(),"")) {
////            layout2.setVisibility(View.GONE);
////        }
////        lv1.setOnItemLongClickListener(this);
////        lv2.setOnItemLongClickListener(this);
////    }
////
////    //长按监听器
////    @Override
////    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////        showFamilyDialog(position);
////        return true;
////    }
////
////    //长按对话框
////    private void showFamilyDialog(final int p){
////        if(getActivity()==null){
////            return;
////        }
////        //对话框确定按钮监听器调用方法
////        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
////        builder.setTitle("查看详细信息");
////        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                getPersonInfo(p);
////            }
////        });
////        builder.setNegativeButton("取消",null);
////        builder.show();
////    }
////
////    //长按确定获取的数据
////    private void  getPersonInfo(final int p){
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                String url=MyOkHttpUtils.BaseUrl+"/Json/Get_BASIC_INFORMATION.aspx?sfz="+hujiData.get(p).getZJHM();
////
////                Response response=MyOkHttpUtils.okHttpGet(url);
////                Message msg=Message.obtain();
////                if(response!=null){
////
////                    try {
////                        String resStr=response.body().string();
////                        if(!TextUtils.equals(resStr,"")&&!TextUtils.equals(resStr,"[]")) {
////                            Gson gson = new Gson();
////                            msg.obj = gson.fromJson(resStr, new TypeToken<List<PersonListInfo>>() {}.getType());
////                            msg.what = SUCCESS_PINFO;
////                        }else{
////                            msg.what = SUCCESS_NODATA;
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }else{
////                    msg.what=PROBLEM;
////                }
////                mHandler.sendMessage(msg);
////            }
////        }).start();
////    }
////
////    // 请求户籍地址
////    private void networkData(int index1){
//////        ProgressDialogUtils.showMyProgressDialog(getActivity());
////        if(index1==0) {
////            if (hujiData != null && hujiData.size() > 0 ) {
////                hujiData.clear();
////            }
////        }else if (index1>=1){
////            hujiData.clear();
////        }
////
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                //户籍地址
////                //http://web.youli.pw:8088/json/Get_HJDZ.aspx?sfz=000102199604061613
////                String url= MyOkHttpUtils.BaseUrl+"/Json/Get_HJDZ.aspx?sfz="+pInfo.getZJHM();
////                Response response=MyOkHttpUtils.okHttpGet(url);
////                Log.e("户籍地址：",""+response);
////
////                Message msg=Message.obtain();
////                if (response !=null){
////                    try {
////                        String resStr=response.body().string();
////                        if (!TextUtils.equals(resStr,"")&&!TextUtils.equals(resStr,"[]")){
////                            Gson gson=new Gson();
////                            msg.obj=gson.fromJson(resStr,new TypeToken<List<HouseholdInfo>>(){}.getType());
////                            msg.what=SUCCESS_HUJI;
////                        }else{
////                            msg.what=SUCCESS_NODATA;
////                        }
////                        mHandler.sendMessage(msg);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }else {
////                    msg.what=PROBLEM;
////                    mHandler.sendMessage(msg);
////                }
////            }
////        }).start();
////    }
////
////    // 获取联系地址
////    private void getJuzhuInfo(int index2) {
//////        ProgressDialogUtils.showMyProgressDialog(getActivity());
////        if(index2==0) {
////            if (lianxiData != null && lianxiData.size() > 0 ) {
////                lianxiData.clear();
////            }
////        }else if(index2>=1){
////            lianxiData.clear();
////        }
////        new Thread(
////                new Runnable() {
////                    @Override
////                    public void run() {
////                        //联系地址
////                        //http://web.youli.pw:8088/json/Get_LXDZ.aspx?sfz=000102199604061613
////                        String url= MyOkHttpUtils.BaseUrl+"/Json/Get_LXDZ.aspx?sfz="+pInfo.getZJHM();
////                        Response response=MyOkHttpUtils.okHttpGet(url);
////                        Log.e("联系地址:",""+response);
////                        Message msg=Message.obtain();
////                        if(response!=null){
////                            try {
////                                String resStr=response.body().string();
////                                if(!TextUtils.equals(resStr,"")&&!TextUtils.equals(resStr,"[]")){
////                                    Gson gson=new Gson();
////                                    msg.obj=gson.fromJson(resStr,new TypeToken<List<HouseholdInfo>>(){}.getType());
////                                    msg.what=SUCCESS_JUZHU;
////                                }else{
////                                    msg.what=SUCCESS_NODATA;
////                                }
////                                mHandler.sendMessage(msg);
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
////                        }else{
////                            msg.what=PROBLEM;
////                            mHandler.sendMessage(msg);
////                        }
////                    }
////                }
////        ).start();
////    }
////
////    //lv1———adapder
////    private void getAdapder1(final List<HouseholdInfo> mlist){
////           if (adapderm1==null){
////               adapderm1=new CommonAdapter(getActivity(),mlist,R.layout.item_item_family_info_lv) {
////                   @Override
////                   public void convert(CommonViewHolder holder, Object item, int position) {
////                       final ImageView head=holder.getView(R.id.iv_item_item_family_info_head);
////                         TextView name=holder.getView(R.id.tv_item_item_family_info_name);
////                         name.setText(mlist.get(position).getXM());
////                         TextView sex=holder.getView(R.id.tv_item_item_family_info_sex);
////                         sex.setText(mlist.get(position).getGENDER());
////                         TextView birthday=holder.getView(R.id.tv_item_item_family_info_birthday);
////                         birthday.setText(mlist.get(position).getCSDATE1().split("T")[0]);
////                         TextView idCard=holder.getView(R.id.tv_item_item_family_info_idcard);
////                         idCard.setText(mlist.get(position).getZJHM());
////                   }
////               };
////               lv1.setAdapter(adapderm1);
////           }else {
////               adapderm1.notifyDataSetChanged();
////           }
////
////        if (lv1.isRefreshing()) {
////            lv1.onRefreshComplete();
////        }
////    }
////
////    //lv2———adapder
////    private void getAdapder2(final List<HouseholdInfo> mlist){
////        if (adapderm2==null){
////            adapderm2=new CommonAdapter(getActivity(),mlist,R.layout.item_item_family_info_lv) {
////                @Override
////                public void convert(CommonViewHolder holder, Object item, int position) {
////                    final ImageView head=holder.getView(R.id.iv_item_item_family_info_head);
////                    TextView name=holder.getView(R.id.tv_item_item_family_info_name);
////                    name.setText(mlist.get(position).getXM());
////                    TextView sex=holder.getView(R.id.tv_item_item_family_info_sex);
////                    sex.setText(mlist.get(position).getGENDER());
////                    TextView birthday=holder.getView(R.id.tv_item_item_family_info_birthday);
////                    birthday.setText(mlist.get(position).getCSDATE1().split("T")[0]);
////                    TextView idCard=holder.getView(R.id.tv_item_item_family_info_idcard);
////                    idCard.setText(mlist.get(position).getZJHM());
////                }
////            };
////            lv2.setAdapter(adapderm2);
////        }else {
////            adapderm2.notifyDataSetChanged();
////        }
////        if (lv2.isRefreshing()) {
////            lv2.onRefreshComplete();
////        }
////    }
////
////    @Override
////    public void onClick(View v) {
////        switch (v.getId()){
////            case R.id.linear_item_geren1:
////                if (a==true){
////                    lv1.setVisibility(View.VISIBLE);
////                    iv1.setImageResource(R.drawable.sj1);
////                    a=false;
////                }else{
////                    lv1.setVisibility(View.GONE);
////                    iv1.setImageResource(R.drawable.sj);
////                    a=true;
////                }
////                break;
////
////            case R.id.linear_item_geren2:
////                if (b==true){
////                    iv2.setImageResource(R.drawable.sj1);
////                    lv2.setVisibility(View.VISIBLE);
////                    b=false;
////                }else{
////                    iv2.setImageResource(R.drawable.sj);
////                    lv2.setVisibility(View.GONE);
////                    b=true;
////                }
////                break;
////
////        }
//
//    }
//


}
