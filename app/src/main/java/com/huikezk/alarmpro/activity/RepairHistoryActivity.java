package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.RepairHistoryLvAdapter;
import com.huikezk.alarmpro.entity.RepairRecordEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class RepairHistoryActivity extends BaseActivity implements OnClickListener {

    private ListView repair_history_lv;
    private ImageButton myBack;
    private TextView repair_history_start;
    private TextView repair_history_end;
    private EditText repair_history_et_search;
    /**
     * 开始时间，结束时间，关键字
     */
    private String startTime, endTime;
    private String TAG = "RepairHistoryActivity";
    private RepairHistoryLvAdapter adapter;
    private List<RepairRecordEntity.DataBean> repairList = new ArrayList<>();
    /**
     * 当前年月日
     */
    private int currentYear, currentMonth, currentMonthDay;
    private int oldYear, oldMonth, oldMonthDay;
    private TextView repair_history_null;
    /**
     * 最大日期
     */
    private int maxYear, maxMonth, maxMonthDay;
    private String oldMonthStr;
    private String oldMonthDayStr;
    private String currentMonthStr;
    private String currentMonthDayStr;
    private Button repair_history_btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_history);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        repair_history_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepairInfoActivity.start(RepairHistoryActivity.this, repairList.get(position).get_id());
            }
        });

        repair_history_et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getRepairHistory();
                }
                return false;
            }
        });
        repair_history_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()<1){
                    getRepairHistory();
                }
            }
        });

    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RepairHistoryActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        repair_history_lv = findViewById(R.id.repair_history_lv);
        adapter = new RepairHistoryLvAdapter(this);
        repair_history_lv.setAdapter(adapter);
        myBack = findViewById(R.id.myBack);
        myBack.setOnClickListener(this);
        repair_history_start = findViewById(R.id.repair_history_start);
        repair_history_start.setOnClickListener(this);
        repair_history_end = findViewById(R.id.repair_history_end);
        repair_history_end.setOnClickListener(this);
        repair_history_et_search = findViewById(R.id.repair_history_et_search);
        repair_history_null = findViewById(R.id.repair_history_null);
        repair_history_btn_search=findViewById(R.id.repair_history_btn_search);
        repair_history_btn_search.setOnClickListener(this);
    }

    private void initData() {
        setDate();
        getRepairHistory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myBack:
                finish();
                break;
            case R.id.repair_history_start:
                showTimeDialog(oldYear, oldMonth, oldMonthDay, 0);
                break;
            case R.id.repair_history_end:
                showTimeDialog(currentYear, currentMonth, currentMonthDay, 1);
                break;
            case R.id.repair_history_btn_search:
                getRepairHistory();
                break;
        }
    }

    /**
     * 获取报修历史
     */
    public void getRepairHistory() {
        showLoadingAnim(this);
        String url;
        if (!TextUtils.isEmpty(repair_history_et_search.getText().toString())) {
            url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.REPAIR_HISTORY + SaveUtils.getString(KeyUtils.PROJECT_NUM)
                    + "?startTime=" + startTime
                    + "&stopTime=" + endTime
                    + "&keyword=" + repair_history_et_search.getText().toString();
        } else {
            url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.REPAIR_HISTORY + SaveUtils.getString(KeyUtils.PROJECT_NUM)
                    + "?startTime=" + startTime
                    + "&stopTime=" + endTime;
        }
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(RepairHistoryActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        RepairRecordEntity repairRecordEntity = gson.fromJson(response, RepairRecordEntity.class);
                        if (repairRecordEntity != null && repairRecordEntity.getData() != null) {
                            setView(repairRecordEntity.getData());
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(RepairHistoryActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(RepairHistoryActivity.this);
                MyUtils.showToast(RepairHistoryActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(RepairHistoryActivity.this).addToRequestQueue(stringRequest);
    }

    private void setView(List<RepairRecordEntity.DataBean> data) {
        repairList.clear();
        repairList.addAll(data);
        if (repairList.size() > 0) {
            repair_history_null.setVisibility(View.GONE);
            repair_history_lv.setVisibility(View.VISIBLE);
            adapter.setListData(repairList);
            adapter.notifyDataSetChanged();
        } else {
            repair_history_null.setVisibility(View.VISIBLE);
            repair_history_lv.setVisibility(View.GONE);
        }
    }


    public void showTimeDialog(int year, int month, int day, final int type) {

        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setAnimationStyle(R.style.style_dialog);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeEnd(maxYear, maxMonth, maxMonthDay);//控件最大所能显示的时间，即结束时间
        picker.setRangeStart(1997, 1, 1);//控件最小所能显示的时间
        picker.setSelectedItem(year, month, day);//默认选择时间
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {

            @Override
            public void onDatePicked(String year, String month, String day) {
                if (type == 0) {
                    repair_history_start.setText(year + "-" + month + "-" + day);
                    startTime = year + "-" + month + "-" + day;
                    oldYear = Integer.valueOf(year);
                    oldMonth = Integer.valueOf(month);
                    oldMonthDay = Integer.valueOf(day);

                }
                if (type == 1) {
                    repair_history_end.setText(year + "-" + month + "-" + day);
                    endTime = year + "-" + month + "-" + day;
                    currentYear = Integer.valueOf(year);
                    currentMonth = Integer.valueOf(month);
                    currentMonthDay = Integer.valueOf(day);
                }
                getRepairHistory();
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
        repair_history_start.setText(oldYear + "-" + oldMonthStr + "-" + oldMonthDayStr);
        repair_history_end.setText(currentYear + "-" + currentMonthStr + "-" + currentMonthDayStr);
        startTime = oldYear + "-" + oldMonth + "-" + oldMonthDay;
        endTime = currentYear + "-" + currentMonth + "-" + currentMonthDay;
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        getRepairHistory();
    }
}
