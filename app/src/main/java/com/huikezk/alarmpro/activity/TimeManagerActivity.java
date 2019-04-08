package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.TimeManagerLvAdapter;
import com.huikezk.alarmpro.entity.RepairRecordEntity;
import com.huikezk.alarmpro.entity.TimeManagerEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeManagerActivity extends BaseActivity {

    private ListView time_manager_lv;
    private String TAG = "TimeManagerActivity";
    private TimeManagerLvAdapter adapter;
    private List<TimeManagerEntity.DataBean> list=new ArrayList<>();
    private TextView time_manager_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_manager);
        setBack(true);
        ToolBarStyle(1);
        setTitle("时间表管理");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        adapter.setOnTimeSelectListener(new TimeManagerLvAdapter.OnTimeSelectListener() {
            @Override
            public void onSelected(int pos1, int pos2) {
                MyUtils.Loge(TAG,"pos1:"+pos1+"--pos2:"+pos2);
                TimeInfoActivity.start(TimeManagerActivity.this,list.get(pos1).getType(),list.get(pos1).getTimes().get(pos2).getName());
            }
        });
    }

    private void initData() {
        getTimeTable();
    }

    private void initView() {
        time_manager_null=findViewById(R.id.time_manager_null);
        time_manager_lv = findViewById(R.id.time_manager_lv);
        adapter=new TimeManagerLvAdapter(this);
        time_manager_lv.setAdapter(adapter);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TimeManagerActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取时间表
     */
    public void getTimeTable() {
        showLoadingAnim(this);
        String url = MyApplication.IP + HttpsConts.TIME_TABLE + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(TimeManagerActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        TimeManagerEntity timeManagerEntity=gson.fromJson(response,TimeManagerEntity.class);
                        if (timeManagerEntity!=null&&timeManagerEntity.getData()!=null){
                            list.clear();
                            list.addAll(timeManagerEntity.getData());
                            if (list.size()>0) {
                                time_manager_null.setVisibility(View.GONE);
                                time_manager_lv.setVisibility(View.VISIBLE);
                                adapter.setList(list);
                                adapter.notifyDataSetChanged();
                            }else {
                                time_manager_null.setVisibility(View.VISIBLE);
                                time_manager_lv.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(TimeManagerActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(TimeManagerActivity.this);
                MyUtils.showToast(TimeManagerActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(TimeManagerActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        getTimeTable();
    }
}
