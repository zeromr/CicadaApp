package com.demo.cicada.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.demo.cicada.R;
import com.demo.cicada.utils.MyMusicUtil;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
    }

    private void initTheme(){
        int themeId = MyMusicUtil.getTheme(BaseActivity.this);
        switch (themeId){
            case 1:
                setTheme(R.style.ZhiHuBlueTheme);
                break;
            default:
                break;
        }
    }


}
