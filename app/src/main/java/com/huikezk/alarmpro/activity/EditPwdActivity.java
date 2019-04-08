package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
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

import static com.huikezk.alarmpro.utils.ActivityUtil.exitAll;

public class EditPwdActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "EditPwdActivity";
    private EditText edit_pwd_old, edit_pwd_new, edit_pwd_again;
    private Button edit_pwd_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        setBack(true);
        setTitle("修改密码");
        ToolBarStyle(1);
        initView();
    }

    private void initView() {
        edit_pwd_old = findViewById(R.id.edit_pwd_old);
        edit_pwd_new = findViewById(R.id.edit_pwd_new);
        edit_pwd_again = findViewById(R.id.edit_pwd_again);
        edit_pwd_commit = findViewById(R.id.edit_pwd_commit);
        edit_pwd_commit.setOnClickListener(this);
    }


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, EditPwdActivity.class);
        context.startActivity(intent);
    }

    public void editPwd() {
        showLoadingAnim(this);
        String url = HttpsConts.BASE_URL + HttpsConts.EDIT_PWD
                + "?userId=" + MyApplication.USER_ID
                + "&password=" + edit_pwd_old.getText().toString().trim()
                + "&newPassword=" + edit_pwd_new.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(EditPwdActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        MyUtils.Loge("lbw", "===mqtt onDestroy");
                        MQTTServiceCommand.disconnect(getApplicationContext());
                        SaveUtils.removeAllData();
                        MyApplication.finishAllActivity();
                        ActivityUtil.exitAll();
                        LoginActivity.start(EditPwdActivity.this);
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(EditPwdActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(EditPwdActivity.this);
                MyUtils.showToast(EditPwdActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(EditPwdActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_pwd_commit:
                if (TextUtils.isEmpty(edit_pwd_old.getText().toString())) {
                    MyUtils.showToast(this, "请先输入旧密码");
                    return;
                }
                if (TextUtils.isEmpty(edit_pwd_new.getText().toString())) {
                    MyUtils.showToast(this, "请先输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(edit_pwd_again.getText().toString())) {
                    MyUtils.showToast(this, "请先输入新密码");
                    return;
                }
                if (!edit_pwd_new.getText().toString().trim().equals(edit_pwd_again.getText().toString().trim())) {
                    MyUtils.showToast(this, "新密码不一致");
                    return;
                }
                editPwd();
                break;
        }
    }
}
