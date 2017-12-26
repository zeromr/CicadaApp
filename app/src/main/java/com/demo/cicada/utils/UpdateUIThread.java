package com.demo.cicada.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.demo.cicada.fragment.PlayBarFragment;
import com.demo.cicada.receiver.PlayerManagerReceiver;

//此线程只是用于循环发送广播，通知更改歌曲播放进度。
public class UpdateUIThread extends Thread {

    private static final String TAG = UpdateUIThread.class.getName();
    private int threadNumber;
    private Context context;
    private PlayerManagerReceiver playerManagerReceiver;

    public UpdateUIThread(PlayerManagerReceiver playerManagerReceiver, Context context, int threadNumber) {
        Log.i(TAG, "UpdateUIThread: ");
        this.playerManagerReceiver = playerManagerReceiver;
        this.context = context;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        try {
            while (playerManagerReceiver.getThreadNumber() == this.threadNumber) {
                if (PlayerManagerReceiver.status == Constant.STATUS_STOP) {
                    Log.e(TAG, "run: Constant.STATUS_STOP");
                    break;
                }
                if (PlayerManagerReceiver.status == Constant.STATUS_PLAY || PlayerManagerReceiver.status == Constant.STATUS_PAUSE) {
                    if (!playerManagerReceiver.getMediaPlayer().isPlaying()) {
                        Log.i(TAG, "run: getMediaPlayer().isPlaying() = " + playerManagerReceiver.getMediaPlayer()
								.isPlaying());
                        break;
                    }
                    int duration = playerManagerReceiver.getMediaPlayer().getDuration();
                    int curPosition = playerManagerReceiver.getMediaPlayer().getCurrentPosition();
                    Intent intent = new Intent(PlayBarFragment.ACTION_UPDATE_UI_PlayBar);
                    intent.putExtra(Constant.STATUS, Constant.STATUS_RUN);
                    //				intent.putExtra("status2", playerManagerReceiver.status);
                    intent.putExtra(Constant.KEY_DURATION, duration);
                    intent.putExtra(Constant.KEY_CURRENT, curPosition);
                    context.sendBroadcast(intent);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

