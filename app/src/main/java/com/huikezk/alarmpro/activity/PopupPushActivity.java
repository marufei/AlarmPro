package com.huikezk.alarmpro.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
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

import net.igenius.mqttservice.MQTTServiceCommand;

import org.json.JSONObject;

import java.util.Map;

public class PopupPushActivity extends AndroidPopupActivity {

    static final String TAG = "PopupPushActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_popup_push);
    }

    /**
     * 实现通知打开回调方法，获取通知相关信息
     *
     * @param title   标题
     * @param summary 内容
     * @param extMap  额外参数
     */
    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        MyUtils.Loge(TAG, "OnMiPushSysNoticeOpened, title: " + title + ", content: " + summary + ", extMap: " + extMap);
        String projectName = extMap.get("projectName");
        String messageType = extMap.get("messageType");
        MyUtils.Loge(TAG, "projectName:" + projectName + "--messageType:" + messageType);


        if (!TextUtils.isEmpty(projectName) && !TextUtils.isEmpty(messageType)) {

            login(projectName, messageType);

        }

    }

    /**
     * 登录
     */
    private void login(final String projectName, final String messageType) {
        String url = HttpsConts.BASE_URL + HttpsConts.LOGIN
                + "?username=" + SaveUtils.getString(KeyUtils.TEL)
                + "&password=" + SaveUtils.getString(KeyUtils.PWD);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        LoginEntity loginEntity = gson.fromJson(response, LoginEntity.class);
                        if (loginEntity != null && loginEntity.getData() != null) {
                            MyApplication.loginEntity = loginEntity;
                            setSP(loginEntity.getData());
                            switch (messageType) {
                                case "alarm":
                                    Intent intent = new Intent(PopupPushActivity.this, MainActivity.class);
                                    intent.putExtra("currentPage", 1);
                                    intent.putExtra("projectName", projectName);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "repair":
                                    Intent intent1 = new Intent(PopupPushActivity.this, MainActivity.class);
                                    intent1.putExtra("currentPage", 2);
                                    intent1.putExtra("projectName", projectName);
                                    startActivity(intent1);
                                    finish();
                                    break;
                                default:
                                    finish();
                                    break;
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(PopupPushActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(PopupPushActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(PopupPushActivity.this).addToRequestQueue(stringRequest);

    }

    private void setSP(LoginEntity.DataBean data) {
        SaveUtils.setString(KeyUtils.USER_ID, data.get_id());
        SaveUtils.setString(KeyUtils.NICK_NAME, data.getNickName());
        SaveUtils.setString(KeyUtils.USER_NAME, data.getUsername());
        if (data.getProjectName() != null && data.getProjectName().size() > 0) {
            SaveUtils.setString(KeyUtils.PROJECT_NAME, data.getProjectName().get(0).getProjectName());
            SaveUtils.setString(KeyUtils.PROJECT_SEND, "/" + data.getProjectName().get(0).getProjectName() + "/");
            SaveUtils.setString(KeyUtils.PROJECT_NUM, String.valueOf(data.getProjectName().get(0).getProjectNum()));
            String[] projectList = new String[data.getProjectName().size()];
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
        }
        SaveUtils.setString(KeyUtils.PROJECT_IP,data.getIp());
        SaveUtils.setString(KeyUtils.TEL, data.getUsername());
        SaveUtils.setString(KeyUtils.PWD, data.getPassword());
        SaveUtils.setString(KeyUtils.PIC_URL, data.getImage());
        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.MQTT_URL))) {
            MyUtils.Loge(TAG, "clientId：" + Build.SERIAL);
            MQTTServiceCommand.connect(getApplicationContext(),
                    "tcp://" + SaveUtils.getString(KeyUtils.MQTT_URL),
                    Build.SERIAL,
                    "admin",
                    "123456");
        }

    }
}
