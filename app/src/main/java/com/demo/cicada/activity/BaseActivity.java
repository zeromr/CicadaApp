package com.demo.cicada.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.demo.cicada.R;

//import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
    }

    // 初始化主题颜色为蓝色
    public void initTheme() {
        setTheme(R.style.ZhiHuBlueTheme);
    }

    /**
     * Build.VERSION.SDK_INT是指当前设备的API Level
     * 如果当前设备版本大于等于 Android 5.0 则设置半透明状态栏的效果(沉浸式状态栏)
     */
    public void buildVersion(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    // Activity跳转
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    // 加载背景图片
    public void loadBgImage(ImageView imageView,int resId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 初始化标题栏
     * @param id
     * @param resId 资源Id
     */
    public void initToolbar(int id,int resId) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 给标题栏左边加上一个返回箭头
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(resId);
        }
    }

    /**
     * 初始化标题栏
     * @param id
     * @param title 标题
     */
    public void initToolbar(int id,String title) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 给标题栏左边加上一个返回箭头
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }
}
