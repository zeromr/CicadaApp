package com.demo.cicada.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.demo.cicada.service.MusicPlayerService;

import org.litepal.LitePalApplication;

/**
 *
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        Intent startIntent = new Intent(MyApplication.this,MusicPlayerService.class);
        startService(startIntent);
//        initNightMode();
    }

    /*protected void initNightMode() {
        boolean isNight = MyMusicUtil.getNightMode(context);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }*/

    public static Context getContext() {
        return context;
    }
}
