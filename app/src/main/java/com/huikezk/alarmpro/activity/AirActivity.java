package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.huikezk.alarmpro.adapter.AirLvAdapter;
import com.huikezk.alarmpro.entity.AirEntity;
import com.huikezk.alarmpro.entity.ProjectInfoEntity;
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

public class AirActivity extends BaseActivity implements MyReceiver.OnMyReceiverListener{
    private String TAG="AirActivity";
    private String title;
    private ListView air_lv;
    private AirLvAdapter adapter;
    private List<AirEntity.DataBean> listData = new ArrayList<>();
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
        initListener();
        initReceiver();
    }

    private void initListener() {
        adapter.setOnAirClickListener(new AirLvAdapter.OnAirClickListener() {
            @Override
            public void onClick(int pos1, int pos2) {
                AirInfoActivity.start(AirActivity.this,title,listData.get(pos1).getName(),listData.get(pos1).getInfo().get(pos2));
            }
        });
    }

    private void initView() {
        air_lv = findViewById(R.id.air_lv);
        adapter = new AirLvAdapter(this);
        air_lv.setAdapter(adapter);
    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        setTitle(title);
        getProjectInfo();
    }


    public static void start(Context context, String title) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.setClass(context, AirActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取项目详细信息
     */
    public void getProjectInfo() {
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.PROJECT_INFO + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "url::" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        AirEntity airEntity = gson.fromJson(response, AirEntity.class);
                        if (airEntity != null && airEntity.getData() != null) {
                            setView(airEntity.getData());
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(AirActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "exception:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyUtils.showToast(AirActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("deviceName", title);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AirActivity.this).addToRequestQueue(stringRequest);
    }

    private void setView(List<AirEntity.DataBean> data) {
        listData.clear();
        listData.addAll(data);
        adapter.setListData(listData);
        adapter.notifyDataSetChanged();
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
        MyUtils.Loge(TAG,"AirActivity--收到广播");
        getCurrentActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver!=null) {
            unregisterReceiver(myReceiver);
        }

    }
}
