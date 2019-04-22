package com.huikezk.alarmpro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.huikezk.alarmpro.R;

/**
 * Created by MaRufei
 * on 2019/4/22.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MyBackgroudService extends Service {

    private HandlerThread mWorkerThread;
    private Handler mHandler;
//    private PowerManager.WakeLock mWakeLock;


    @Override
    public void onCreate() {
        super.onCreate();
        String CHANNEL_ONE_ID = "com.primedu.cn";
        String CHANNEL_ONE_NAME = "Channel One";
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getSimpleName());
//
//        mWakeLock.acquire();

        mWorkerThread = new HandlerThread(getClass().getSimpleName(),
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mWorkerThread.start();
        mHandler = new Handler(mWorkerThread.getLooper());


    }

    private Notification getNotification() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setShowWhen(false);
        mBuilder.setAutoCancel(false);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentText("推送测试");
        mBuilder.setContentTitle("推送测试");
        return mBuilder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mWorkerThread.quitSafely();
        } else {
            mWorkerThread.quit();
        }
//        mWakeLock.release();
        stopForeground(true);
    }

    protected void post(Runnable job) {
        mHandler.post(job);
    }
}

