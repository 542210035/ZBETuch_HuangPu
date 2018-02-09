package com.youli.zbetuch_huangpu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youli.zbetuch_huangpu.R;

import java.util.List;
import java.util.Map;

/**
 * 作者: zhengbin on 2017/12/19.
 * <p>
 * 邮箱:2381447237@qq.com
 * <p>
 * github:2381447237
 */

public  class PolicyQueryAdapter extends BaseExpandableListAdapter{

    private List<Map<String,String>> questions;
    private List<List<Map<String,String>>> answers;
    private Context context;

    public PolicyQueryAdapter(List<Map<String, String>> questions, List<List<Map<String, String>>> answers, Context context) {
        this.questions = questions;
        this.answers = answers;
        this.context = context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return answers.get(groupPosition).get(childPosition).get("answer");
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_policy_query_subitem,null);

        TextView tv= (TextView) view.findViewById(R.id.txtSubItem);
        tv.setText(answers.get(groupPosition).get(childPosition).get("answer"));
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return answers.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return questions.get(groupPosition).get("question");
    }

    @Override
    public int getGroupCount() {
        return questions.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View view=LayoutInflater.from(context).
                inflate(R.layout.item_policy_query_groupitem,null);

        ImageView imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
        TextView txtGroupItem = (TextView) view.findViewById(R.id.txtGroupItem);
        txtGroupItem.setText(questions.get(groupPosition).get("question"));

        if (isExpanded) {
            imgIcon.setImageResource(R.drawable.tree_ex);
        } else {
            imgIcon.setImageResource(R.drawable.tree_ec);
        }

        return view;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
