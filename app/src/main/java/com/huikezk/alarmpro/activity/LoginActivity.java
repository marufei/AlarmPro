package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_login_commit;
    private EditText login_tel;
    private EditText login_pwd;
    private String TAG = "LoginActivity";
    private String[] projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        btn_login_commit = findViewById(R.id.btn_login_commit);
        btn_login_commit.setOnClickListener(this);
        login_tel = findViewById(R.id.login_tel);
        login_pwd = findViewById(R.id.login_pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_commit:
                if (TextUtils.isEmpty(login_tel.getText().toString())) {
                    MyUtils.showToast(LoginActivity.this, "请先输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(login_pwd.getText().toString())) {
                    MyUtils.showToast(LoginActivity.this, "请先输入密码");
                    return;
                }
                login();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        showLoadingAnim(this);
        String url = HttpsConts.BASE_URL + HttpsConts.LOGIN + "?username=" + login_tel.getText().toString().trim()
                + "&password=" + login_pwd.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(LoginActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        LoginEntity loginEntity = gson.fromJson(response, LoginEntity.class);
                        if (loginEntity != null && loginEntity.getData() != null) {
                            MyApplication.loginEntity = loginEntity;
                            //设置缓存信息
                            setSP(loginEntity.getData());
                            MainActivity.start(LoginActivity.this);
                            finish();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(LoginActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(LoginActivity.this);
                MyUtils.showToast(LoginActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);

    }


    private void setSP(LoginEntity.DataBean data) {

        MyApplication.USER_ID = data.get_id();
        MyApplication.NICK_NAME = data.getNickName();
        MyApplication.USER_NAME = data.getUsername();
        if (data.getProjectName() != null && data.getProjectName().size() > 0) {
            //默认选取第一个项目
            MyApplication.PROJECT_NAME = data.getProjectName().get(0).getProjectName();
            MyApplication.PROJECT_SEND = "/" + data.getProjectName().get(0).getProjectName() + "/";
            MyApplication.PROJECT_NUM = data.getProjectName().get(0).getProjectNum();
            projectList = new String[data.getProjectName().size()];
            String topics="";
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
            SaveUtils.setString(KeyUtils.MQTT_URL,data.getMqttUrl());
            MyApplication.projectList = projectList;
            MyApplication.MOUDLE=data.getProjectName().get(0).getModules();
        }
        MyApplication.PIC_URL = data.getImage();
        MyApplication.IP = data.getIp();
        SaveUtils.setString(KeyUtils.TEL, data.getUsername());
        SaveUtils.setString(KeyUtils.PWD, data.getPassword());

        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.MQTT_URL))) {
            MyUtils.Loge(TAG,"clientId："+Build.SERIAL);
//            MQTTService.NAMESPACE = BuildConfig.APPLICATION_ID;
            MQTTServiceCommand.connect(getApplicationContext(),
                    "tcp://"+SaveUtils.getString(KeyUtils.MQTT_URL),
                    Build.SERIAL,
                    "admin",
                    "123456");
        }
    }

}
