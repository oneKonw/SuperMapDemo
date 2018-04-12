package com.example.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by hyt on 2018/4/12.
 */

public class BaseMyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
