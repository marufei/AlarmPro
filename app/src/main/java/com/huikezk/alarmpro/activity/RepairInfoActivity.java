package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.RepairGvAdapter;
import com.huikezk.alarmpro.adapter.RepairInfoFinishGvAdapter;
import com.huikezk.alarmpro.adapter.RepairInfoGvAdapter;
import com.huikezk.alarmpro.entity.RepairHistoryEntity;
import com.huikezk.alarmpro.entity.RepairInfoEntity;
import com.huikezk.alarmpro.receiver.MyReceiver;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.PictureUtil;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.huikezk.alarmpro.views.GridViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairInfoActivity extends BaseActivity implements MyReceiver.OnMyReceiverListener {

    private TextView repair_info_name_start, repair_info_name_end, repair_info_time_start,
            repair_info_time_end, repair_info_content_start, repair_info_content_end;
    private GridViewForScrollView repair_info_gv_start, repair_info_gv_end;
    private String TAG = "RepairInfoActivity";
    private RepairInfoGvAdapter adapter_start;
    private RepairInfoFinishGvAdapter adapter_finish;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_info);
        setBack(true);
        ToolBarStyle(1);
        setTitle("报修详情");
        initView();
        initData();
        initReceiver();

    }

    private void initView() {
        repair_info_name_start = findViewById(R.id.repair_info_name_start);
        repair_info_name_end = findViewById(R.id.repair_info_name_end);
        repair_info_time_start = findViewById(R.id.repair_info_time_start);
        repair_info_time_end = findViewById(R.id.repair_info_time_end);
        repair_info_content_start = findViewById(R.id.repair_info_content_start);
        repair_info_content_end = findViewById(R.id.repair_info_content_end);
        repair_info_gv_start = findViewById(R.id.repair_info_gv_start);
        repair_info_gv_end = findViewById(R.id.repair_info_gv_end);
        adapter_start = new RepairInfoGvAdapter(this);
        repair_info_gv_start.setAdapter(adapter_start);
        adapter_finish = new RepairInfoFinishGvAdapter(this);
        repair_info_gv_end.setAdapter(adapter_finish);
    }

    public static void start(Context context, String repairId) {
        Intent intent = new Intent();
        intent.putExtra("repairId", repairId);
        intent.setClass(context, RepairInfoActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
        final String repairId = getIntent().getStringExtra("repairId");
        if (TextUtils.isEmpty(repairId)) {
            return;
        }
        showLoadingAnim(this);
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.REPAIR_INFO + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(RepairInfoActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        RepairInfoEntity repairInfoEntity = gson.fromJson(response, RepairInfoEntity.class);
                        if (repairInfoEntity != null && repairInfoEntity.getData() != null) {
                            setViews(repairInfoEntity.getData());
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(RepairInfoActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "e:" + e.getMessage());
                    MyUtils.Loge(TAG, "e:" + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(RepairInfoActivity.this);
                MyUtils.showToast(RepairInfoActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("history", "true");
                map.put("id", repairId);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(RepairInfoActivity.this).addToRequestQueue(stringRequest);
    }

    private void setViews(RepairInfoEntity.DataBean data) {
        if (!TextUtils.isEmpty(data.getNickName())) {
            repair_info_name_start.setText(data.getNickName());
        }
        if (!TextUtils.isEmpty(data.getFinishNickName())) {
            repair_info_name_end.setText(data.getFinishNickName());
        }
        if (!TextUtils.isEmpty(data.getDatetime())) {
            repair_info_time_start.setText(data.getDatetime());
        }
        if (!TextUtils.isEmpty(data.getFinishDatetime())) {
            repair_info_time_end.setText(data.getFinishDatetime());
        }
        if (!TextUtils.isEmpty(data.getRepairInfo())) {
            repair_info_content_start.setText(data.getRepairInfo());
        }
        if (!TextUtils.isEmpty(data.getFinishRepairInfo())) {
            repair_info_content_end.setText(data.getFinishRepairInfo());
        }
        if (data.getImgs() != null) {
            List<String> list = new Gson().fromJson(data.getImgs(), new TypeToken<List<String>>() {
            }.getType());
            if (list != null) {
                adapter_start.setListData(list);
                adapter_start.notifyDataSetChanged();
            }
        }
        if (data.getFinishImgs() != null) {
            List<String> list = new Gson().fromJson(data.getFinishImgs(), new TypeToken<List<String>>() {
            }.getType());
            if (list != null) {
                adapter_finish.setListData(list);
                adapter_finish.notifyDataSetChanged();
            }
        }
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
        MyUtils.Loge(TAG, "RepairInfoActivity--收到广播");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

    }
}
