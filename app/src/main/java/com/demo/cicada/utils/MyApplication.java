package com.demo.cicada.utils;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * 全局变量
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);

    }

    public static Context getContext() {
        return context;
    }
}
