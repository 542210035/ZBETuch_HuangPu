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

import com.youli.zbetuch_huangpu.R;

public class GuidanceActivity extends BaseActivity {
    private Context mContext=GuidanceActivity.this;
    private Intent intent;
    private GridView gridView;
    private int [] ImageItem=new int[]{
            R.drawable.zdhd,  R.drawable.pxkc,  R.drawable.pxhd
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        initVew();
    }
    private void initVew(){
        gridView= (GridView) findViewById(R.id.gridviewchuangz);
        gridView.setAdapter(new getActivity());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                         intent=new Intent(mContext,CreateActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(mContext,KechengPXActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                         intent=new Intent(mContext,ZhidaoActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }


private class getActivity extends BaseAdapter{

    @Override
    public int getCount() {
        return ImageItem.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return ImageItem.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(GuidanceActivity.this).inflate(R.layout.activity_item_cyzd,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.ap_ivv);
        imageView.setImageResource(ImageItem[position]);
        return view;
    }
}

}
