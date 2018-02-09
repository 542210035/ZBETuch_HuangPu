package com.youli.zbetuch_huangpu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.MeetPersonInfo;

import java.util.List;


/**
 * 作者: zhengbin on 2017/12/7.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 */

public class ChooseMeetPersonAdapter extends BaseAdapter{

    private List<MeetPersonInfo> data;
    private Context context;
    private CheckBox cb;

    public ChooseMeetPersonAdapter(Context context, List<MeetPersonInfo> data,CheckBox cb) {
        this.context = context;
        this.data = data;
        this.cb=cb;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;

        if(convertView==null){

            vh=new ViewHolder();

            convertView= LayoutInflater.from(context).inflate(R.layout.item_choose_meet_person,parent,false);

            vh.tv= (TextView) convertView.findViewById(R.id.item_tv);

            vh.cb= (CheckBox) convertView.findViewById(R.id.item_cb);

            convertView.setTag(vh);
        }else{

            vh= (ViewHolder) convertView.getTag();

        }

        final MeetPersonInfo c=data.get(position);

        vh.tv.setText(c.getName());
        vh.cb.setChecked(c.isCheck());
        vh.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.setCheck(!c.isCheck());
                vh.cb.setChecked(c.isCheck());
                notifyDataSetChanged();

                cb.setChecked(setCbCheck());

            }
        });

        return convertView;
    }

    class ViewHolder{

        TextView tv;
        CheckBox cb;

    }

    private boolean setCbCheck(){

        for(MeetPersonInfo c:data){

            if(!c.isCheck()){

                return  false;

            }

        }

        return true;

    }

}
