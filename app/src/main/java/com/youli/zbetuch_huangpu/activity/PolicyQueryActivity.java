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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.adapter.PolicyQueryAdapter;
import com.youli.zbetuch_huangpu.entity.MeetInfo;
import com.youli.zbetuch_huangpu.entity.PolicyQueryInfo;
import com.youli.zbetuch_huangpu.entity.PolicyTypeInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * 作者: zhengbin on 2017/12/15.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 *
 * 政策查询界面
 */

public class PolicyQueryActivity extends BaseActivity implements View.OnClickListener{


    private Context mContext=this;

    private final int SUCCEED=10001;
    private final int SUCCEED_POLICY=10002;
    private final int SUCCEED_NODATA=10003;
    private final int  PROBLEM=10004;
    private final int OVERTIME=10005;//登录超时

    private Spinner spinner;
    private EditText et;
    private Button btn;

    private List<PolicyTypeInfo> ptData=new ArrayList<>();//政策类别
    private ArrayAdapter<String> typeAdapter;

    private List<PolicyQueryInfo> queryInfo=new ArrayList<>();//政策列表的数据
    private ExpandableListView elv;
    private PolicyQueryAdapter adapter;

    private   String typeStr;//政策类别的内容

    private  String codeStr;//关键字的内容

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case SUCCEED:

                    ptData.clear();
                    ptData.addAll((List<PolicyTypeInfo>)msg.obj);
                    spSetAdapter(ptData);
                    btn.setEnabled(true);

                    break;

                case SUCCEED_POLICY:
                    queryInfo.clear();
                    queryInfo.addAll((List<PolicyQueryInfo>)msg.obj);

                    fretchElv(queryInfo);
                    elv.setVisibility(View.VISIBLE);

                    break;

                case PROBLEM:
                    Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();

                    break;
                case SUCCEED_NODATA:
                    elv.setVisibility(View.GONE);

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
        setContentView(R.layout.activity_policy_query);


        initViews();
    }


    private void initViews(){

        spinner= (Spinner) findViewById(R.id.policy_query_spinner);
        et= (EditText) findViewById(R.id.policy_query_et);
        btn= (Button) findViewById(R.id.policy_query_btn);
        btn.setOnClickListener(this);
        elv= (ExpandableListView) findViewById(R.id.policy_query_elv);

        initDatas("spinner");
    }

    private void initDatas(final String mark){


        new Thread(


                new Runnable() {
                    @Override
                    public void run() {

                        String url = null;

                        if(TextUtils.equals(mark,"spinner")){
                            //http://web.youli.pw:8088/Json/Get_policy_Type.aspx
                            url= MyOkHttpUtils.BaseUrl+"/Json/Get_policy_Type.aspx";
                        }else if(TextUtils.equals(mark,"policy")){
                           // http://web.youli.pw:8088/Json/Get_QUESTION_ANSWER.aspx?type=失业保险&code=失业
                            url= MyOkHttpUtils.BaseUrl+"/Json/Get_QUESTION_ANSWER.aspx?type="+typeStr+"&code="+codeStr;
                        }

                        Log.e("2017-12-19","url=="+url);

                        Response response=MyOkHttpUtils.okHttpGet(url);

                        Message msg=Message.obtain();

                        if(response!=null){

                            try {
                                String str=response.body().string();

                                if(!TextUtils.equals(str,"")&&!TextUtils.equals(str,"[]")){

                                    Gson gson=new Gson();
                                    if(TextUtils.equals(mark,"spinner")) {
                                        msg.obj = gson.fromJson(str, new TypeToken<List<PolicyTypeInfo>>() {
                                        }.getType());
                                        msg.what=SUCCEED;
                                    }else if(TextUtils.equals(mark,"policy")){
                                        msg.obj = gson.fromJson(str, new TypeToken<List<PolicyQueryInfo>>() {
                                        }.getType());
                                        msg.what=SUCCEED_POLICY;
                                    }



                                }else{
                                    msg.what=SUCCEED_NODATA;
                                }

                                Log.e("2017-12-19","str="+str);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("2017-12-19","登录超时了");
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


    private void spSetAdapter(List<PolicyTypeInfo> list){

         List<String> typeList=new ArrayList<>();

        for(PolicyTypeInfo infoList:list){

            typeList.add(infoList.getTYPE_NAME());

        }

        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(typeAdapter);

        for(int i=0;i<typeList.size();i++){

            if(TextUtils.equals("劳动力资源",typeList.get(i))){
                spinner.setSelection(i);
                break;
            }



        }

        typeStr=spinner.getSelectedItem().toString();
        codeStr="";
        initDatas("policy");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.policy_query_btn:
                typeStr=spinner.getSelectedItem().toString();
                codeStr=et.getText().toString().trim();

               // Toast.makeText(mContext,"选中的是"+typeStr+"==="+codeStr,Toast.LENGTH_SHORT).show();
                initDatas("policy");
                break;

        }

    }

    private void fretchElv(List<PolicyQueryInfo> list){

        List<Map<String,String>> questions=new ArrayList<>();
        List<List<Map<String,String>>> answers=new ArrayList<>();

        for(int i=0;i<list.size();i++){

            String question=list.get(i).getQUESTIONS();
            String answer=list.get(i).getANSWERS();

            Map<String,String> questionMap=new HashMap<>();
            questionMap.put("question", question);
            questions.add(questionMap);

            List<Map<String,String>> answerList=new ArrayList<>();
            Map<String,String> answerMap=new HashMap<>();
            answerMap.put("answer", answer);
            answerList.add(answerMap);
            answers.add(answerList);
        }

        adapter=new PolicyQueryAdapter(questions,answers,this);
        elv.setAdapter(adapter);
//        int groupCount = elv.getCount();
//        for (int i=0; i<groupCount; i++) {
//            elv.expandGroup(i);
//        }

        //elv.expandGroup(0);//设置默认展开第一项
//        final int groupCount = elv.getCount();

    }

}
