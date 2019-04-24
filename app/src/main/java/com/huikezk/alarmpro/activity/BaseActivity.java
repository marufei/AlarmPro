package com.huikezk.alarmpro.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.service.IListener;
import com.huikezk.alarmpro.service.ListenerManager;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.OSHelper;
import com.huikezk.alarmpro.utils.ToolUtils;
import com.huikezk.alarmpro.views.LoadingView;
import com.wang.avi.AVLoadingIndicatorView;

import net.igenius.mqttservice.MQTTServiceCommand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by MaRufei
 * on 2018/05/28.
 * Email: www.867814102@qq.com
 * Phone：132 1358 0912
 * Purpose:公共
 */
public class BaseActivity extends AppCompatActivity implements IListener {
    public static final String TAG = "BaseActivity";
    public static TextView MENU;

    public boolean RightOut = true;
    public AVLoadingIndicatorView avi;
    public LoadingView loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListenerManager.getInstance().registerListtener(this);
        //把状态栏设置为透明

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //透明状态栏
            window.setStatusBarColor(Color.TRANSPARENT);
            //透明导航栏
//            window.setNavigationBarColor(Color.TRANSPARENT);

        }

        //添加Activity到堆栈
        MyApplication.getInstance().addActivity(this);
        initLoading();
        setContentView(R.layout.view_title_bar);


    }


    /**
     * ToolBar样式
     *
     * @param type 0 白  1 左右渐变变 2 上下渐变
     */
    public void ToolBarStyle(int type) {
        switch (type) {
            case 0:
                findViewById(R.id.title_bar_tob).setBackgroundColor(getResources().getColor(R.color.transparent));
                ((TextView) findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.gray_3));
                ((ImageButton) findViewById(R.id.myBack)).setImageResource(R.mipmap.arrow_left);
                ((TextView) findViewById(R.id.menu)).setTextColor(getResources().getColor(R.color.gray_3));
                setDarkStatusIcon(true);
                findViewById(R.id.title_line).setVisibility(View.VISIBLE);
                break;
            case 1:
//                findViewById(R.id.title_bar_tob).setBackgroundColor(getResources().getColor(R.color.transparent));
                ((TextView) findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.white));
                ((ImageButton) findViewById(R.id.myBack)).setImageResource(R.mipmap.arrow_left_w);
                ((TextView) findViewById(R.id.menu)).setTextColor(getResources().getColor(R.color.white));
                setDarkStatusIcon(false);
                findViewById(R.id.title_line).setVisibility(View.GONE);
                break;
//            case 2:
//                findViewById(R.id.title_bar_tob).setBackgroundResource(R.mipmap.toolbar_style_2);
//                ((TextView) findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.white));
//                ((ImageButton) findViewById(R.id.back)).setImageResource(R.drawable.ic_back);
//                ((TextView) findViewById(R.id.menu)).setTextColor(getResources().getColor(R.color.white));
//
//                setDarkStatusIcon(false);
//                break;
//            case 3:
//                findViewById(R.id.title_bar_tob).setBackgroundResource(R.drawable.style_color_3);
//                ((TextView) findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.white));
//                ((ImageButton) findViewById(R.id.back)).setImageResource(R.drawable.ic_back);
//                ((TextView) findViewById(R.id.menu)).setTextColor(getResources().getColor(R.color.white));
//
//                setDarkStatusIcon(false);
//                break;
        }
    }

    /**
     * 状态栏 亮暗
     *
     * @param bDark
     */
    public void setDarkStatusIcon(boolean bDark) {

        //如果是Android 6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }

        if (OSHelper.isMIUI()) {
            setMiuiStatusBarDarkMode(this, bDark);
        }


        if (OSHelper.isFlyme()) {
            setMeizuStatusBarDarkIcon(this, bDark);
        }

    }

    /**
     * MIUI 系统
     *
     * @param activity
     * @param darkmode 字体颜色为黑色。
     * @return
     */
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Flyme 系统
     *
     * @param activity
     * @param dark     字体颜色
     * @return
     */
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
//                LogUtil.e(TAG, e.getMessage());
            }
        }
        return result;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        RelativeLayout titlebar = findViewById(R.id.title_bar_tob);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        //获取状态栏高度 加上 要设置的标题栏高度 等于 标题栏实际高度
        layoutParams.height = ToolUtils.getStatusHeight() + ToolUtils.dp2px(48);
        titlebar.setLayoutParams(layoutParams);

        TextView tv = findViewById(R.id.title);

//        if (BuildConfig.LogShow) {
//            tv.setText(title + "(测试)");
//        } else {
        tv.setText(title);
