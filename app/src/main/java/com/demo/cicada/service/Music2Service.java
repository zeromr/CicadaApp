package com.demo.cicada.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.demo.cicada.R;
import com.demo.cicada.activity.MusicActivity;
import com.demo.cicada.entity.music.MusicInfo;

import java.io.IOException;
import java.util.List;

/**
 * 音乐服务
 */

public class Music2Service extends Service {
    private int currentMusic;           // 正在播放的音乐
    private int currentPosition;        //正在播放的音乐的位置
    private static final int updateProgress = 1;
    private static final int updateCurrentMusic = 2;
    private static final int updateDuration = 3;
    public static MediaPlayer mediaPlayer;
    private boolean playingStatus = false;
    public static final String ACTION_UPDATE_PROGRESS = "UPDATE_PROGRESS";
    public static final String ACTION_UPDATE_DURATION = "UPDATE_DURATION";
    public static final String ACTION_UPDATE_CURRENT_MUSIC = "UPDATE_CURRENT_MUSIC";
    //    public static final String[] MODE_DESC={"Single Loop","List Loop","Random","Sequence"};
    public static final String[] MODE_DESC = {"单曲循环", "列表循环", "随机播放", "顺序播放"};
    public static final int MODE_SINGLE_LOOP = 0;   // 单曲循环
    public static final int MODE_LIST_LOOP = 1;     // 列表循环
    public static final int MODE_RANDOM = 2;        // 随机播放
    public static final int MODE_SEQUENCE = 3;      // 顺序播放
    private int currentMode = 3;                      // 默认顺序播放
    private List<MusicInfo> musicList;
    private Binder musicBinder = new MusicBinder();
    private Notification notice;

    @SuppressWarnings({"static-access", "deprecation"})
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();

        Intent intent = new Intent(this, MusicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notice = new Notification.Builder(this).setTicker("Music")                     //系统收到通知时，通知栏上面显示的文字
                .setSmallIcon(R.drawable.ic_music_notify)     //显示在通知栏上的小图标
                .setContentTitle("MusicPlayer")         // 设置通知栏标题
                .setContentText(musicList.get((currentMusic)).getName()).setContentIntent(pendingIntent)
                //                .setDefaults(Notification.DEFAULT_ALL)    //功能：向通知添加声音、闪灯和振动效果的最简单、使用默认（defaults）属性
                //                .setVibrate(new long[]{0,300,500,700})    //实现效果：延迟0ms，然后振动300ms，在延迟500ms，接着在振动700ms
                .setWhen(System.currentTimeMillis()).setPriority(Notification.PRIORITY_DEFAULT).getNotification();
        notice.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(1, notice);
    }

