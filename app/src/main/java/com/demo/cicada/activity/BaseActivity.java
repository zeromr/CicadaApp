package com.demo.cicada.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.demo.cicada.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
    }

    private void initTheme() {
        setTheme(R.style.ZhiHuBlueTheme);
    }
}
