package com.huikezk.alarmpro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.entity.LoginEntity;
import com.huikezk.alarmpro.utils.MyUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

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
    private PushAgent mPushAgent;



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initPush();
    }

    public PushAgent getmPushAgent() {
        return mPushAgent;
    }

    /**
     * 初始化友盟
     */
    private void initPush() {
        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
        // 参数一：当前上下文context；
        // 参数二：应用申请的Appkey（需替换）；
        // 参数三：渠道名称；
        // 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；
        // 传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
        // 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, "5c7a704d0cafb2d38e00157c",
                "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "7ce600fe4fb93d022bea667e86383182");

        //获取消息推送代理示例
        mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                MyUtils.Loge(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
                UMENG_TOKEN=deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {
                MyUtils.Loge(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        //小米通道
        MiPushRegistar.register(this, HttpsConts.MIUI_APP_ID,HttpsConts.MIUI_APP_KEY);

        //魅族通道
        MeizuRegister.register(this, HttpsConts.MZ_APP_ID, HttpsConts.MZ_APP_KEY);

//        mPushAgent.addAlias();
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
