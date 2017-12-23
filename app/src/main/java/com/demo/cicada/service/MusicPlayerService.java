package com.demo.cicada.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.demo.cicada.R;
import com.demo.cicada.activity.MusicActivity;
import com.demo.cicada.receiver.PlayerManagerReceiver;

public class MusicPlayerService extends Service {
    private static final String TAG = MusicPlayerService.class.getName();

    public static final String PLAYER_MANAGER_ACTION = "com.demo.cicada.service.MusicPlayerService.player.action";

    private PlayerManagerReceiver mReceiver;
    //通知 是通过 NotificationManager 来管理的
    private NotificationManager notifyManager;

    private final int NOTIFICATION_ID = 1;

    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        //初始化得到通知系统服务
        notifyManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 创建通知
//        creatNotification();
        showNotify();
        register();
    }

    @SuppressLint("NewApi")
    private void creatNotification() {
        //通过 Builder 来创建  Notification 的
        Builder builder=new Notification.Builder(this);
        //设置 通知 图标
        builder.setSmallIcon(R.drawable.ic_music_notify);
        //设置 通知 显示标题
        builder.setTicker("Music");
        //设置 通知栏 标题
        builder.setContentTitle("知了听乐");
        //设置 通知内容
        // TODO: 2017/12/23
        builder.setContentText("// TODO...");
        //设置 提醒 声音/震动/指示灯
//        builder.setDefaults(Notification.DEFAULT_ALL);

        //设置 点击后的跳转 通过 pendingIntent 实现
        Intent intent=new Intent(this,MusicActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0, intent,0);
        builder.setContentIntent(pendingIntent);

        Notification notification=builder.build();
        notifyManager.notify(NOTIFICATION_ID, notification);
    }

    public void showNotify(){
        Notification.Builder builder=new Builder(this);
        RemoteViews remoteViews=new RemoteViews(getPackageName(), R.layout.notification_layout);
        //设置 点击后的跳转 通过 pendingIntent 实现
        Intent intent=new Intent(this,MusicActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0, intent,0);
        builder.setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setTicker("Music")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_music_notify);
        Notification notify=builder.build();
        startForeground(1,notify);
        notify.flags=Notification.FLAG_ONGOING_EVENT;
        notifyManager.notify(NOTIFICATION_ID,notify);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        unRegister();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }


    private void register() {
        mReceiver = new PlayerManagerReceiver(MusicPlayerService.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAYER_MANAGER_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    private void unRegister() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

}

