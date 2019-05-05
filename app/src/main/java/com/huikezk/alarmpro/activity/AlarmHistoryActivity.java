package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.huikezk.alarmpro.adapter.AlarmRecordLvAdapter;
import com.huikezk.alarmpro.entity.AlarmHistoryEntity;
import com.huikezk.alarmpro.entity.AlarmRecordEntity;
import com.huikezk.alarmpro.entity.BaseEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.util.ConvertUtils;

public class AlarmHistoryActivity extends BaseActivity implements View.OnClickListener{

    private ImageView alarm_history_back;
    private TextView alarm_history_title;
    private TextView alarm_history_start;
    private TextView alarm_history_end;
    private ListView alarm_history_lv;
    private TextView alarm_history_null;
    private String TAG="AlarmHistoryActivity";
    private String type;
    private String info;
    private String startTime;
    private String endTime;

    /**
     * 当前年月日
     */
    private int currentYear, currentMonth, currentMonthDay;
    private int oldYear, oldMonth, oldMonthDay;
    /**
     * 最大日期
     */
    private int maxYear, maxMonth, maxMonthDay;
    private String oldMonthStr;
    private String oldMonthDayStr;
    private String currentMonthStr;
    private String currentMonthDayStr;
    private List<AlarmRecordEntity.DataBean> listData=new ArrayList<>();
    private AlarmRecordLvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_history);
        initView();
        initData();
    }

    public static void start(Context context,String type,String info) {
        Intent intent = new Intent();
        intent.putExtra("type",type);
        intent.putExtra("info",info);
        intent.setClass(context, AlarmHistoryActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
        type=getIntent().getStringExtra("type");
        info=getIntent().getStringExtra("info");
        alarm_history_title.setText(info);
        setDate();
        getAlarmHistory();
    }

    private void initView() {
        alarm_history_back=findViewById(R.id.alarm_history_back);
        alarm_history_back.setOnClickListener(this);
        alarm_history_title=findViewById(R.id.alarm_history_title);
        alarm_history_start=findViewById(R.id.alarm_history_start);
        alarm_history_start.setOnClickListener(this);
        alarm_history_end=findViewById(R.id.alarm_history_end);
        alarm_history_end.setOnClickListener(this);
        alarm_history_lv=findViewById(R.id.alarm_history_lv);
        adapter=new AlarmRecordLvAdapter(this);
        alarm_history_lv.setAdapter(adapter);
        alarm_history_null=findViewById(R.id.alarm_history_null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alarm_history_back:
                finish();
                break;
            case R.id.alarm_history_start:
                showTimeDialog(oldYear, oldMonth, oldMonthDay, 0);
                break;
            case R.id.alarm_history_end:
                showTimeDialog(currentYear, currentMonth, currentMonthDay, 1);
                break;
        }
    }

    /**
     * 报警历史详情
     */
    public void getAlarmHistory(){
        showLoadingAnim(this);
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.ALARM_INFO + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "URL:" + url);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(AlarmHistoryActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson=new Gson();
                        AlarmRecordEntity alarmRecordEntity=gson.fromJson(response,AlarmRecordEntity.class);
                        if (alarmRecordEntity!=null&&alarmRecordEntity.getData()!=null){
                            listData.clear();
                            listData.addAll(alarmRecordEntity.getData());
                            setView(listData);
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(AlarmHistoryActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(AlarmHistoryActivity.this);
                MyUtils.showToast(AlarmHistoryActivity.this, "网络有问题");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("type",type);
                map.put("info",info);
                MyUtils.Loge(TAG,"startTime:"+startTime);
                MyUtils.Loge(TAG,"stopTime:"+endTime);
                map.put("startTime",startTime);
                map.put("stopTime",endTime);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(AlarmHistoryActivity.this).addToRequestQueue(stringRequest);
    }

    private void setView(List<AlarmRecordEntity.DataBean> listData) {
        if (listData.size()>0){
            alarm_history_lv.setVisibility(View.VISIBLE);
            alarm_history_null.setVisibility(View.GONE);
            adapter.setListData(listData);
            adapter.notifyDataSetChanged();
        }else {
            alarm_history_lv.setVisibility(View.GONE);
            alarm_history_null.setVisibility(View.VISIBLE);
        }
    }

    public void showTimeDialog(int year, int month, int day, final int type) {

        final cn.qqtheme.framework.picker.DatePicker picker = new cn.qqtheme.framework.picker.DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setAnimationStyle(R.style.style_dialog);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeEnd(maxYear, maxMonth, maxMonthDay);//控件最大所能显示的时间，即结束时间
        picker.setRangeStart(1997, 1, 1);//控件最小所能显示的时间
        picker.setSelectedItem(year, month, day);//默认选择时间
        picker.setOnDatePickListener(new cn.qqtheme.framework.picker.DatePicker.OnYearMonthDayPickListener() {

            @Override
            public void onDatePicked(String year, String month, String day) {
                if (type == 0) {
                    alarm_history_start.setText(year + "-" + month + "-" + day);
                    startTime = year + "-" + month + "-" + day;
                    oldYear = Integer.valueOf(year);
                    oldMonth = Integer.valueOf(month);
                    oldMonthDay = Integer.valueOf(day);

                }
                if (type == 1) {
                    alarm_history_end.setText(year + "-" + month + "-" + day);
                    endTime = year + "-" + month + "-" + day;
                    currentYear = Integer.valueOf(year);
                    currentMonth = Integer.valueOf(month);
                    currentMonthDay = Integer.valueOf(day);
                }
                getAlarmHistory();
            }
        });
        picker.show();
    }

    /**
     * 设置日期
     */
    public void setDate() {
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentMonthDay = calendar.get(Calendar.DATE);
        maxYear = calendar.get(Calendar.YEAR);
        maxMonth = calendar.get(Calendar.MONTH) + 1;
        maxMonthDay = calendar.get(Calendar.DATE);
        calendar.clear();
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentMonthDay);
        calendar.set(Calendar.YEAR, currentYear);
        calendar.add(Calendar.DATE, -7);
        oldYear = calendar.get(Calendar.YEAR);
        oldMonth = calendar.get(Calendar.MONTH);
        oldMonthDay = calendar.get(Calendar.DATE);
        if (oldMonth < 10) {
            oldMonthStr = "0" + oldMonth;
        } else {
            oldMonthStr = String.valueOf(oldMonth);
        }
        if (oldMonthDay < 10) {
            oldMonthDayStr = "0" + oldMonthDay;
        } else {
            oldMonthDayStr = String.valueOf(oldMonthDay);
        }
        if (currentMonth < 10) {
            currentMonthStr = "0" + currentMonth;
        } else {
            currentMonthStr = String.valueOf(currentMonth);
        }
        if (currentMonthDay < 10) {
            currentMonthDayStr = "0" + currentMonthDay;
        } else {
            currentMonthDayStr = String.valueOf(currentMonthDay);
        }
        alarm_history_start.setText(oldYear + "-" + oldMonthStr + "-" + oldMonthDayStr);
        alarm_history_end.setText(currentYear + "-" + currentMonthStr + "-" + currentMonthDayStr);
        startTime = oldYear + "-" + oldMonth + "-" + oldMonthDay;
        endTime = currentYear + "-" + currentMonth + "-" + currentMonthDay;
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        getAlarmHistory();
    }
}
