package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.CommonAdapter;
import com.youli.zbetuch_huangpu.entity.AdminInfo;
import com.youli.zbetuch_huangpu.entity.CommonViewHolder;
import com.youli.zbetuch_huangpu.entity.ResourcesInfo;
import com.youli.zbetuch_huangpu.entity.StreetInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by sfhan on 2017/11/7.
 */

public class ZiyuandiaochaActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context mContext = ZiyuandiaochaActivity.this;
    private List<ResourcesInfo> RInfoList = new ArrayList<>();
    private ListView rInfoLv;
    private Button queryBtn;
    private TextView noDataTv;
    private CommonAdapter commonAdapter;
    private ArrayAdapter<String> typeAdapter;
    private Spinner typeSpinner;
    private String[] typeItems = {"全部", "失业", "无业", "应届生"};
    private ArrayAdapter<StreetInfo> streetAdapter;   //街道信息
    private Spinner streetSpinner;

    private String typeStr;
    private String streetId;

    private final int SUCCESS = 10000;
    private final int PROBLEM = 10001;
    private final int OVERTIME=10002;//登录超时
    public final static int RequestCode=111111;
    public final static int ResultCode=222222;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SUCCESS:
                    if (!((List<ResourcesInfo>) msg.obj).isEmpty()) {
                        noDataTv.setVisibility(View.GONE);
                        rInfoLv.setVisibility(View.VISIBLE);
                        lvSetAdapter((List<ResourcesInfo>) msg.obj);
                    } else {
                        noDataTv.setVisibility(View.VISIBLE);
                        rInfoLv.setVisibility(View.GONE);
                    }
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
        setContentView(R.layout.activity_ziyuandiaocha);

        initViews();

    }

    private void initViews() {

        noDataTv = (TextView) findViewById(R.id.ziyuandiaocha_tv_nodata);
        queryBtn = (Button) findViewById(R.id.ziyuandiaocha_query_btn);
        queryBtn.setOnClickListener(this);
        rInfoLv = (ListView) findViewById(R.id.ziyuandiaocha_lv);
        rInfoLv.setOnItemClickListener(this);

        typeSpinner = (Spinner) findViewById(R.id.ziyuandiaocha_type_spinner);

        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeItems);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeStr = typeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        streetSpinner = (Spinner) findViewById(R.id.ziyuandiaocha_street_spinner);
        streetAdapter = new ArrayAdapter<StreetInfo>(this, android.R.layout.simple_spinner_item, new StreetInfo().getSteetList());
        streetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        streetSpinner.setAdapter(streetAdapter);
        streetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // streetId = getStreetId(position);
                streetId =streetSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getNetData(typeStr, streetId);

    }

    private String getStreetId(int p) {

        List<StreetInfo> list = new StreetInfo().getSteetList();

        StreetInfo info = null;

        for (StreetInfo sInfo : list) {

            if (sInfo.getStreetName().equals(list.get(p).getStreetName())) {
                info = sInfo;
            }

        }
        return info.getStreetId();
    }

    private void getNetData(String type, String street) {

        if (type == null) {
            type = "全部";
        }
        if (street == null) {
            street = "全部";
        }
        final String finalType = type;
        final String finalStreet = street;
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        String url = null;

                        if (TextUtils.equals(finalType, "全部")) {
                            url = MyOkHttpUtils.BaseUrl + "/Json/GetResourceSurvey.aspx?STREET=" + finalStreet + "&page=0&rows=15";
                        } else {
                            url = MyOkHttpUtils.BaseUrl + "/Json/GetResourceSurvey.aspx?STREET=" + finalStreet + "&TYPE=" + typeStr + "&page=0&rows=15";
                        }
                        Log.e("2017/11/8","url="+url);
                        Response response = MyOkHttpUtils.okHttpGet(url);

                        try {
                            Message msg = Message.obtain();
                            if (response != null) {

                                if (response.body() != null) {
                                    String infoStr = response.body().string();

                                    Log.e("2017/11/8","infoStr="+infoStr);

                                    if (!TextUtils.equals(infoStr, "")) {

                                        Gson gson = new Gson();
                                        try {
                                            RInfoList = gson.fromJson(infoStr, new TypeToken<List<ResourcesInfo>>() {}.getType());

                                            msg.what = SUCCESS;
                                            msg.obj = RInfoList;
                                            mHandler.sendMessage(msg);
                                        } catch (Exception e) {

                                            Log.e("2017/11/13","登录超时了");
                                            msg.what=OVERTIME;
                                            mHandler.sendMessage(msg);

                                        }
                                    }
                                } else {
                                    sendProblemMessage(msg);
                                }


                            } else {
                                sendProblemMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).start();

    }

    private void lvSetAdapter(final List<ResourcesInfo> data) {

        commonAdapter = null;

        commonAdapter = new CommonAdapter<ResourcesInfo>(this, data, R.layout.item_ziyuandiaocha_lv) {

            @Override
            public void convert(CommonViewHolder holder, ResourcesInfo item, int position) {

                TextView numTv = holder.getView(R.id.item_ziyuandiaocha_number_tv);
                numTv.setText(position + 1 + "");
                TextView typeTv = holder.getView(R.id.item_ziyuandiaocha_type_tv);
                typeTv.setText(data.get(position).getDclx());//本次调查类型(失业、无业、应届生)
                TextView nameTv = holder.getView(R.id.item_ziyuandiaocha_operator_tv);
                nameTv.setText(data.get(position).getDccjrxm());//本次调查创建人姓名
                TextView timeTv = holder.getView(R.id.item_ziyuandiaocha_time_tv);
                timeTv.setText(data.get(position).getBzdate().split("T")[0]);//本次调查布置日期
                TextView xuchaTv = holder.getView(R.id.item_ziyuandiaocha_needNum_tv);
                xuchaTv.setText(data.get(position).getDcmdrs() + "");//本次调查名单人数
                TextView yichaTv = holder.getView(R.id.item_ziyuandiaocha_alreadyNum_tv);
                yichaTv.setText(data.get(position).getDcwcrs() + "");////要显示的
                LinearLayout ll = holder.getView(R.id.item_ziyuandiaocha_ll);
//                if (position % 2 != 0) {
//                    ll.setBackgroundResource(R.drawable.selector_ziyuandiaocha_item1);
//                } else {
//                    ll.setBackgroundResource(R.drawable.selector_ziyuandiaocha_item2);
//                }
                if (position % 2 == 0){
                    ll.setBackgroundResource(R.drawable.selector_questionnaire_click_blue);
                }else {
                    ll.setBackgroundResource(R.drawable.selector_questionnaire_click_white);
                }
            }
        };

        rInfoLv.setAdapter(commonAdapter);

    }


    private void sendProblemMessage(Message msg) {
        msg.what = PROBLEM;
        mHandler.sendMessage(msg);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ziyuandiaocha_query_btn:
                getNetData(typeStr, streetId);
                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent=new Intent(this,ZiyuanDetailListActivity.class);
        intent.putExtra("RInfo",RInfoList.get(position));

        startActivityForResult(intent,RequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RequestCode && resultCode == ResultCode) {

            getNetData(typeStr, streetId);


        }
    }
}