//        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public void setZT(String color, boolean bDark) {
        RelativeLayout titlebar = findViewById(R.id.title_bar_tob);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
        //获取状态栏高度 加上 要设置的标题栏高度 等于 标题栏实际高度
        layoutParams.height = ToolUtils.getStatusHeight();
        titlebar.setLayoutParams(layoutParams);
        titlebar.setBackgroundColor(Color.parseColor(color));

        setDarkStatusIcon(bDark);
    }

    /**
     * 是否有返回键
     *
     * @param isvisible
     */
    public void setBack(boolean isvisible) {
        ImageButton backBtn = findViewById(R.id.myBack);
        if (isvisible) {
            backBtn.setVisibility(View.VISIBLE);
        } else {
            backBtn.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });

    }

//    /**
//     * 图片代替返回按钮
//     */
//    public void backReplaced(int resourceId) {
//        ImageButton backBtn = findViewById(R.id.back);
//        backBtn.setImageResource(resourceId);
//        backBtn.setOnClickListener((View.OnClickListener) this);
//    }


    /**
     * 展示加载动画
     */
    public void showLoadingAnim(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
        MyUtils.Loge(TAG, "showLoadingAnim()--avi:" + avi);
        if (loadingView != null && avi != null) {
            loadingView.dismiss();
            loadingView.show();
            avi.show();
        }
    }

    public void hideLoadingAnim(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
        MyUtils.Loge(TAG, "hideLoadingAnim()--avi:" + avi);
        if (loadingView != null && avi != null) {
//            avi.hide();
            loadingView.dismiss();

        }
    }


    public void initLoading() {
        loadingView = new LoadingView(this);
        avi = loadingView.findViewById(R.id.avi);
        loadingView.showDialog();
    }

    @Override
    public void onBackPressed() {
        MyApplication.finishActivity();
    }

    /**
     * 设置菜单
     *
     * @param objects 图片或者文字/文字或者颜色/颜色
     */
    public void setMenu(Object... objects) {
        MENU = findViewById(R.id.menu);
        if (objects.length > 0) {
            MENU.setVisibility(View.VISIBLE);
            MENU.setOnClickListener((View.OnClickListener) this);
            //判断是不是图
            if (objects[0] instanceof Integer) {

                Drawable drawableleft = getResources().getDrawable((Integer) objects[0]);
                drawableleft.setBounds(0, 0, drawableleft.getIntrinsicWidth(), drawableleft.getMinimumHeight());
                MENU.setCompoundDrawables(drawableleft, null, null, null);//只放左边

                //判断是不是字
                if (objects.length > 1 && objects[1] instanceof String) {

                    MENU.setText((String) objects[1]);

                    //判断是不是颜色
                    if (objects.length > 2 && objects[2] instanceof Integer) {

                        MENU.setTextColor(getResources().getColor((Integer) objects[2]));

                    }
                }
            }

            //判断是不是字
            if (objects[0] instanceof String) {

                MENU.setText((String) objects[0]);
                //判断是不是颜色
                if (objects.length > 1 && objects[1] instanceof Integer) {
                    MENU.setTextColor(getResources().getColor((Integer) objects[1]));
                }
            }
        } else {
            MENU.setVisibility(View.GONE);
        }
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     **/
    public void showAlertDialog(String title, String message,
                                String positiveText,
                                DialogInterface.OnClickListener onClickListener,
                                String negativeText,
                                DialogInterface.OnClickListener onClickListener2) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener2).setCancelable(false)
                .show();
    }

    /**
     * QMTT 发布消息
     */
    public void publish(String topic, String msg) {
        //消息策略
        int qos = 1;
        //是否保留
        boolean retained = false;

        MQTTServiceCommand.publish(this, topic, msg.getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从栈中移除该Activity
        finishActivity(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkNetWork();
    }

    public void checkNetWork() {
        MyUtils.Loge(TAG,"判断网络1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    /**
                     * 网络可用的回调
                     * */
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        MyUtils.Loge(TAG,"判断网络1");
                    }

                    /**
                     * 网络丢失的回调
                     * */
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                    }

                    /**
                     * 当建立网络连接时，回调连接的属性
                     * */
                    @Override
                    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);
                    }

                    /**
                     *  按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
                     *
                     *  之后在仔细的研究
                     * */
                    @Override
                    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
                    }

                    /**
                     * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
                     * */
                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                    }

                    /**
                     * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
                     * */
                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                    }

                });
            }
        }
    }


    /**
     * 收到广播回调
     *
     * @param str
     */
    @Override
    public void notifyAllActivity(String str) {
//        MyUtils.Loge(TAG, "BaseActivity收到广播");
    }
}
