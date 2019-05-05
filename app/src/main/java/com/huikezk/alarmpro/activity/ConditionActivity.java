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
import com.huikezk.alarmpro.adapter.ConditionLvAdapter;
import com.huikezk.alarmpro.entity.ConditionEntity;
import com.huikezk.alarmpro.entity.ProjectInfoEntity;
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

public class ConditionActivity extends BaseActivity {

    private ListView condition_lv;
    private String title;
    private ConditionLvAdapter adapter;
    private List<ConditionEntity.DataBean> listData=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        condition_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConditionInfoActivity.start(ConditionActivity.this,title,listData.get(position).getName(),listData.get(position).getInfo());
            }
        });
    }

    private void initData() {
        title=getIntent().getStringExtra("title");
        setTitle(title);
        getProjectInfo();
    }

    private void initView() {
        condition_lv=findViewById(R.id.condition_lv);
        adapter=new ConditionLvAdapter(this);
        condition_lv.setAdapter(adapter);
    }
    public static void start(Context context, String title) {
        Intent intent = new Intent();
        intent.putExtra("title",title);
        intent.setClass(context, ConditionActivity.class);
        context.startActivity(intent);
    }
    /**
     * 获取项目详细信息
     */
    public void getProjectInfo(){
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP)+ HttpsConts.PROJECT_INFO + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG,"url::"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        ConditionEntity conditionEntity=gson.fromJson(response,ConditionEntity.class);
                        if (conditionEntity!=null&&conditionEntity.getData()!=null){
                            setView(conditionEntity.getData());
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(ConditionActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyUtils.showToast(ConditionActivity.this, "网络有问题");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("deviceName",title);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(ConditionActivity.this).addToRequestQueue(stringRequest);
    }

    private void setView(List<ConditionEntity.DataBean> data) {
        listData.clear();
        listData.addAll(data);
        adapter.setListData(listData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        getProjectInfo();
    }
}
