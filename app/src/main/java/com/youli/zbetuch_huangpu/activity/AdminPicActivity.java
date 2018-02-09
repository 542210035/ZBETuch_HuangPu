package com.youli.zbetuch_huangpu.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.youli.zbetuch_huangpu.R;

/**
 * Created by liutao on 2017/12/29.
 */

//头像
public class AdminPicActivity extends BaseActivity{

    private ImageView iv;

    private byte [] picByte;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pic);

        iv= (ImageView) findViewById(R.id.iv_admin_pic);

        picByte=getIntent().getByteArrayExtra("pic");

        if(picByte!=null){
            setPic();
        }
    }

    private void setPic(){

       Bitmap bm= BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
      iv.setImageBitmap(bm);
    }

}
