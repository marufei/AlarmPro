package com.huikezk.alarmpro;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.huikezk.alarmpro.activity.SplashActivity;
import com.huikezk.alarmpro.entity.LoginEntity;
import com.huikezk.alarmpro.service.ListenerManager;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

import net.igenius.mqttservice.MQTTService;
import net.igenius.mqttservice.MQTTServiceCommand;
import net.igenius.mqttservice.MQTTServiceReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/1.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MyApplication extends Application {

    /**
     * 更新地址
     */
    public static String update_url="";

    /**
     * 更新内容
     */
    public static String update_content="";

    private String TAG = "MyApplication";


    public static LoginEntity loginEntity;

    /**
     * 项目名数组
     */
    public static String[] projectList;

    /**
     * http地址
     */
    public static String IP;

    /**
     * 当前项目下的模块集合
     */
    public static List<String> MOUDLE;

    //判断APP是否被回收
//    public static int flag = -1;


    /**
     * 打开的activity
     **/
    private static List<Activity> activities;
    private static MyApplication mInstance;

    private int activityCount = 0;
    private long firstTime = 0;
    private long lastTime = 0;

    PushSuccessListener pushSuccessListener;

    /**
     * 当前栈顶的activity
     */
    private Activity currentActivity;

    public void setPushSuccessListener(PushSuccessListener pushSuccessListener) {
        this.pushSuccessListener = pushSuccessListener;
    }

    /**
     * 第一次进来订阅
     */
    private boolean isFirst = true;

    /**
     * MQTT连接状态
     */
    public static boolean myConnected;

    MQTTServiceReceiver receiver = new MQTTServiceReceiver() {


        @Override
        public void onSubscriptionSuccessful(Context context,
                                             String requestId, String topic) {
            // called when a message has been successfully published
            MyUtils.Loge("lbw", "===onSubscriptionSuccessful");
            MyUtils.Loge("lbw", "===requestId:" + requestId + "--topic:" + topic);

        }

        @Override
        public void onSubscriptionError(Context context, String requestId,
                                        String topic, Exception exception) {
            // called when a subscription is not successful.
            // This usually happens when the broker does not give permissions
            // for the requested topic
            MyUtils.Loge("lbw", "===onSubscriptionError");
        }

        @Override
        public void onMessageArrived(Context context, String topic, byte[] payload) {
            MyUtils.Loge("lbw", "===onMessageArrived:" + topic + " " + new String(payload));
            SaveUtils.setString(topic, new String(payload));
            SaveUtils.removeManyData();
            ListenerManager.getInstance().sendBroadCast("com.huikezk.alarmpro.activity.BaseActivity");
        }

        @Override
        public void onPublishSuccessful(Context context, String requestId, String topic) {
            // called when a subscription is successful
            MyUtils.Loge("lbw", "===onPublishSuccessful");
        }


        @Override
        public void onConnectionSuccessful(Context context, String requestId) {
            // called when the connection is successful
            MyUtils.Loge("lbw", "===onConnectionSuccessful");

            if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.TOPICS))) {
                String[] topics = SaveUtils.getString(KeyUtils.TOPICS).split(",");
                MQTTServiceCommand.subscribe(getApplicationContext(), 1, false, topics);
            }

        }

        @Override
        public void onException(Context context, String requestId,
                                Exception exception) {
            // called when an error happens
            MyUtils.Loge("lbw", "===onException:" + exception.getMessage());
        }

        @Override
        public void onConnectionStatus(Context context, boolean connected) {
            myConnected=connected;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MQTTService.NAMESPACE = "com.huikezk.alarmpro";
//        registerActivityCallbacks();
        NotificationChannel();
        initCloudChannel(this);
        receiver.register(getApplicationContext());

    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                MyUtils.Loge(TAG, "init cloudchannel success");
                if (pushSuccessListener != null && !TextUtils.isEmpty(pushService.getDeviceId())) {
                    pushSuccessListener.onSuccess(pushService.getDeviceId());
                }
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                MyUtils.Loge(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(applicationContext, "2882303761517956329", "5811795650329");
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(applicationContext);
        //GCM/FCM辅助通道注册
//        GcmRegister.register(this, sendId, applicationId); //sendId/applicationId为步骤获得的参数
        // OPPO通道注册
//        OppoRegister.register(applicationContext, appKey, appSecret); // appKey/appSecret在OPPO通道开发者平台获取
    }

    /**
     * 注册NotificationChannel
     */
    private void NotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public static void addActivity(Activity activity) {
        if (activities == null) {
            activities = new ArrayList();
        }
        activities.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        Activity activity = activities.get(activities.size() - 1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishActivity() {
        try {
            Activity activity = activities.get(activities.size() - 1);
            finishActivity(activity);
        } catch (Exception e) {
//            LogUtil.e(TAG,"出错了");
        }

    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 查询栈中是否有这个
     *
     * @param cls
     */
    public static boolean QueryActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activities.size(); i < size; i++) {
            if (null != activities.get(i)) {
                activities.get(i).finish();
            }
        }
        activities.clear();
    }

    /**
     * 退出应用程序
     */
    public static void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    private void registerActivityCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivityCreated--currentActivity:" + currentActivity.toString());

            }

            @Override
            public void onActivityStarted(final Activity activity) {
//                //等于0时就是从后台或者打开app  这里SplashActivity就是你的启动页面如果不是启动页面就是后台进来的
//                MyUtils.Loge(TAG, "onActivityStarted---------activityCount:" + activityCount + "--firstTime:" + firstTime + "--lastTime:" + lastTime);
//                if (0 == activityCount && !(activity instanceof SplashActivity)) {
//                    firstTime = System.currentTimeMillis();
//                    if ((firstTime - lastTime) > (2 * 60 * 1000)) {
//                        //超过2分钟在后台就进入解锁页面
//                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finishAllActivity();
//                        receiver.unregister(getApplicationContext());
//                    }
//                }else {
//                    receiver.register(getApplicationContext());
//                }
//                activityCount++;
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivityStarted--currentActivity:" + currentActivity.toString());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivityResumed--currentActivity:" + currentActivity.toString());

            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivityPaused--currentActivity:" + currentActivity.toString());

            }

            @Override
            public void onActivityStopped(Activity activity) {
//                activityCount--;
//                MyUtils.Loge(TAG, "onActivityStopped-----activityCount:" + activityCount + "--firstTime:" + firstTime + "--lastTime:" + lastTime);
//                if (0 == activityCount) {
//                    lastTime = System.currentTimeMillis();//等于0时 就是从后台进来的
//                    if (lastTime == 0) {
//                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finishAllActivity();
//                        receiver.unregister(getApplicationContext());
//                    }
//                }else {
//                    receiver.register(getApplicationContext());
//                }
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivityStopped--currentActivity:" + currentActivity.toString());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivitySaveInstanceState--currentActivity:" + currentActivity.toString());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                currentActivity = activity;
                MyUtils.Loge(TAG, "onActivityDestroyed--currentActivity:" + currentActivity.toString());
            }
        });
    }

    public interface PushSuccessListener {
        void onSuccess(String string);
    }


    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
