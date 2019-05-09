package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.AlarmHistoryLvAdapter;
import com.huikezk.alarmpro.entity.AlarmHistoryEntity;
import com.huikezk.alarmpro.receiver.MyReceiver;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmActivity extends BaseActivity implements MyReceiver.OnMyReceiverListener{

    private ListView alarm_lv;
    private String TAG = "AlarmActivity";
    private AlarmHistoryLvAdapter adapter;
    private List<AlarmHistoryEntity.DataBean> listData = new ArrayList<>();
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setBack(true);
        ToolBarStyle(1);
        setTitle("报警记录");
        initView();
        initData();
        initListener();
        initReceiver();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new AlarmHistoryLvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos1, int pos2) {
                if (listData != null && listData.get(pos1) != null
                        && !TextUtils.isEmpty(listData.get(pos1).getTitle())
                        && listData.get(pos1).getRecordList() != null
                        && !TextUtils.isEmpty(listData.get(pos1).getRecordList().get(pos2))) {
                    AlarmHistoryActivity.start(
                            AlarmActivity.this,
                            listData.get(pos1).getTitle(),
                            listData.get(pos1).getRecordList().get(pos2));
                }
            }
        });
    }

    private void initData() {
        getAlarmHistory();
    }

    private void initView() {
        alarm_lv = findViewById(R.id.alarm_lv);
        adapter = new AlarmHistoryLvAdapter(this);
        alarm_lv.setAdapter(adapter);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AlarmActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取报警记录
     */
    private void getAlarmHistory() {
        showLoadingAnim(this);
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.ALARM_HISTORY + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(AlarmActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        AlarmHistoryEntity alarmHistoryEntity = gson.fromJson(response, AlarmHistoryEntity.class);
                        if (alarmHistoryEntity != null && alarmHistoryEntity.getData() != null) {
                            listData.clear();
                            listData.addAll(alarmHistoryEntity.getData());
                            adapter.setListData(alarmHistoryEntity.getData());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(AlarmActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(AlarmActivity.this);
                MyUtils.showToast(AlarmActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AlarmActivity.this).addToRequestQueue(stringRequest);
    }


    private void initReceiver() {
        myReceiver = new MyReceiver();
        myReceiver.setOnMyReceive(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("myReceiver");
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onMyReceiver(Context context, Intent intent) {
        MyUtils.Loge(TAG,"AlarmActivity--收到广播");
        getAlarmHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver!=null) {
            unregisterReceiver(myReceiver);
        }

    }
}
