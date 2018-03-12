package com.youli.zbetuch_huangpu.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.PersonDetaileInfoActivity;
import com.youli.zbetuch_huangpu.entity.HouseholdInfo;
import com.youli.zbetuch_huangpu.entity.PersonListInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 *
 * 同户籍信息
 */

public class SameHjInfoFragment extends BaseVpFragment{
    private PersonListInfo pInfo;
    private View view;
    private ExpandableListView lv;
    private TextView tv_wsj;

    private final int SUCCESS_HUJI = 10000;
    private final int SUCCESS_JUZHU = 10001;
    private final int SUCCESS_PINFO = 10002;
    private final int SUCCESS_NODATA = 10003;
    private final int PROBLEM = 10004;

    private List<HouseholdInfo> hujiData = new ArrayList<>();
    private List<HouseholdInfo> lianxiData = new ArrayList<>();
    private Map<String,List<HouseholdInfo>> map=new HashMap<>();
    private String [] arr=new String[]{"户籍地址","现住址"};

    public static final SameHjInfoFragment newInstance(PersonListInfo zjhm) {
        SameHjInfoFragment fragment = new SameHjInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("zjhm", zjhm);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ProgressDialogUtils.dismissMyProgressDialog(getActivity());
            switch (msg.what) {
                case SUCCESS_HUJI:
                    hujiData.addAll((List<HouseholdInfo>) msg.obj);
//                    if (hujiData.size()!=0){
//                        map.put(arr[0],hujiData);
//                    }
                    break;

                case SUCCESS_JUZHU:
                    lianxiData.addAll((List<HouseholdInfo>) msg.obj);
                    break;

                case PROBLEM:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case SUCCESS_PINFO:
                    if(getActivity()!=null) {
                        Intent intent = new Intent(getActivity(), PersonDetaileInfoActivity.class);
                        intent.putExtra("pInfo",((List<PersonListInfo>) msg.obj).get(0));
                        startActivity(intent);
                        getActivity().finish();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_same_hj_info, container, false);
        isViewCreated = true;//标记不能少
        pInfo = (PersonListInfo) getArguments().getSerializable("zjhm");
        Log.e("--1--", "" + pInfo);
        return view;
    }

    @Override
    public void lazyLoadData() {
        if (isViewCreated) {//逻辑都写这个里面
            if (pInfo != null) {
                initView(view);
            }
        }}

    private void initView(View view) {
        lv= (ExpandableListView) view.findViewById(R.id.expandablelistview);
        tv_wsj= (TextView) view.findViewById(R.id.tv_wsj);
        map.put(arr[0],hujiData);
        map.put(arr[1],lianxiData);

        if (TextUtils.equals(pInfo.getHJDZ(),"") && TextUtils.equals(pInfo.getLXDZ(),"")){
            lv.setVisibility(View.GONE);
            tv_wsj.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.equals(pInfo.getHJDZ(),"") || !TextUtils.equals(pInfo.getLXDZ(),"")){
            Datas();  //获取网络数据写在这个方法里可以用一个加载框
        }
        lv.setAdapter(new setAdapter());
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                showFamilyDialog(position-1);
//                return true;
//            }
//        });
        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

//                Toast.makeText(getActivity(),"父位置==="+groupPosition+"子位置==="+childPosition,Toast.LENGTH_SHORT).show();
                                showFamilyDialog(childPosition,groupPosition,childPosition);
                return true;
            }
        });
    }

    //长按对话框
    private void showFamilyDialog (final int p, final int a , final int b){
        if (getActivity() == null) {
            return;
        }
        //对话框确定按钮监听器调用方法
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("查看详细信息");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    getPersonInfo(p,a,b);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private class setAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return map.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return map.get(arr[groupPosition]).size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return map.get(arr[groupPosition]);
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
        //父选项
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_dw,null);
            TextView tv= (TextView) view.findViewById(R.id.tv_dw);
            tv.setText(arr[groupPosition]);
            ImageView image= (ImageView) view.findViewById(R.id.image_wd);

            // 根据当前父条目的展开状态来设置不同的图片
            if (isExpanded) {
                // 条目展开，设置向下的箭头
                image.setImageResource(R.drawable.sj1);
            } else {
                // 条目未展开，设置向上的箭头
                image.setImageResource(R.drawable.sj);
            }
            return view;
        }

        //子选项
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v=null;
            GroupHolder groupholder=null;
            if (convertView !=null){
                v=convertView;
                groupholder = (GroupHolder) v.getTag();
            }else {
                v=LayoutInflater.from(getActivity()).inflate(R.layout.item_item_family_info_lv,null);
                groupholder = new GroupHolder();
//              groupholder.head = (ImageView) v.findViewById(R.id.iv_item_item_family_info_head);
                groupholder.name= (TextView) v.findViewById(R.id.tv_item_item_family_info_name);
                groupholder.sex = (TextView) v.findViewById(R.id.tv_item_item_family_info_sex);
                groupholder.birthday = (TextView) v.findViewById(R.id.tv_item_item_family_info_birthday);
                groupholder.idCard = (TextView) v.findViewById(R.id.tv_item_item_family_info_idcard);
                v.setTag(groupholder);
            }
            groupholder.name.setText((CharSequence) map.get(arr[groupPosition]).get(childPosition).getXM());
            groupholder.sex.setText(map.get(arr[groupPosition]).get(childPosition).getGENDER());
            groupholder.birthday.setText(map.get(arr[groupPosition]).get(childPosition).getCSDATE1().split("T")[0]);
            groupholder.idCard.setText(map.get(arr[groupPosition]).get(childPosition).getZJHM());
            return v;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    //子组件Holder
    static class GroupHolder{
        ImageView head;
        TextView name;
        TextView  sex;
        TextView birthday;
        TextView idCard;
    }

    //长按确定获取的数据
    private void  getPersonInfo(final int p, final int a , int b){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url=null;
                if (a==0){
                     url=MyOkHttpUtils.BaseUrl+"/Json/Get_BASIC_INFORMATION.aspx?sfz="+hujiData.get(p).getZJHM();
                }else if (a==1){

                     url=MyOkHttpUtils.BaseUrl+"/Json/Get_BASIC_INFORMATION.aspx?sfz="+lianxiData.get(p).getZJHM();
                }

//                Log.e("长按获取数据",url);
                Response response=MyOkHttpUtils.okHttpGet(url);
                Message msg=Message.obtain();
                if(response!=null){

                    try {
                        String resStr=response.body().string();
                        if(!TextUtils.equals(resStr,"")&&!TextUtils.equals(resStr,"[]")) {
                            Gson gson = new Gson();
                            msg.obj = gson.fromJson(resStr, new TypeToken<List<PersonListInfo>>() {}.getType());
                            msg.what = SUCCESS_PINFO;
                        }else{
                            msg.what = SUCCESS_NODATA;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    msg.what=PROBLEM;
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    // 请求户籍地址
    private void networkData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //户籍地址
                //http://web.youli.pw:8088/json/Get_HJDZ.aspx?sfz=000102199604061613
                String url = MyOkHttpUtils.BaseUrl + "/Json/Get_HJDZ.aspx?sfz=" + pInfo.getZJHM();
                Response response = MyOkHttpUtils.okHttpGet(url);
                Log.e("户籍地址：", "" + response);

                Message msg = Message.obtain();
                if (response != null) {
                    try {
                        String resStr = response.body().string();
                        if (!TextUtils.equals(resStr, "") && !TextUtils.equals(resStr, "[]")) {
                            Gson gson = new Gson();
                            msg.obj = gson.fromJson(resStr, new TypeToken<List<HouseholdInfo>>() {
                            }.getType());
                            msg.what = SUCCESS_HUJI;
                        } else {
                            msg.what = SUCCESS_NODATA;
                        }
                        mHandler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    msg.what = PROBLEM;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    // 获取现住地址
    private void getJuzhuInfo() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        //现住地址
                        //http://web.youli.pw:8088/json/Get_LXDZ.aspx?sfz=000102199604061613
                        String url = MyOkHttpUtils.BaseUrl + "/Json/Get_LXDZ.aspx?sfz=" + pInfo.getZJHM();
                        Response response = MyOkHttpUtils.okHttpGet(url);
                        Log.e("现住地址:", "" + response);
                        Message msg = Message.obtain();
                        if (response != null) {
                            try {
                                String resStr = response.body().string();
                                if (!TextUtils.equals(resStr, "") && !TextUtils.equals(resStr, "[]")) {
                                    Gson gson = new Gson();
                                    msg.obj = gson.fromJson(resStr, new TypeToken<List<HouseholdInfo>>() {
                                    }.getType());
                                    msg.what = SUCCESS_JUZHU;
                                } else {
                                    msg.what = SUCCESS_NODATA;
                                }
                                mHandler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            msg.what = PROBLEM;
                            mHandler.sendMessage(msg);
                        }
                    }
                }
        ).start();
    }

    public void Datas(){
        ProgressDialogUtils.showMyProgressDialog(getActivity());
        networkData();
        getJuzhuInfo();
    }


}

