package com.demo.cicada.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.cicada.R;
import com.demo.cicada.database.DBManager;
import com.demo.cicada.fragment.PlayBarFragment;
import com.demo.cicada.receiver.PlayerManagerReceiver;
import com.demo.cicada.service.MusicService;
import com.demo.cicada.utils.Constant;
import com.demo.cicada.utils.CustomAttrValueUtil;
import com.demo.cicada.utils.MyMusicUtil;
import com.demo.cicada.view.PlayingPopWindow;

import java.util.Locale;

public class PlayActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PlayActivity.class.getName();
    private DBManager dbManager;
    private ImageView playIv;
    private ImageView modeIv;
    private TextView curTimeTv;
    private TextView totalTimeTv;
    private TextView musicNameTv;
    private TextView singerNameTv;
    private SeekBar seekBar;
    private PlayReceiver mReceiver;
    private int mProgress;
    private int duration;
    private int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        dbManager = DBManager.getInstance(PlayActivity.this);
        setStyle();
        initView();
        register();
    }

    private void initView() {
        ImageView backIv = (ImageView) findViewById(R.id.iv_back);
        playIv = (ImageView) findViewById(R.id.iv_play);
        ImageView menuIv = (ImageView) findViewById(R.id.iv_menu);
        ImageView preIv = (ImageView) findViewById(R.id.iv_prev);
        ImageView nextIv = (ImageView) findViewById(R.id.iv_next);
        modeIv = (ImageView) findViewById(R.id.iv_mode);
        curTimeTv = (TextView) findViewById(R.id.tv_current_time);
        totalTimeTv = (TextView) findViewById(R.id.tv_total_time);
        musicNameTv = (TextView) findViewById(R.id.tv_title);
        singerNameTv = (TextView) findViewById(R.id.tv_artist);
        seekBar = (SeekBar) findViewById(R.id.activity_play_seekbar);
        backIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        menuIv.setOnClickListener(this);
        preIv.setOnClickListener(this);
        nextIv.setOnClickListener(this);
        modeIv.setOnClickListener(this);

        setSeekBarBg();
        initPlayMode();
        initTitle();
        initPlayIv();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //                seekBar_touch = true;	//可以拖动标志
                int musicId = MyMusicUtil.getIntShared(Constant.KEY_ID);
                if (musicId == -1) {
                    Intent intent = new Intent(MusicService.PLAYER_MANAGER_ACTION);
                    intent.putExtra("cmd", Constant.COMMAND_STOP);
                    sendBroadcast(intent);
                    Toast.makeText(PlayActivity.this, "歌曲不存在", Toast.LENGTH_LONG).show();
                    return;
                }

                //发送播放请求
                Intent intent = new Intent(MusicService.PLAYER_MANAGER_ACTION);
                intent.putExtra("cmd", Constant.COMMAND_PROGRESS);
                intent.putExtra("current", mProgress);
                sendBroadcast(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
                initTime();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_mode:
                switchPlayMode();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_next:
                MyMusicUtil.playNextMusic(this);
                break;
            case R.id.iv_prev:
                MyMusicUtil.playPreMusic(this);
                break;
            case R.id.iv_menu:
                showPopFormBottom();
                break;
        }
    }


    private void initPlayIv() {
        int status = PlayerManagerReceiver.status;
        switch (status) {
            case Constant.STATUS_STOP:
                playIv.setSelected(false);
                break;
            case Constant.STATUS_PLAY:
                playIv.setSelected(true);
                break;
            case Constant.STATUS_PAUSE:
                playIv.setSelected(false);
                break;
            case Constant.STATUS_RUN:
                playIv.setSelected(true);
                break;
        }
    }

    private void initPlayMode() {
        int playMode = MyMusicUtil.getIntShared(Constant.KEY_MODE);
        if (playMode == -1) {
            playMode = 0;
        }
        modeIv.setImageLevel(playMode);
    }

    private void initTitle() {
        int musicId = MyMusicUtil.getIntShared(Constant.KEY_ID);
        if (musicId == -1) {
            musicNameTv.setText("歌曲");
            singerNameTv.setText("歌手");
        } else {
            musicNameTv.setText(dbManager.getMusicInfo(musicId).get(1));
            singerNameTv.setText(dbManager.getMusicInfo(musicId).get(2));
        }
    }

    private void initTime() {
        curTimeTv.setText(formatTime(current));
        totalTimeTv.setText(formatTime(duration));
    }

    private String formatTime(long time) {
        return formatTime("mm:ss", time);
    }

    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }

    private void switchPlayMode() {
        int playMode = MyMusicUtil.getIntShared(Constant.KEY_MODE);
        switch (playMode) {
            case Constant.PLAYMODE_SEQUENCE:
                MyMusicUtil.setShared(Constant.KEY_MODE, Constant.PLAYMODE_RANDOM);
                break;
            case Constant.PLAYMODE_RANDOM:
                MyMusicUtil.setShared(Constant.KEY_MODE, Constant.PLAYMODE_SINGLE_REPEAT);
                break;
            case Constant.PLAYMODE_SINGLE_REPEAT:
                MyMusicUtil.setShared(Constant.KEY_MODE, Constant.PLAYMODE_SEQUENCE);
                break;
        }
        initPlayMode();
    }

    private void setSeekBarBg() {
        try {
            int progressColor = CustomAttrValueUtil.getAttrColorValue(R.attr.colorPrimary, R.color.colorAccent, this);
            LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
            ScaleDrawable scaleDrawable = (ScaleDrawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);
            GradientDrawable drawable = (GradientDrawable) scaleDrawable.getDrawable();
            if (drawable != null) {
                drawable.setColor(progressColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() {
        int musicId;
        musicId = MyMusicUtil.getIntShared(Constant.KEY_ID);
        if (musicId == -1 || musicId == 0) {
            //            musicId = dbManager.getFirstId(Constant.LIST_ALLMUSIC);
            Intent intent = new Intent(Constant.MP_FILTER);
            intent.putExtra(Constant.COMMAND, Constant.COMMAND_STOP);
            sendBroadcast(intent);
            Toast.makeText(PlayActivity.this, "歌曲不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        //如果当前媒体在播放音乐状态，则图片显示暂停图片，按下播放键，则发送暂停媒体命令，图片显示播放图片。以此类推。
        if (PlayerManagerReceiver.status == Constant.STATUS_PAUSE) {
            Intent intent = new Intent(MusicService.PLAYER_MANAGER_ACTION);
            intent.putExtra(Constant.COMMAND, Constant.COMMAND_PLAY);
            sendBroadcast(intent);
        } else if (PlayerManagerReceiver.status == Constant.STATUS_PLAY) {
            Intent intent = new Intent(MusicService.PLAYER_MANAGER_ACTION);
            intent.putExtra(Constant.COMMAND, Constant.COMMAND_PAUSE);
            sendBroadcast(intent);
        } else {
            //为停止状态时发送播放命令，并发送将要播放歌曲的路径
            String path = dbManager.getMusicPath(musicId);
            Intent intent = new Intent(MusicService.PLAYER_MANAGER_ACTION);
            intent.putExtra(Constant.COMMAND, Constant.COMMAND_PLAY);
            intent.putExtra(Constant.KEY_PATH, path);
            Log.i(TAG, "onClick: path = " + path);
            sendBroadcast(intent);
        }
    }

    /**
     * 显示当前音乐的详情界面
     */
    public void showPopFormBottom() {
        PlayingPopWindow playingPopWindow = new PlayingPopWindow(PlayActivity.this);
        playingPopWindow.showAtLocation(findViewById(R.id.activity_play), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);

        playingPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private void register() {
        mReceiver = new PlayReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlayBarFragment.ACTION_UPDATE_UI_PlayBar);
        registerReceiver(mReceiver, intentFilter);
    }

    private void unRegister() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    class PlayReceiver extends BroadcastReceiver {
        int status;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            initTitle();
            status = intent.getIntExtra(Constant.STATUS, 0);
            current = intent.getIntExtra(Constant.KEY_CURRENT, 0);
            duration = intent.getIntExtra(Constant.KEY_DURATION, 100);
            switch (status) {
                case Constant.STATUS_STOP:
                    playIv.setSelected(false);
                    break;
                case Constant.STATUS_PLAY:
                    playIv.setSelected(true);
                    break;
                case Constant.STATUS_PAUSE:
                    playIv.setSelected(false);
                    break;
                case Constant.STATUS_RUN:
                    playIv.setSelected(true);
                    seekBar.setMax(duration);
                    seekBar.setProgress(current);
                    break;
                default:
                    break;
            }

        }
    }

    private void setStyle() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
