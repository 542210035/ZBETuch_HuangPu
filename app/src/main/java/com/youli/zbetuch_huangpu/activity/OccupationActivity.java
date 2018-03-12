package com.youli.zbetuch_huangpu.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.youli.zbetuch_huangpu.R;

public class OccupationActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context mContext=OccupationActivity.this;
    private GridView gridView;
    private Intent intent;
    private int [] itemImage=new int[]{
            R.drawable.zpgl
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupation);
        initView();
    }

    private void initView(){
        gridView= (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new adapterr());
        gridView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
                intent=new Intent(mContext,RecruitActivity.class);//招聘管理
                startActivity(intent);
                break;

        }
    }

    private class adapterr extends BaseAdapter {
        @Override
        public int getCount() {
            return itemImage.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.gr_adapter_content,null);
            ImageView iv= (ImageView) view.findViewById(R.id.ap_iV);
            iv.setImageResource(itemImage[position]);
            return view;
        }
    }



}
