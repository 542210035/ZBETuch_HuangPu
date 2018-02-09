package com.youli.zbetuch_huangpu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.activity.OvertimeDialogActivity;
import com.youli.zbetuch_huangpu.entity.MeetPersonInfo;
import com.youli.zbetuch_huangpu.entity.ZhiwuInfo;
import com.youli.zbetuch_huangpu.treeview.Node;
import com.youli.zbetuch_huangpu.treeview.TreeHelper;
import com.youli.zbetuch_huangpu.treeview.TreeListViewAdapter;
import com.youli.zbetuch_huangpu.utils.AlertDialogUtils;
import com.youli.zbetuch_huangpu.utils.MyOkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T>
{

   private Context mContext;

	private  Node myNode;

	private List<MeetPersonInfo>  personData;

	CommonAdapter cAdapter;

	public SimpleTreeListViewAdapter(ListView tree, Context context,
									 List<T> datas, int defaultExpandLevel,List<MeetPersonInfo>  personData,CommonAdapter cAdapter)
			throws IllegalArgumentException, IllegalAccessException
	{
		super(tree, context, datas, defaultExpandLevel);
       mContext=context;
		this.personData=personData;
		this.cAdapter=cAdapter;
	}

	@Override
	public View getConvertView(final Node node, final int position, View convertView,
							   ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_inspector_zhiwu, parent, false);
			holder = new ViewHolder();
			holder.mIcon = (ImageView) convertView
					.findViewById(R.id.id_item_icon);
			holder.mText = (TextView) convertView
					.findViewById(R.id.id_item_text);



			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (node.getIcon() == -1)
		{
			holder.mIcon.setVisibility(View.INVISIBLE);
		} else
		{
			holder.mIcon.setVisibility(View.VISIBLE);
			holder.mIcon.setImageResource(node.getIcon());
		}

		holder.mIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				expandOrCollapse(position);
			}
		});


		holder.mText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//showChooseMeetPerson(node);

				myNode=node;
				getMeetPersonData(node);

			}
		});

		holder.mText.setText(node.getName());

		return convertView;
	}

	private class ViewHolder
	{
		ImageView mIcon;
		TextView mText;
	}

	/**
	 * 动态插入节点
	 * 
	 * @param position
	 * @param string
	 */
	public void addExtraNode(int position, String string)
	{
		Node node = mVisibleNodes.get(position);
		int indexOf = mAllNodes.indexOf(node);
		// Node
		Node extraNode = new Node(-1, node.getId(), string);
		extraNode.setParent(node);
		node.getChildren().add(extraNode);
		mAllNodes.add(indexOf + 1, extraNode);

		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		notifyDataSetChanged();

	}

	//===================================================================================================================================

	//===================================================================================================================================

	private final int SUCCESS_DATA=10001;
	private final int SUCCESS_NO_DATA=10002;
	private final int FAIL=10003;
	private final int OVERTIME=10004;//登录超时

	private List<MeetPersonInfo> pInfo=new ArrayList<>();

	private ChooseMeetPersonAdapter pAdapter;

	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what){

				case SUCCESS_DATA:
					pInfo.clear();
					pInfo.addAll((List<MeetPersonInfo>)(msg.obj));

					showChooseMeetPerson(myNode,pInfo);
					break;

				case SUCCESS_NO_DATA:

					Toast.makeText(mContext,"没有可选人员",Toast.LENGTH_SHORT).show();

					break;

				case OVERTIME:
					Intent i=new Intent(mContext,OvertimeDialogActivity.class);
					mContext.startActivity(i);
					break;

				case FAIL:
					Toast.makeText(mContext,"网络不给力",Toast.LENGTH_SHORT).show();
					break;
			}

		}
	};

	private void getMeetPersonData(final Node node){

		//http://web.youli.pw:8088/Json/Get_Line_Staff.aspx?all=false&ID=2&window=员工待办事宜

		new Thread(


				new Runnable() {
					@Override
					public void run() {

						String url= MyOkHttpUtils.BaseUrl+"/Json/Get_Line_Staff.aspx?all=false&ID="+node.getId()+"&window=员工待办事宜";

						Response response=MyOkHttpUtils.okHttpGet(url);

						Message msg=Message.obtain();

						if(response!=null){

							try {
								String meetStr=response.body().string();

								if(!TextUtils.equals(meetStr,"")&&!TextUtils.equals(meetStr,"[]")){

									Gson gson=new Gson();
									msg.obj=gson.fromJson(meetStr,new TypeToken<List<MeetPersonInfo>>(){}.getType());
									msg.what=SUCCESS_DATA;
								}else{
									msg.what=SUCCESS_NO_DATA;
								}

							} catch (Exception e) {
								e.printStackTrace();
								msg.what=OVERTIME;

							}

						}else{

							msg.what=FAIL;

						}

						mHandler.sendMessage(msg);

					}
				}


		).start();


	}


	private void showChooseMeetPerson(Node node, final List<MeetPersonInfo> list){
		final AlertDialogUtils personAdu=new AlertDialogUtils(mContext,R.layout.dialog_choose_meet_person);

		personAdu.showAlertDialog();

		ListView lv= (ListView) personAdu.getAduView().findViewById(R.id.dialog_choose_meet_person_lv);

		final CheckBox cb= (CheckBox) personAdu.getAduView().findViewById(R.id.dialog_choose_meet_person_cb);

		pAdapter=new ChooseMeetPersonAdapter(mContext,list,cb);

		lv.setAdapter(pAdapter);


		cb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(cb.isChecked()){

					for(MeetPersonInfo c:pInfo){

					c.setCheck(true);

				}

				}else{

					for(MeetPersonInfo c:pInfo){

					c.setCheck(false);

				}

				}
			pAdapter.notifyDataSetChanged();
			}
		});




		Button sureBtn= (Button) personAdu.getAduView().findViewById(R.id.dialog_choose_meet_person_ensure_btn);
		sureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//确定



					for (MeetPersonInfo info : list) {
						if (info.isCheck()) {
							personData.add(info);
							if(personData.size()==0){
								personData.add(info);
							}else{

							for(MeetPersonInfo pInfo:personData) {
								if (pInfo.getStaff_id() != info.getStaff_id()) {

									personData.add(info);
								}
							}
						}
					}

				}
				personAdu.dismissAlertDialog();

				cAdapter.notifyDataSetChanged();
			}
		});

		Button cancelBtn= (Button)personAdu.getAduView().findViewById(R.id.dialog_choose_meet_person_cancel_btn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//取消

				personAdu.dismissAlertDialog();
			}
		});

	}

}
