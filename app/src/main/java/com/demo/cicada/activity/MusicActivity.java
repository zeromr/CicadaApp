package com.demo.cicada.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.cicada.R;
import com.demo.cicada.adapter.MusicListViewAdapter;
import com.demo.cicada.database.DBManager;
import com.demo.cicada.entity.music.PlayListInfo;
import com.demo.cicada.service.MusicService;
import com.demo.cicada.utils.Constant;

import java.util.List;

public class MusicActivity extends PlayBarBaseActivity {
    private static final String TAG = MusicActivity.class.getName();
    private DBManager dbManager;
    private TextView localMusicCountTv;
    private TextView lastPlayCountTv;
    private TextView myLoveCountTv;
    private TextView myPLCountTv;
    private ImageView myPLArrowIv;
    private ListView listView;
    private MusicListViewAdapter adapter;
    private List<PlayListInfo> playListInfo;
    private int count;
    private boolean isOpenMyPL = false; //标识我的歌单列表打开状态
    private boolean isStartTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Log.i("msg", "onCreate: music");
        dbManager = DBManager.getInstance(MusicActivity.this);
//        initToolBar();
        initToolbar(R.id.home_activity_toolbar,R.string.music_title);
        initView();
        startMusiceService();
    }

    // 初始化ToolBar
    /*@Override
    public void initToolbar(int id, int resId) {
        super.initToolbar(id, resId);
    }*/

    /*public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.music_title);
        }
    }*/

    public void startMusiceService() {
        Intent intent = new Intent(MusicActivity.this, MusicService.class);
        //        Intent startIntent = new Intent(MusicActivity.this, Music2Service.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        count = dbManager.getMusicCount(Constant.LIST_ALLMUSIC);
        localMusicCountTv.setText(count + "");
        count = dbManager.getMusicCount(Constant.LIST_LASTPLAY);
        lastPlayCountTv.setText(count + "");
        count = dbManager.getMusicCount(Constant.LIST_MYLOVE);
        myLoveCountTv.setText(count + "");
        count = dbManager.getMusicCount(Constant.LIST_MYPLAY);
        myPLCountTv.setText("(" + count + ")");
        adapter.updateDataList();
    }

    // 初始化控件 
    private void initView() {
        LinearLayout llLocalMusic = (LinearLayout) findViewById(R.id.home_local_music_ll);
        LinearLayout lastPlayLl = (LinearLayout) findViewById(R.id.home_recently_music_ll);
        LinearLayout myLoveLl = (LinearLayout) findViewById(R.id.home_my_love_music_ll);
        LinearLayout myListTitleLl = (LinearLayout) findViewById(R.id.home_my_list_title_ll);
        listView = (ListView) findViewById(R.id.home_my_list_lv);
        localMusicCountTv = (TextView) findViewById(R.id.home_local_music_count_tv);
        lastPlayCountTv = (TextView) findViewById(R.id.home_recently_music_count_tv);
        myLoveCountTv = (TextView) findViewById(R.id.home_my_love_music_count_tv);
        myPLCountTv = (TextView) findViewById(R.id.home_my_list_count_tv);
        myPLArrowIv = (ImageView) findViewById(R.id.home_my_pl_arror_iv);
        ImageView myPLAddIv = (ImageView) findViewById(R.id.home_my_pl_add_iv);

        // 本地音乐
        llLocalMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicActivity.this, LocalMusicActivity.class);
                startActivity(intent);
            }
        });
       /* llLocalMusic.setOnClickListener(v -> {
            Intent intent = new Intent(MusicActivity.this, LocalMusicActivity.class);
            startActivity(intent);
        });*/

        lastPlayLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicActivity.this, LastAndLoveActivity.class);
                intent.putExtra(Constant.LABEL, Constant.LABEL_LAST);
                startActivity(intent);
            }
        });

        myLoveLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicActivity.this, LastAndLoveActivity.class);
                intent.putExtra(Constant.LABEL, Constant.LABEL_MYLOVE);
                startActivity(intent);
            }
        });

        playListInfo = dbManager.getMyPlayList();
        adapter = new MusicListViewAdapter(playListInfo, this, dbManager);
        listView.setAdapter(adapter);
        myPLAddIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加歌单
                final AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
                View view = LayoutInflater.from(MusicActivity.this).inflate(R.layout.dialog_create_playlist, null);
                final EditText etPlayList = (EditText) view.findViewById(R.id.dialog_playlist_name_et);
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etPlayList.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(MusicActivity.this, "请输入歌单名", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbManager.createPlaylist(name);
                        dialog.dismiss();
                        adapter.updateDataList();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();     // 配置好后再builder show
            }
        });
        myListTitleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展现我的歌单
                if (isOpenMyPL) {
                    isOpenMyPL = false;
                    myPLArrowIv.setImageResource(R.drawable.arrow_right);
                    listView.setVisibility(View.GONE);
                } else {
                    isOpenMyPL = true;
                    myPLArrowIv.setImageResource(R.drawable.arrow_down);
                    listView.setVisibility(View.VISIBLE);
                    playListInfo = dbManager.getMyPlayList();
                    adapter = new MusicListViewAdapter(playListInfo, MusicActivity.this, dbManager);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    public void updatePlaylistCount() {
        count = dbManager.getMusicCount(Constant.LIST_MYPLAY);
        myPLCountTv.setText("(" + count + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStartTheme) {
            MusicActivity.this.finish();
        }
        isStartTheme = false;
    }

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