    // 初始化 MediaPlayer
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        // 指定流媒体的类型,不能再onCreate中设置
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 监听一个回调函数去执行音乐已经准备好并且可以开始时调用
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.seekTo(currentPosition);
                handler.sendEmptyMessage(updateDuration);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playingStatus) {
                    switch (currentMode) {
                        case MODE_SINGLE_LOOP:
                            mediaPlayer.start();
                            break;
                        case MODE_LIST_LOOP:
                            play((currentMusic + 1) % musicList.size(), 0);
                            break;
                        case MODE_RANDOM:
                            play(getRandomPosition(), 0);
                            break;
                        case MODE_SEQUENCE:
                            if (currentMusic < musicList.size() - 1)
                                playNext();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    private void play(int currentMusic, int currentPosition) {
        this.currentPosition = currentPosition;
        setCurrentMusic(currentMusic);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicList.get(currentMusic).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 开始准备,在OnPreparedListener的onPrepared的方法调用start()。
        mediaPlayer.prepareAsync();
        handler.sendEmptyMessage(updateProgress);
        playingStatus = true;
    }

    private void stop() {
        mediaPlayer.stop();
        playingStatus = false;
    }

    private void playNext() {
        switch (currentMode) {
            case MODE_SINGLE_LOOP:
                play(currentMusic, 0);
            case MODE_LIST_LOOP:
                if (currentMusic + 1 == musicList.size()) {
                    play(0, 0);
                } else {
                    play(currentMusic + 1, 0);
                }
                break;
            case MODE_SEQUENCE:
                if (currentMusic + 1 == musicList.size()) {
                    Toast.makeText(this, "No more song...", Toast.LENGTH_SHORT).show();
                } else {
                    play(currentMusic + 1, 0);
                }
                break;
            case MODE_RANDOM:
                play(getRandomPosition(), 0);
        }
    }

    // 向前
    private void playPrevious() {
        switch (currentMode) {
            case MODE_SINGLE_LOOP:
                play(currentMusic, 0);
                break;
            case MODE_LIST_LOOP:
                if (currentMusic - 1 < 0) {
                    play(musicList.size() - 1, 0);
                } else {
                    play(currentMusic - 1, 0);
                }
                break;
            case MODE_SEQUENCE:
                if (currentMusic - 1 < 0) {
                    Toast.makeText(this, "No previous song.", Toast.LENGTH_SHORT).show();
                } else {
                    play(currentMusic - 1, 0);
                }
                break;
            case MODE_RANDOM:
                play(getRandomPosition(), 0);
                break;
        }
    }

    private int getRandomPosition() {
        int random = (int) (Math.random() * (musicList.size() - 1));
        return random;
    }

    public int getCurrentMusic() {
        Log.i("msg", "getCurrentMusic: " + currentMusic);
        return currentMusic;
    }

    private int setCurrentMusic(int currentMusic) {
        this.currentMusic = currentMusic;
        handler.sendEmptyMessage(updateCurrentMusic);
        return currentMusic;
        //        Log.i("msg", "setCurrentMusic: "+currentMusic);
    }

    // 接收子线程发送的数据，并更新主线程UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case updateProgress:
                    toUpdateProgress();
                    break;
                case updateDuration:
                    toUpdateDuration();
                    break;
                case updateCurrentMusic:
                    toUpdateCurrentMusic();
                    break;
            }
        }
    };

    private void toUpdateCurrentMusic() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_CURRENT_MUSIC);
        intent.putExtra(ACTION_UPDATE_CURRENT_MUSIC, currentMusic);
        sendBroadcast(intent);
    }

    private void toUpdateDuration() {
        if (mediaPlayer != null && playingStatus) {
            Integer duration = mediaPlayer.getDuration();
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_DURATION);
            intent.putExtra(ACTION_UPDATE_DURATION, duration);
            sendBroadcast(intent);
        }
    }

    private void toUpdateProgress() {
        if (mediaPlayer != null && playingStatus) {
            Integer progress = mediaPlayer.getCurrentPosition();
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_PROGRESS);
            intent.putExtra(ACTION_UPDATE_PROGRESS, progress);
            sendBroadcast(intent);
            handler.sendEmptyMessageDelayed(updateProgress, 1000);   // 设置延迟
        }
    }

    public class MusicBinder extends Binder {

        public void startPlay(int currentMusic, int currentPosition) {
            play(currentMusic, currentPosition);
        }

        public void stopPlay() {
            stop();
        }

        public void toNext() {
            playNext();
        }

        public void toPrevious() {
            playPrevious();
        }

        // 改变播放模式
        public void changeMode() {
            currentMode = (currentMode + 1) % 4;
            Toast.makeText(Music2Service.this, MODE_DESC[currentMode], Toast.LENGTH_SHORT).show();
        }

        public int getCurrentMode() {return currentMode;}

        public boolean playingStatus() {
            return playingStatus;
        }

        // 通知Activity 更新当前的音乐和时间的变化
        public void noticeActivity() {
            toUpdateCurrentMusic();
            toUpdateDuration();
        }

        // 调整进度条后的音乐播放
        public void changeProgress(int progress) {
            if (mediaPlayer != null) {
                currentPosition = progress * 1000;
                if (playingStatus) {
                    mediaPlayer.seekTo(currentPosition);
                } else {
                    play(currentMusic, currentPosition);
                }
            }
        }
    }
}
