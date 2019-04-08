package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.TimeInfoLvAdapter;
import com.huikezk.alarmpro.entity.TimeInfoEntity;
import com.huikezk.alarmpro.entity.TimeManagerEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class TimeInfoActivity extends BaseActivity implements View.OnClickListener {

    private ListView time_info_lv;
    private TextView time_info_title;
    private ImageButton time_info_back;
    private Button time_info_save;
    private String type = "";
    private String name = "";
    private String TAG = "TimeInfoActivity";
    private TimeInfoLvAdapter adapter;
    private List<TimeInfoEntity.DataBean> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_info);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        adapter.setOnTimeSelectListener(new TimeInfoLvAdapter.OnTimeSelectListener() {
            @Override
            public void onOpenClick(int position) {
                onTimePicker(position, 0);
            }

            @Override
            public void onCloseClick(int position) {
                onTimePicker(position, 1);
            }
        });
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(name)) {
            time_info_title.setText(name);
            getTimeInfo();
        }
    }

    public static void start(Context context, String type, String name) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("name", name);
        intent.setClass(context, TimeInfoActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        time_info_lv = findViewById(R.id.time_info_lv);
        adapter = new TimeInfoLvAdapter(this);
        time_info_lv.setAdapter(adapter);
        time_info_title = findViewById(R.id.time_info_title);
        time_info_back = findViewById(R.id.time_info_back);
        time_info_back.setOnClickListener(this);
        time_info_save = findViewById(R.id.time_info_save);
        time_info_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_info_back:
                finish();
                break;
            case R.id.time_info_save:
                List<TimeInfoEntity.DataBean> updateList = listData;
                updateList.remove(0);
                updateTimeTable(updateList);
                break;
        }
    }

    /**
     * 获取时间表
     */
    public void getTimeInfo() {
        showLoadingAnim(this);
        String url = MyApplication.IP + HttpsConts.TIME_INFO + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(TimeInfoActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        TimeInfoEntity timeInfoEntity = gson.fromJson(response, TimeInfoEntity.class);
                        if (timeInfoEntity != null && timeInfoEntity.getData() != null) {
                            listData.clear();
                            TimeInfoEntity.DataBean dataHeader = new TimeInfoEntity.DataBean();
                            dataHeader.setOpenTime("开");
                            dataHeader.setCloseTime("关");
                            dataHeader.setWeek(0);
                            listData.add(dataHeader);
                            listData.addAll(timeInfoEntity.getData());
                            adapter.setList(listData);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(TimeInfoActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(TimeInfoActivity.this);
                MyUtils.showToast(TimeInfoActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", type);
                map.put("name", name);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(TimeInfoActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 更新时间表
     */
    private void updateTimeTable(final List<TimeInfoEntity.DataBean> updateList) {
        showLoadingAnim(this);
        String url = MyApplication.IP + HttpsConts.SET_TIME_TABLE + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(TimeInfoActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        MyUtils.showToast(TimeInfoActivity.this,"保存成功");
                        finish();
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(TimeInfoActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(TimeInfoActivity.this);
                MyUtils.showToast(TimeInfoActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", type);

                Gson gson=new Gson();
                String gsonData = gson.toJson(updateList);
                MyUtils.Loge(TAG,"times:"+gsonData);
                map.put("times", gsonData);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(TimeInfoActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 时间选择器
     *
     * @param position
     * @param type
     */
    public void onTimePicker(final int position, final int type) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setTextPadding(ConvertUtils.toPx(this, 15));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                MyUtils.Loge(TAG, hour + ":" + minute);
                if (type == 0) {
                    listData.get(position).setOpenTime(hour + ":" + minute);
                } else {
                    listData.get(position).setCloseTime(hour + ":" + minute);
                }
                adapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        getTimeInfo();
    }
}
