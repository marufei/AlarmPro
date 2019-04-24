package com.huikezk.alarmpro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

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
     * 友盟token
     */
    public static  String UMENG_TOKEN ;
    public static String PIC_URL="";
    private String TAG = "MyApplication";

    /**
     * userId
     */
    public static String USER_ID="";

    /**
     * 工程号
     */
    public static int PROJECT_NUM;

    /**
     * 用户昵称
     */
    public static String NICK_NAME="";

    /**
     * 用户名
     */
    public static String USER_NAME="";

    /**
     * 选中的项目名
     */
    public static String PROJECT_NAME="";

    public static String PROJECT_SEND="";

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



    /**
     * 打开的activity
     **/
    private static List<Activity> activities;
    private static MyApplication mInstance;

    /**
     * 第一次进来订阅
     */
    private boolean isFirst=true;
     MQTTServiceReceiver receiver = new MQTTServiceReceiver() {
        @Override
        public void onSubscriptionSuccessful(Context context,
                                             String requestId, String topic) {
            // called when a message has been successfully published
            MyUtils.Loge("lbw", "===onSubscriptionSuccessful");
            MyUtils.Loge("lbw", "===requestId:"+requestId+"--topic:"+topic);

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

//        @Override
//        public void onMessageArrived(Context context, String topic, String payload) {
//            MyUtils.Loge("lbw", "===onMessageArrived:" + topic + " " + payload);
//            SaveUtils.setString(topic, payload);
//            SaveUtils.removeManyData();
//            ListenerManager.getInstance().sendBroadCast("com.huikezk.alarmpro.activity.BaseActivity");
//        }

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
                MQTTServiceCommand.subscribe(getApplicationContext(), 1,false, topics);
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

        }
    };



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MQTTService.NAMESPACE="com.huikezk.alarmpro";
        receiver.register(getApplicationContext());
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



}
