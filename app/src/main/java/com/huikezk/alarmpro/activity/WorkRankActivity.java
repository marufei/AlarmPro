package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.WorkRankLvAdapter;
import com.huikezk.alarmpro.entity.RepairHistoryEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.huikezk.alarmpro.views.DialogYearView;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class WorkRankActivity extends BaseActivity implements View.OnClickListener {

    private ListView work_rank_lv;
    /**
     * 年份
     */
    private int year;
    private String TAG = "WorkRankActivity";
    private WorkRankLvAdapter adapter;
    private int[] array_year = new int[3];
    private TextView work_rank_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_rank);
        setBack(true);
        ToolBarStyle(1);
        setMenu("年份", R.color.white);
        initView();
        initData();

    }

    private void initData() {
        final Calendar mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        setTitle(year + "年份工单排行");
        array_year[0] = year;
        array_year[1] = year - 1;
        array_year[2] = year - 2;
        getRankList(year);
    }

    private void initView() {
        work_rank_lv = findViewById(R.id.work_rank_lv);
        adapter = new WorkRankLvAdapter(this);
        work_rank_lv.setAdapter(adapter);
        work_rank_null=findViewById(R.id.work_rank_null);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WorkRankActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取年报修维修排行列表
     */
    public void getRankList(final int year) {
        showLoadingAnim(this);
        String url = MyApplication.IP + HttpsConts.WORK_RANK + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(WorkRankActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson=new Gson();
                        RepairHistoryEntity repairHistoryEntity=gson.fromJson(response,RepairHistoryEntity.class);
                        if (repairHistoryEntity!=null&&repairHistoryEntity.getData()!=null){
                            setViews(repairHistoryEntity.getData());
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(WorkRankActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(WorkRankActivity.this);
                MyUtils.showToast(WorkRankActivity.this, "网络有问题");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("year",String.valueOf(year));
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(WorkRankActivity.this).addToRequestQueue(stringRequest);
    }

    private void setViews(RepairHistoryEntity.DataBeanX data) {
        if (data.getData().size()>0) {
            work_rank_lv.setVisibility(View.VISIBLE);
            work_rank_null.setVisibility(View.GONE);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }else {
            work_rank_lv.setVisibility(View.GONE);
            work_rank_null.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                DialogYearView dialogBottomView = new DialogYearView(this, R.layout.dialog_bottom_year, array_year);
                dialogBottomView.setOnEventClickListenner(new DialogYearView.OnEventClickListenner() {
                    @Override
                    public void onContent1() {
                        setTitle(array_year[0] + "年份工单排行");
                        getRankList(array_year[0]);
                    }

                    @Override
                    public void onContent2() {
                        setTitle(array_year[1] + "年份工单排行");
                        getRankList(array_year[1]);
                    }

                    @Override
                    public void onContent3() {
                        setTitle(array_year[2] + "年份工单排行");
                        getRankList(array_year[2]);
                    }
                });
                dialogBottomView.showDialog();
                break;
        }
    }
}
