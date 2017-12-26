package com.demo.cicada.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.cicada.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动界面
 */
public class SplashActivity extends BaseActivity {
    List<String> permissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildVersion();
        setContentView(R.layout.activity_splash);
        hasCache();
    }

    // 缓存数据判读
    public void hasCache() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getString("weather", null) != null) {
            startActivity(new Intent(this, WeatherActivity.class));
            finish();
        } else {
            initView();
            applyPermission();
        }
    }

    // 初始化控件
    private void initView() {
        ImageView ivSplash = (ImageView) findViewById(R.id.iv_splash);
        TextView tvCopyright = (TextView) findViewById(R.id.tv_copyright);
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash);
        ivSplash.setImageBitmap(bitmap);*/
        loadBgImage(ivSplash,R.drawable.ic_splash);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        tvCopyright.setText(getString(R.string.copyright, year));
    }

    // Timer是一种定时器工具，用来在一个后台线程计划执行指定任务，它可以计划执行一个任务一次或反复多次
    private void gotoActivity() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(WeatherActivity.class);
            }
        };
        timer.schedule(task, 2000);     // 2秒后跳转
    }

    /*private void startWeatherActivity() {
        startActivity(new Intent(this, WeatherActivity.class));
        finish();
    }*/

    /**
     * 申请权限
     */
    private void applyPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            gotoActivity();
            return;
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String[] permisstions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SplashActivity.this, permisstions, 1);
        } else {
            gotoActivity();
        }
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    /*for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
//                            finish();
//                            return;
                        }
                    }*/
                    gotoActivity();
                } else {
                    Toast.makeText(this, "发生未知的错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
