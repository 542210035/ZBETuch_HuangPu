package com.youli.zbetuch_huangpu.naire;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.BaseActivity;
import com.youli.zbetuch_huangpu.activity.HomePageActivity;
import com.youli.zbetuch_huangpu.activity.ZiyuanDetailListActivity;
import com.youli.zbetuch_huangpu.entity.PersonInfo;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class ShowWenJuanMarkActivity extends BaseActivity implements IActivity,
		OnClickListener, OnCheckedChangeListener {
	private EditText mark_text;
	private Button button_true;
	private Button button_false;
	private int position;
	private RadioGroup group;
	private RadioButton btn_chuguo, btn_renhufenli, btn_dianhuayouwu,
			btn_wurenjieting, btn_qita;
	private String reason;
	private String reason_edit;

	private String sfz;

	private final int SUBMIT_ADDRESS=10001;
	private final int PROBLEM = 10002;




	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {


				case SUBMIT_ADDRESS://提交经纬度
					ShowWenJuanMarkActivity.this.finish();
					break;

				case PROBLEM:

					Toast.makeText(ShowWenJuanMarkActivity.this, "提交失败", Toast.LENGTH_SHORT).show();

					break;


			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wenjuan_mark);

		sfz=getIntent().getStringExtra("sfz");

		init();
		initView();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		PersonService.addActivity(this);
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub

	}

	private void initView() {

		mark_text = (EditText) this.findViewById(R.id.et_mark);
		button_true = (Button) this.findViewById(R.id.btn_true);
		button_false = (Button) this.findViewById(R.id.btn_false);
		group = (RadioGroup) findViewById(R.id.rgp_chuguo);
		btn_chuguo = (RadioButton) findViewById(R.id.btn_chuguo);
		btn_dianhuayouwu = (RadioButton) findViewById(R.id.btn_dianhuacuowu);
		btn_qita = (RadioButton) findViewById(R.id.btn_qita);
		btn_renhufenli = (RadioButton) findViewById(R.id.btn_renhufenli);
		button_true.setOnClickListener(this);
		button_false.setOnClickListener(this);
		group.setOnCheckedChangeListener(this);
		mark_text.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_true:
			reason_edit=mark_text.getText().toString().trim();
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, String> data = new HashMap<String, String>();
			if (reason == null&&(reason_edit.equals(""))) {
				Toast.makeText(ShowWenJuanMarkActivity.this, "输入不能为空",
						Toast.LENGTH_SHORT).show();
			
			} else if(reason == null&&!(reason_edit.equals(""))){
				data.put("Personnel_id", getIntent().getStringExtra("pid")
						.toString().trim());
				data.put("mark", reason_edit);
				data.put("position", getIntent().getIntExtra("position", 0)
						+ "");
				params.put("data", data);
				PersonTask task = new PersonTask(
						PersonTask.UPLOADWENJUAN_SET_WENJUAN, params);
				PersonService.newTask(task);

				submitInfo();//提交经纬度

			}else if(reason != null&&(reason_edit.equals("")))
			{
				data.put("Personnel_id", getIntent().getStringExtra("pid")
						.toString().trim());
				data.put("mark", reason);
				data.put("position", getIntent().getIntExtra("position", 0)
						+ "");
				params.put("data", data);
				PersonTask task = new PersonTask(
						PersonTask.UPLOADWENJUAN_SET_WENJUAN, params);
				PersonService.newTask(task);
				submitInfo();//提交经纬度

			}else{
				data.put("Personnel_id", getIntent().getStringExtra("pid")
						.toString().trim());
				data.put("mark", reason);
				data.put("position", getIntent().getIntExtra("position", 0)
						+ "");
				params.put("data", data);
				PersonTask task = new PersonTask(
						PersonTask.UPLOADWENJUAN_SET_WENJUAN, params);
				PersonService.newTask(task);
				submitInfo();//提交经纬度

			}
			break;

		case R.id.btn_false:
			ShowWenJuanMarkActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PersonService.allActivity.remove(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.btn_chuguo:
			reason = (String) btn_chuguo.getText();
			mark_text.setEnabled(false);
			mark_text.setText("");
			break;

		case R.id.btn_dianhuacuowu:
			reason = (String) btn_dianhuayouwu.getText();
			mark_text.setEnabled(false);
			mark_text.setText("");
			break;
		
		case R.id.btn_renhufenli:
			reason = (String) btn_renhufenli.getText();
			mark_text.setEnabled(false);
			mark_text.setText("");
			break;
		case R.id.btn_qita:
			reason=null;
			mark_text.setEnabled(true);
			
			break;
		}
	}


	private void submitInfo() {

		new Thread(new Runnable() {
			@Override
			public void run() {

		String	url = MyOkHttpUtils.BaseUrl + "/Json/Set_GPS_Staff_Log.aspx?sfz="+sfz+"&detail=专项调查中的无法调查&gps="+ HomePageActivity.jDuStr+","+HomePageActivity.wDuStr;

		Response	response = MyOkHttpUtils.okHttpGet(url);

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
}
