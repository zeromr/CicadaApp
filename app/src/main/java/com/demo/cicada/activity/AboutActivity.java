package com.demo.cicada.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.cicada.R;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildVersion();
        setContentView(R.layout.activity_about);

        initView();
        onClickListenerShare();
    }

    public void initView() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView aboutImageView = (ImageView) findViewById(R.id.about_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitleEnabled(false);
        collapsingToolbar.setTitle(getString(R.string.app_name));
        Glide.with(this).load(R.drawable.ic_about_head).into(aboutImageView);
    }

    // 分享点击事件
    public void onClickListenerShare() {
        FloatingActionButton fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });
    }

    // 分享App
    public void shareApp() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_txt));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)));
    }

    // 点击返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
