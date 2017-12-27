package com.demo.cicada.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.demo.cicada.R;
import com.demo.cicada.fragment.AlbumFragment;
import com.demo.cicada.fragment.FolderFragment;
import com.demo.cicada.fragment.SingerFragment;
import com.demo.cicada.fragment.SongFragment;
import com.demo.cicada.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends PlayBarBaseActivity {

    private static final String TAG = LocalMusicActivity.class.getName();
    private List<String> titleList = new ArrayList<>(4);
    private List<Fragment> fragments = new ArrayList<>(4);
    private SongFragment songFragment;
    private SingerFragment singerFragment;
    private AlbumFragment albumFragment;
    private FolderFragment folderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        // 标题栏设置
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.local_music_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 给标题栏左边加上一个返回的图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Constant.LABEL_LOCAL);
        }*/
        initToolbar(R.id.local_music_toolbar, R.string.local_music);
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    // 初始化控件
    private void initView() {
        addTapData();
        MyViewPager viewPager = (MyViewPager) findViewById(R.id.local_viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.local_tab);
        MyAdapter fragmentAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        //当前view的左右两边的预加载的页面的个数
        viewPager.setOffscreenPageLimit(2);
        // 设置tab模式，当前为系统默认模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // TabLayout绑定ViewPager滑动
        tabLayout.setupWithViewPager(viewPager);
    }

    // 滑动布局
    private void addTapData() {
        titleList.add("歌曲");
        titleList.add("歌手");
        titleList.add("专辑");
        titleList.add("文件夹");

        if (songFragment == null) {
            songFragment = new SongFragment();
            fragments.add(songFragment);
        }
        if (singerFragment == null) {
            singerFragment = new SingerFragment();
            fragments.add(singerFragment);
        }
        if (albumFragment == null) {
            albumFragment = new AlbumFragment();
            fragments.add(albumFragment);
        }
        if (folderFragment == null) {
            folderFragment = new FolderFragment();
            fragments.add(folderFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.local_music_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.scan_local_menu) {
            Intent intent = new Intent(LocalMusicActivity.this, ScanActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        // 用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
