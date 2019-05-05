package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
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
import com.huikezk.alarmpro.adapter.TableLvAdapter;
import com.huikezk.alarmpro.entity.FanEntity;
import com.huikezk.alarmpro.entity.LightEntity;
import com.huikezk.alarmpro.entity.TableEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.huikezk.alarmpro.views.DialogTableView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableActivity extends BaseActivity implements View.OnClickListener {

    private ListView table_lv;
    private String title;
    private TableLvAdapter adapter;
    private TextView table_null;
    private List<TableEntity.DataBean> listData = new ArrayList<>();
    private DialogTableView dialogTableView;
    private String sendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setBack(true);
        ToolBarStyle(1);
        setMenu("添加", R.color.white);
        initView();
        initData();
        initListener();

    }

    private void initListener() {
        table_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showAlertDialog("提示",
                        "确定要删除吗？",
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MyUtils.Loge(TAG, "选中了" + position);
                                delTable(listData.get(position).getDeviceId());
                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                return false;
            }
        });
        adapter.setOnSwitchListener(new TableLvAdapter.OnSwitchListener() {
            @Override
            public void onOpen(final int position) {
                showAlertDialog("提示",
                        "确定要打开吗？",
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MyUtils.Loge(TAG, "选中了" + position);
                                String topic=sendName+listData.get(position).getDeviceId()+"/阀门控制/order";
                                publish(topic,"开");

                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }

            @Override
            public void onClose(final int position) {
                showAlertDialog("提示",
                        "确定要关闭吗？",
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MyUtils.Loge(TAG, "选中了" + position);
                                String topic=sendName+listData.get(position).getDeviceId()+"/阀门控制/order";
                                publish(topic,"关");
                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }

            @Override
            public void onRefresh(int position) {
                String topic=sendName+listData.get(position).getDeviceId()+"/刷新度数/order";
                publish(topic,"刷新");
            }
        });

        dialogTableView.setOnEventClickListenner(new DialogTableView.OnEventClickListenner() {
            @Override
            public void onSure(String describe, String deviceid, String number) {
                addTable(describe, deviceid, number);
            }
        });

    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        sendName = SaveUtils.getString(KeyUtils.PROJECT_SEND) + title+"/";
        setTitle(title);
        adapter = new TableLvAdapter(this, sendName);
        table_lv.setAdapter(adapter);
        tableList();
    }

    private void initView() {
        table_lv = findViewById(R.id.table_lv);
        table_null = findViewById(R.id.table_null);
        dialogTableView = new DialogTableView(this, R.layout.dialog_table);
    }

    public static void start(Context context, String title) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.setClass(context, TableActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取电表
     */
    public void tableList() {
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.GET_TABLE + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "url::" + url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        TableEntity tableEntity = gson.fromJson(response, TableEntity.class);
                        if (tableEntity != null && tableEntity.getData() != null) {
                            if (tableEntity.getData().size() > 0) {
                                table_null.setVisibility(View.GONE);
                                table_lv.setVisibility(View.VISIBLE);
                                listData.clear();
                                listData.addAll(tableEntity.getData());
                                adapter.setListData(listData);
                                adapter.notifyDataSetChanged();
                            } else {
                                table_null.setVisibility(View.VISIBLE);
                                table_lv.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(TableActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "exception:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyUtils.showToast(TableActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(TableActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 删除电表
     */
    private void delTable(final String deviceId) {
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.DEL_TABLE + SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "url::" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        MyUtils.showToast(TableActivity.this, "删除成功");
                        tableList();
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(TableActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "exception:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(TableActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("deviceId", deviceId);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(TableActivity.this).addToRequestQueue(stringRequest);
    }

    /**
     * 添加电表
     */
    private void addTable(final String describe, final String deviceid, final String number) {
        String url = SaveUtils.getString(KeyUtils.PROJECT_IP) + HttpsConts.ADD_TABLE +SaveUtils.getString(KeyUtils.PROJECT_NUM);
        MyUtils.Loge(TAG, "url::" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        MyUtils.showToast(TableActivity.this, "添加成功");
                        tableList();
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(TableActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "exception:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(TableActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("describe", describe);
                map.put("deviceId", deviceid);
                map.put("numbers", number);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(TableActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                dialogTableView.showDialog();
                break;
        }
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        tableList();
    }
}
