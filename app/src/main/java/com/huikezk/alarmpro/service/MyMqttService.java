package com.huikezk.alarmpro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.huikezk.alarmpro.BuildConfig;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;


import net.igenius.mqttservice.MQTTService;
import net.igenius.mqttservice.MQTTServiceCommand;
import net.igenius.mqttservice.MQTTServiceReceiver;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyMqttService extends Service {

    /**
     * 订阅topic
     */
    private String[] topics;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ExecutorService mqttThreadExecutor;
    Notification.Builder mBuilder;
    NotificationManager notificationManager;
    private String notificationId = "channelId";
    private String notificationName = "channelName";

    @Override
    public void onCreate() {
        super.onCreate();
        MyUtils.Loge("lbw", "===mqtt onCreate");
        mqttThreadExecutor = Executors.newSingleThreadExecutor();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyUtils.Loge("lbw", "===mqtt onStartCommand");
        String url = intent.getStringExtra("url");
        String clientId = intent.getStringExtra("clientId");
        String[] topics = intent.getStringArrayExtra("topics");
        initMqtt(url, clientId, topics);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMqtt(final String url, final String clientId, final String... topics) {
        this.topics=topics;

        MQTTService.NAMESPACE = BuildConfig.APPLICATION_ID; //or BuildConfig.APPLICATION_ID;
//        MQTTService.KEEP_ALIVE_INTERVAL = 10; //in seconds
//        MQTTService.CONNECT_TIMEOUT = 30;

        mqttThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //连接订阅MQTT
                MQTTServiceCommand.connect(getApplicationContext(), "tcp://"+url, clientId, "admin", "123456");
                receiver.register(MyMqttService.this);
            }
        });

//        MQTTServiceLogger.setLogLevel(MQTTServiceLogger.LogLevel.DEBUG);
//        MQTTServiceLogger.setLoggerDelegate(new MQTTServiceLogger.LoggerDelegate() {
//            @Override
//            public void error(String tag, String message) {
//                //your own implementation here
//                MyUtils.Loge("lbw", "===mqtt tag err:" + message);
//            }
//
//            @Override
//            public void error(String tag, String message, Throwable exception) {
//                //your own implementation here
//            }
//
//            @Override
//            public void debug(String tag, String message) {
//                //your own implementation here
//                MyUtils.Loge("lbw", "===mqtt tag debug:" + message);
//            }
//
//            @Override
//            public void info(String tag, String message) {
//                //your own implementation here
//                MyUtils.Loge("lbw", "===mqtt tag info:" + message);
//            }
//        });

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            channel.enableLights(false);
            channel.enableVibration(false);
        }

        startForeground(99, getNotification());
    }


    private MQTTServiceReceiver receiver = new MQTTServiceReceiver() {
        @Override
        public void onSubscriptionSuccessful(Context context,
                                             String requestId, String topic) {
            // called when a message has been successfully published
            MyUtils.Loge("lbw", "===mqtt onSubscriptionSuccessful");
        }

        @Override
        public void onSubscriptionError(Context context, String requestId,
                                        String topic, Exception exception) {
            // called when a subscription is not successful.
            // This usually happens when the broker does not give permissions
            // for the requested topic
            MyUtils.Loge("lbw", "===mqtt onSubscriptionError");
        }

        @Override
        public void onPublishSuccessful(Context context, String requestId, String topic) {
            // called when a subscription is successful
            MyUtils.Loge("lbw", "===mqtt onPublishSuccessful");
        }

        @Override
        public void onMessageArrived(Context context, final String topic,
                                     final byte[] payload) {
            // called when a new message arrives on any topic
            MyUtils.Loge("lbw", "===mqtt onMessageArrived:" + topic + " " + new String(payload));
            SaveUtils.setString(topic,new String(payload));
            SaveUtils.removeManyData();
            ListenerManager.getInstance().sendBroadCast("大家能收到我的信息吗");
            
        }

        @Override
        public void onConnectionSuccessful(Context context, String requestId) {
            // called when the connection is successful
//            Toast.makeText(getApplicationContext(), "推送服务已连接", Toast.LENGTH_SHORT).show();
            MyUtils.Loge("lbw", "===mqtt onConnectionSuccessful");
            mqttThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    MQTTServiceCommand.subscribe(getApplicationContext(), 1, true, topics);
                }
            });


        }

        @Override
        public void onException(Context context, String requestId,
                                Exception exception) {
            // called when an error happens
            MyUtils.Loge("lbw", "===mqtt onException:" + exception.toString());

        }

        @Override
        public void onConnectionStatus(Context context, boolean connected) {
            // called when connection status is requested or changes
            MyUtils.Loge("lbw", "===mqtt onConnectionStatus:" + connected);
        }
    };

    private Notification getNotification() {
        mBuilder = new Notification.Builder(MyMqttService.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(notificationId);
        }
        mBuilder.setShowWhen(false);
        mBuilder.setAutoCancel(false);
        mBuilder.setSmallIcon(R.mipmap.logo);
        mBuilder.setContentText("智能楼宇系统");
        mBuilder.setContentTitle("智能楼宇系统");
        return mBuilder.build();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyUtils.Loge("lbw", "===mqtt onDestroy");
        stopForeground(true);
        MQTTServiceCommand.disconnect(getApplicationContext());
        receiver.unregister(this);
    }


}
