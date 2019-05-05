package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.huikezk.alarmpro.adapter.LightLvAdapter;
import com.huikezk.alarmpro.entity.FanEntity;
import com.huikezk.alarmpro.entity.LightEntity;
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

public class LightActivity extends BaseActivity {

    private String title;
    private ListView light_lv;
    private LightLvAdapter adapter;
    private List<LightEntity.DataBean> listData=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        light_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LightInfoActivity.start(LightActivity.this,title,listData.get(position));
            }
        });
    }

    private void initData() {
        title=getIntent().getStringExtra("title");
        setTitle(title);
        getProjectInfo();
    }

    private void initView() {
        light_lv=findViewById(R.id.light_lv);
        adapter=new LightLvAdapter(this);
        light_lv.setAdapter(adapter);
    }

    public static void start(Context context, String title) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.setClass(context, LightActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取项目详细信息
     */
    public void getProjectInfo() {
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.PROJECT_INFO +SaveUtils.getString(KeyUtils.PROJECT_NUM);
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
                        LightEntity lightEntity=gson.fromJson(response,LightEntity.class);
                        if (lightEntity!=null&&lightEntity.getData()!=null){
                            listData.clear();
                            listData.addAll(lightEntity.getData());
                            adapter.setListData(listData);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(LightActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "exception:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyUtils.showToast(LightActivity.this, "网络有问题");
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
        VolleyUtils.getInstance(LightActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        getProjectInfo();
    }
}
