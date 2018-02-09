package com.youli.zbetuch_huangpu.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.youli.zbetuch_huangpu.R;
import com.youli.zbetuch_huangpu.entity.EduInfo;
import com.youli.zbetuch_huangpu.entity.PersonJwInfo;
import com.youli.zbetuch_huangpu.entity.PersonStreetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liutao on 2018/1/4.
 */

public class SpinnerUtil {

    /**
     * Spinner Utils
     *
     * @param spinner 控件名
     * @param resId   数组资源 Id
     */
    public static void setSpinner(Context context, Spinner spinner, int resId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, resId, R
                .layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }


    public static <T>void spSetAdapter(Context context,Spinner spinner, List<T> list){

        List<String> typeList=new ArrayList<>();


        if(list.get(0) instanceof EduInfo){
            typeList.add("全部");
            for(EduInfo infoList:(List<EduInfo>)list){

                typeList.add(infoList.getWHCD());

            }
        }else  if(list.get(0) instanceof PersonStreetInfo){
            typeList.add("全部");
            for(PersonStreetInfo infoList:(List<PersonStreetInfo>)list){

                typeList.add(infoList.getJDMC());

            }
        }else  if(list.get(0) instanceof PersonJwInfo){
            typeList.add("全部");
            for(PersonJwInfo infoList:(List<PersonJwInfo>)list){

                typeList.add(infoList.getJWMC());

            }
        }

        ArrayAdapter  typeAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(typeAdapter);
    }

//    /**
//     * 传入 String 类型的 list ，将内容填充到 spinner 中
//     *
//     * @param stringList
//     */
//    private void showSpinner(final List<String> stringList, final Spinner spinner) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R
//                        .layout.simple_spinner_item, stringList);
//                adapter.setDropDownViewResource(android.R.layout
//                        .simple_spinner_dropdown_item);
//                spinner.setAdapter(adapter);
//                if (spinner.getAdapter().getCount() > 1) {
//                    spinner.setSelection(0);
//                }
//            }
//        });
//    }

}
