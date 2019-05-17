package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.LoginEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.List;

public class SplashActivity extends BaseActivity {

    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    //跳转到登录界面
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };
    private String TAG = "SplashActivity";
    private String[] projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setDarkStatusIcon(true);
        //如果从后台进入APP不再显示启动页
//        if (!isTaskRoot() && getIntent() != null) {
//            String action = getIntent().getAction();
//            if (getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
//                finish();
//                return;
//            }
//        }
        setContentView(R.layout.activity_splash);


        initData();

    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SplashActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
//        MyApplication.flag = 0;
        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.TEL)) &&
                !TextUtils.isEmpty(SaveUtils.getString(KeyUtils.PWD))) {

            login();
        } else {
            // 停留3秒后发送消息，跳转到登录界面
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000);
        }
    }

    /**
     * 登录
     */
    private void login() {
        showLoadingAnim(this);
        String url = HttpsConts.BASE_URL + HttpsConts.LOGIN
                + "?username=" + SaveUtils.getString(KeyUtils.TEL)
                + "&password=" + SaveUtils.getString(KeyUtils.PWD);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(SplashActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        LoginEntity loginEntity = gson.fromJson(response, LoginEntity.class);
                        if (loginEntity != null && loginEntity.getData() != null) {
                            if (loginEntity != null && loginEntity.getData() != null) {
                                MyApplication.loginEntity = loginEntity;
                                setSP(loginEntity.getData());
                                MainActivity.start(SplashActivity.this);
                                finish();
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(SplashActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(SplashActivity.this);
                MyUtils.showToast(SplashActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(SplashActivity.this).addToRequestQueue(stringRequest);

    }

    private void setSP(LoginEntity.DataBean data) {
        SaveUtils.setString(KeyUtils.USER_ID, data.get_id());
        SaveUtils.setString(KeyUtils.NICK_NAME, data.getNickName());
        SaveUtils.setString(KeyUtils.USER_NAME, data.getUsername());
        if (data.getProjectName() != null && data.getProjectName().size() > 0) {
            SaveUtils.setString(KeyUtils.PROJECT_NAME, data.getProjectName().get(0).getProjectName());
            SaveUtils.setString(KeyUtils.PROJECT_SEND, "/" + data.getProjectName().get(0).getProjectName() + "/");
            SaveUtils.setString(KeyUtils.PROJECT_NUM, String.valueOf(data.getProjectName().get(0).getProjectNum()));
            projectList = new String[data.getProjectName().size()];
            String topics = "";
            for (int i = 0; i < data.getProjectName().size(); i++) {
                projectList[i] = "/" + data.getProjectName().get(i).getProjectName() + "/#";
                if (i == data.getProjectName().size() - 1) {
                    topics = topics + "/" + data.getProjectName().get(i).getProjectName() + "/#";
                } else {
                    topics = topics + "/" + data.getProjectName().get(i).getProjectName() + "/#" + ",";
                }
            }
            MyUtils.Loge(TAG, "topics：" + topics);
            SaveUtils.setString(KeyUtils.TOPICS, topics);
            SaveUtils.setString(KeyUtils.MQTT_URL, data.getMqttUrl());
            MyApplication.projectList = projectList;
            MyApplication.MOUDLE = data.getProjectName().get(0).getModules();
            List<String> keyList = SaveUtils.getAllKeys();
            if (MyApplication.projectList != null) {
                MyUtils.Loge(TAG, "MyApplication.projectList：" + MyApplication.projectList.length);
                for (int i = 0; i < MyApplication.projectList.length; i++) {
                    for (int j = 0; j < keyList.size(); j++) {
                        if (keyList.get(j).contains(data.getProjectName().get(i).getProjectName())) {
                            SaveUtils.removeData(keyList.get(j));
                        }
                    }
                }
            }
        }
        SaveUtils.setString(KeyUtils.PROJECT_IP, data.getIp());
        SaveUtils.setString(KeyUtils.TEL, data.getUsername());
        SaveUtils.setString(KeyUtils.PWD, data.getPassword());
        SaveUtils.setString(KeyUtils.PIC_URL, data.getImage());

    }

}
