package com.youli.zbetuch_huangpu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.youli.zbetuch_huangpu.R;

import java.util.List;

/**
 * Created by sfhan on 2017/11/7.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<Integer> data ;
    public GridViewAdapter (List<Integer> iconList){
        data = iconList;
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item_layout,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_function);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(data.get(position));
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
    }
}