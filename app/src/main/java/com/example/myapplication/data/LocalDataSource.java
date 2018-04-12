package com.example.myapplication.data;

import android.content.SharedPreferences;
import android.service.autofill.Dataset;

import com.example.myapplication.BaseMyApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hyt on 2018/4/7.
 */

public class LocalDataSource implements DataSource {

    @Override
    public void savePathOfLicense(String path) {
         SharedPreferences.Editor editorSaveLicense = BaseMyApplication.getContext().getSharedPreferences("someThing",MODE_PRIVATE).edit();
         editorSaveLicense.putString("pathOfLicense",path);
         editorSaveLicense.apply();

//        //读取第一周，如果为零则不进行计算，直接设置为第一周，不为零则进行计算，设置为当前周
//        SharedPreferences getFirstMonday = mContent.getSharedPreferences("getWeek",MODE_PRIVATE);
//        int firstMonday = getFirstMonday.getInt("firstDayofSemester",0);
//        if (firstMonday != 0) {
//            SharedPreferences.Editor editorSetWeekNow = mContent.getSharedPreferences("getWeek",MODE_PRIVATE).edit();
//            //防止周数超过25后出现闪退
//            if (DateUtil.getWeekOfNow(firstMonday) < 26){
//                editorSetWeekNow.putInt("weekNow", DateUtil.getWeekOfNow(firstMonday)-1);
//                editorSetWeekNow.apply();
//            }else{
//                editorSetWeekNow.putInt("weekNow", 0);
//                editorSetWeekNow.apply();
//            }
//        }
//        //读取当前星期值
//        pref = mContent.getSharedPreferences("getWeek",MODE_PRIVATE);
//        weekNow = pref.getInt("weekNow",0);
//        scheduleWeekRefresh.refresh(weekNow);

    }

    @Override
    public String getPathOfLicense() {
        String result ="";
        SharedPreferences getPath = BaseMyApplication.getContext().getSharedPreferences("someThing",MODE_PRIVATE);
        result = getPath.getString("pathOfLicense","");
        return result;
    }
}
