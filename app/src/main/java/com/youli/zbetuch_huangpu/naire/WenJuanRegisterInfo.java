package com.youli.zbetuch_huangpu.naire;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.BaseActivity;
import com.youli.zbetuch_huangpu.activity.HomePageActivity;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;
import com.youli.zbetuch_huangpu.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class WenJuanRegisterInfo extends BaseActivity implements OnClickListener{


	private final int SUBMIT_ADDRESS=10001;
	private final int PROBLEM = 10002;

	private Button backBtn,nextBtn;
	
	private WenJuanPersonInfo info;
	private int position;
	private LinearLayout sixteenLl;
	private EditText nameEt,areaEt,streetEt,villageEt,addressEt,personEt,manEt,womanEt,sixteenEt,contactsEt,interviewerEt,phoneEt;
	private String nameStr,areaStr,streetStr,villageStr,addressStr,personStr,manStr,womanStr,sixteenStr,contactsStr,interviewerStr,phoneStr;
	private TextView addressTv,contactsTv,interviewerTv,phoneTv;
	public static String registerInfoUrl="/Json/Set_Home.aspx";
	private String familyListUrl="/Json/Get_Home.aspx";
	private int myHOMEID;
	private IActivity activity = null;
	private List<FamilyInfo> listInfo=new ArrayList<FamilyInfo>();
	private FamilyInfo fInfo;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {


				case SUBMIT_ADDRESS://提交经纬度
					addRefresh();
					break;

				case PROBLEM:

					Toast.makeText(WenJuanRegisterInfo.this, "提交失败", Toast.LENGTH_SHORT).show();

					break;


			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wenjuan_register_info);
		
		info = (WenJuanPersonInfo) getIntent().getSerializableExtra("info");
		fInfo=(FamilyInfo) getIntent().getSerializableExtra("familyList");

		position = getIntent().getIntExtra("position", 0);
		backBtn=(Button) findViewById(R.id.awri_btn_back);
		backBtn.setOnClickListener(this);
		nextBtn=(Button) findViewById(R.id.awri_btn_next);
		nextBtn.setOnClickListener(this);
		
		nameEt=(EditText) findViewById(R.id.awri_et_name);//姓名
		
		areaEt=(EditText) findViewById(R.id.awri_et_area);//区
		
		streetEt=(EditText) findViewById(R.id.awri_et_street);//街道
		
		villageEt=(EditText) findViewById(R.id.awri_et_village);//居委会
		addressTv=(TextView) findViewById(R.id.awri_tv_address);
		addressEt=(EditText) findViewById(R.id.awri_et_address);//具体地址
		
		personEt=(EditText) findViewById(R.id.awri_et_person);//实际人数
	    personEt.requestFocus();
		manEt=(EditText) findViewById(R.id.awri_et_man);//男
		
		womanEt=(EditText) findViewById(R.id.awri_et_woman);//女
		
		sixteenEt=(EditText) findViewById(R.id.awri_et_16);//16周岁以上
		sixteenLl=(LinearLayout) findViewById(R.id.awri_ll_sixteen);
		contactsEt=(EditText) findViewById(R.id.awri_et_contacts);//访员签名（调查员）
		contactsEt.setText(HomePageActivity.adminName);
		contactsTv=(TextView) findViewById(R.id.awri_tv_contacts);
		interviewerEt=(EditText) findViewById(R.id.awri_et_interviewer);//联系人（申请人）
		interviewerTv=(TextView) findViewById(R.id.awri_tv_interviewer);
		phoneEt=(EditText) findViewById(R.id.awri_et_phone);//联系电话
		phoneTv=(TextView) findViewById(R.id.awri_tv_phone);
		if(getIntent().getIntExtra("QUESTIONMASTERID", 0)==5){
			addressTv.setText("本户地址:");
			contactsTv.setText("调查员:");
			interviewerTv.setText("申请人:");
			phoneTv.setText("本户电话:");
			sixteenLl.setVisibility(View.GONE);
		}else if(getIntent().getIntExtra("QUESTIONMASTERID", 0)==6){
			addressTv.setText("访问地址:");
			contactsTv.setText("访员签名:");
			interviewerTv.setText("联系人:");
			phoneTv.setText("联系电话:");
			sixteenLl.setVisibility(View.VISIBLE);
		}
		
		if(fInfo!=null){
			nameEt.setText(fInfo.getNAME());
			areaEt.setText(fInfo.getXSQ());
			villageEt.setText(fInfo.getSQJWC());
			streetEt.setText(fInfo.getXZJD());
			addressEt.setText(fInfo.getADRESS());
			phoneEt.setText(fInfo.getLXDF());
			
			if(TextUtils.isEmpty(fInfo.getSQR())){			
				interviewerEt.requestFocus();
			}else{
				
				if(TextUtils.equals(fInfo.getSQR(),"null")){
					interviewerEt.setText("");
				}else{
					interviewerEt.setText(fInfo.getSQR());
					
				}
				
			}	
			
			if(TextUtils.isEmpty(fInfo.getLXR())){
				contactsEt.requestFocus();
			}else{
				
				if(TextUtils.equals(fInfo.getLXR(),"null")){
					contactsEt.setText("");
				}else{
				contactsEt.setText(fInfo.getLXR());
			}			
			}
			
			if(fInfo.getZS()==0&&fInfo.getNV()==0&&fInfo.getNAN()==0&&fInfo.getJZNUM()==0){
				
				sixteenEt.setText("");		
				womanEt.setText("");		
				manEt.setText("");		
				personEt.setText("");
				personEt.requestFocus();
			}else{
			

				sixteenEt.setText(fInfo.getZS()+"");		

				womanEt.setText(fInfo.getNV()+"");		

				manEt.setText(fInfo.getNAN()+"");		

				personEt.setText(fInfo.getJZNUM()+"");	
				personEt.clearFocus();

			}
		}else{
			
			nameEt.setText(info.getNAME());
			interviewerEt.setText(info.getNAME());
			areaEt.setText(info.getQX());
			villageEt.setText(info.getJW());
			streetEt.setText(info.getJD());
			addressEt.setText(info.getLXDZ());
			phoneEt.setText(info.getPHONE());
			getHuzhuInfo();
		}
		
	}

	private void getHuzhuInfo() {
		// http://192.168.11.11:89/Json/Get_Home.aspx?TYPE=1&QA_MASTER=5&SFZ=310108197604155814
		OkHttpUtils.post().url(MyOkHttpUtils.BaseUrl+familyListUrl).addParams("TYPE","1").addParams("QA_MASTER", getIntent().getIntExtra("QUESTIONMASTERID", 0)+"")
		.addParams("SFZ",info.getSFZ()).build().execute(new StringCallback() {
			
			@Override
			public void onResponse(final String infoStr) {
				
				if(!TextUtils.equals(infoStr, "[]")){

					runOnUiThread(new  Runnable() {
						public void run() {
							
							Gson gson=new Gson();
							Type listType=new TypeToken<LinkedList<FamilyInfo>>(){}.getType();
							
							LinkedList<FamilyInfo> fi=gson.fromJson(infoStr,listType);
							
							listInfo.clear();
							
							for (Iterator iterator = fi.iterator(); iterator
									.hasNext();) {

								FamilyInfo content = (FamilyInfo) iterator
										.next();

								listInfo.add(content);
								
							}
							
							if(!TextUtils.isEmpty(personEt.getText().toString().trim())){
								contactsEt.requestFocus();
							}			
							
							
							if(listInfo.get(0).getZS()==0&&listInfo.get(0).getNV()==0&&listInfo.get(0).getNAN()==0&&listInfo.get(0).getJZNUM()==0){
								
								sixteenEt.setText("");
								womanEt.setText("");
								manEt.setText("");
								personEt.setText("");
								personEt.requestFocus();
							}else{

								sixteenEt.setText(listInfo.get(0).getZS()+"");		

								womanEt.setText(listInfo.get(0).getNV()+"");		

								manEt.setText(listInfo.get(0).getNAN()+"");		

								personEt.setText(listInfo.get(0).getJZNUM()+"");
								personEt.clearFocus();

							}
						}
					});
					
				
				}
				
			}
			
			@Override
			public void onError(Call arg0, Exception arg1) {
				Toast.makeText(WenJuanRegisterInfo.this,"网络不给力",Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		nameStr=nameEt.getText().toString().trim();
		areaStr=areaEt.getText().toString().trim();
		streetStr=streetEt.getText().toString().trim();
		villageStr=villageEt.getText().toString().trim();
		addressStr=addressEt.getText().toString().trim();
		personStr=personEt.getText().toString().trim();
		manStr=manEt.getText().toString().trim();
		womanStr=womanEt.getText().toString().trim();
		sixteenStr=0+sixteenEt.getText().toString().trim();
		contactsStr=contactsEt.getText().toString().trim();
		interviewerStr=interviewerEt.getText().toString().trim();
		phoneStr=phoneEt.getText().toString().trim();
		switch (v.getId()) {
		case R.id.awri_btn_back:
			
			
			if(getIntent().getIntExtra("QUESTIONMASTERID", 0)==5){
				
				if(TextUtils.isEmpty(nameStr)||
				TextUtils.isEmpty(areaStr)||
				TextUtils.isEmpty(streetStr)||
				TextUtils.isEmpty(villageStr)||
				TextUtils.isEmpty(addressStr)||
				TextUtils.isEmpty(personStr)||
				TextUtils.isEmpty(manStr)||
				TextUtils.isEmpty(womanStr)||
				TextUtils.isEmpty(contactsStr)||
				TextUtils.isEmpty(interviewerStr)||
				TextUtils.isEmpty(phoneStr)){
		
			Toast.makeText(WenJuanRegisterInfo.this,"请将资料信息填写完整", Toast.LENGTH_SHORT).show();
		
			return;
		}
				
			} else if(getIntent().getIntExtra("QUESTIONMASTERID", 0)==6){
				
				if(TextUtils.isEmpty(nameStr)||
				TextUtils.isEmpty(areaStr)||
				TextUtils.isEmpty(streetStr)||
				TextUtils.isEmpty(villageStr)||
				TextUtils.isEmpty(addressStr)||
				TextUtils.isEmpty(personStr)||
				TextUtils.isEmpty(manStr)||
				TextUtils.isEmpty(womanStr)||
				TextUtils.isEmpty(sixteenStr)||
				TextUtils.isEmpty(contactsStr)||
				TextUtils.isEmpty(interviewerStr)||
				TextUtils.isEmpty(phoneStr)){
		
			Toast.makeText(WenJuanRegisterInfo.this,"请将资料信息填写完整", Toast.LENGTH_SHORT).show();
		
			return;
		}
				
			}
			
				registerInfo();
			
			setResult(10000);
			
			finish();
			
			break;

case R.id.awri_btn_next:

	showDialog();
	
			break;	
			
		default:
			break;
		}
		
	}

private void showDialog(){
		
		Builder builder = new Builder(this);
		builder.setTitle("温馨提示").setMessage("您确定要取消答题吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create().show();
		
	}
	
	private void registerInfo(){
		//ZS代表16周岁 int
		String cookies = SharedPreferencesUtils.getString("cookie");
		int Type;
		int Id;
		if(fInfo==null){
			
			Type=1;
			Id=info.getID();
		}else{
			Id=fInfo.getID();
			Type=3;
			
		}

		Log.e("2017/11/29","家庭成员=="+MyOkHttpUtils.BaseUrl+registerInfoUrl+"?TYPE="+Type+"&NAME="+info.getNAME()
		+"&SFZ="+info.getSFZ()+"&LXR="+contactsStr+"&LXDF="+phoneStr+"&JZNUM="+personStr+"&NAN="+manStr+"&NV="+womanStr
		+"&ZS="+sixteenStr+"&ID="+Id+"&XSQ="+areaStr+"&XZJD="+streetStr+"&SQJWC="+villageStr+"&SQR="+interviewerStr+"&ADRESS="+addressStr
		+"&QUESTIONMASTERID="+getIntent().getIntExtra("QUESTIONMASTERID", 0));

		OkHttpUtils.post().url(MyOkHttpUtils.BaseUrl+registerInfoUrl).addParams("TYPE",Type+"").addParams("NAME",info.getNAME())
		.addParams("SFZ",info.getSFZ()).addParams("LXR",contactsStr).addParams("LXDF",phoneStr)
		.addParams("JZNUM",personStr+"").addParams("NAN",manStr+"").addParams("NV",womanStr+"").addParams("ZS",sixteenStr+"").addParams("ID",Id+"")
		.addParams("XSQ",areaStr).addParams("XZJD",streetStr).addParams("SQJWC",villageStr).addParams("SQR",interviewerStr)
		.addParams("ADRESS",addressStr).addParams("QUESTIONMASTERID",getIntent().getIntExtra("QUESTIONMASTERID", 0)+"")
		.addHeader("cookie", cookies)
		.build().execute(new StringCallback() {
			
			@Override
			public void onResponse(final String str) {

				submitInfo();//提交经纬度
			}
			
			@Override
			public void onError(Call arg0, Exception arg1) {
				Toast.makeText(WenJuanRegisterInfo.this,"网络不给力",Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void submitInfo() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				String	url = MyOkHttpUtils.BaseUrl + "/Json/Set_GPS_Staff_Log.aspx?sfz="+info.getSFZ()+"&detail=专项调查中的登记人数&gps="+ HomePageActivity.jDuStr+","+HomePageActivity.wDuStr;

				Log.e("2017-12-18","专项调查中的登记人数url="+url);

				Response response = MyOkHttpUtils.okHttpGet(url);

				try {
					Message msg = Message.obtain();
					if (response != null) {
						String resStr = response.body().string();
						Log.e("2017/11/9", "提交==" + resStr);

						if (TextUtils.equals("True", resStr)) {
							msg.what = SUBMIT_ADDRESS;

						}else{
							msg.what = PROBLEM;
						}


					} else {
						msg.what = PROBLEM;

					}

					mHandler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}


private void addRefresh(){
	String cookies = SharedPreferencesUtils.getString("cookie");
	int myTypeId;
		
		if(WenJuanPersonActivity.isWeichaRg){
			myTypeId=1;
		}else{
			myTypeId=0;
		}
		
		OkHttpUtils.post().url(MyOkHttpUtils.BaseUrl+"/Json/Get_Qa_UpLoad_Personnel.aspx").addParams("type",""+myTypeId).addParams("page","0").addParams("rows","15").addParams("master_id",getIntent().getIntExtra("QUESTIONMASTERID", 1)+"").addHeader("cookie", cookies)
		.build().execute(new StringCallback() {
			
			@Override
			public void onResponse(String str) {

				activity = (WenJuanPersonActivity) PersonService
				.getActivityByName("WenJuanPersonActivity");
	
		if (activity != null) {

		activity.refresh(WenJuanPersonActivity.REFRESH_INFO,
				str);
			}
			}
			@Override
			public void onError(Call arg0, Exception arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(WenJuanRegisterInfo.this,"网络不给力",Toast.LENGTH_SHORT).show();
			}
		
		});
		
	}

}


