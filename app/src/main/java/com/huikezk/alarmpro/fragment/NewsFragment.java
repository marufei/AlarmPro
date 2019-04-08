package com.huikezk.alarmpro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.huikezk.alarmpro.activity.AirActivity;
import com.huikezk.alarmpro.activity.AirInfoActivity;
import com.huikezk.alarmpro.activity.ConditionActivity;
import com.huikezk.alarmpro.activity.ConditionInfoActivity;
import com.huikezk.alarmpro.activity.FanActivity;
import com.huikezk.alarmpro.activity.FanInfoActivity;
import com.huikezk.alarmpro.activity.LightActivity;
import com.huikezk.alarmpro.activity.LightInfoActivity;
import com.huikezk.alarmpro.activity.PartActivity;
import com.huikezk.alarmpro.activity.PartInfoActivity;
import com.huikezk.alarmpro.activity.TableActivity;
import com.huikezk.alarmpro.activity.TimeManagerActivity;
import com.huikezk.alarmpro.adapter.AlarmLvAdapter;
import com.huikezk.alarmpro.entity.AirEntity;
import com.huikezk.alarmpro.entity.AlarmEntity;
import com.huikezk.alarmpro.entity.ConditionEntity;
import com.huikezk.alarmpro.entity.FanEntity;
import com.huikezk.alarmpro.entity.LightEntity;
import com.huikezk.alarmpro.entity.ProjectInfoEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MaRufei
 * on 2019/1/3.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class NewsFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private String TAG = "NewsFragment";
    private ListView fragment_news_lv;
    private AlarmLvAdapter adapter;

    private TextView news_null;
    private List<String> list;
    private AlarmEntity alarmEntity;

    @Override
    protected void lazyLoad() {
        list = SaveUtils.getAllEndWithKey("alarm");
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = View.inflate(getActivity(), R.layout.fragment_news, null);
        initViews();
        initListener();
        initData();
        return view;
    }

    private void initListener() {
        fragment_news_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String jsonStr = SaveUtils.getString(list.get(position));
                    Gson gson = new Gson();
                    alarmEntity = gson.fromJson(jsonStr, AlarmEntity.class);
                    if (alarmEntity != null && !TextUtils.isEmpty(alarmEntity.getDeviceName())) {
                        switch (alarmEntity.getDeviceName()) {
                            case "隔油间监测":
                                getProjectInfo("隔油间监测");
//                                PartActivity.start(getActivity(), alarmEntity.getDeviceName());
                                break;
                            case "燃气监测":
                                MyUtils.showToast(getActivity(), "智能楼宇系统：敬请期待");
                                break;
                            case "照明监控":
                                getProjectInfo("照明监控");
//                                LightActivity.start(getActivity(), alarmEntity.getDeviceName());
                                break;
                            case "空调监控":
                                getProjectInfo("空调监控");
//                                ConditionActivity.start(getActivity(), alarmEntity.getDeviceName());
                                break;
                            case "风机监控":
                                getProjectInfo("风机监控");
//                                FanActivity.start(getActivity(), alarmEntity.getDeviceName());
                                break;
                            case "电力监控":
                                MyUtils.showToast(getActivity(), "智能楼宇系统：敬请期待");
                                break;
                            case "远传抄表":
                                TableActivity.start(getActivity(), alarmEntity.getDeviceName());
                                break;
                            case "空气质量":
                                AirActivity.start(getActivity(), alarmEntity.getDeviceName());
                                break;
                            case "时间表管理":
                                TimeManagerActivity.start(getActivity());
                                break;
                            default:
                                break;

                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (list != null && list.size() > 0) {
            List<String> proList=new ArrayList<>();
            for (String str:list){
                if (str.contains(MyApplication.PROJECT_NAME)){
                    proList.add(str);
                }
            }
            if (proList.size()>0) {
                news_null.setVisibility(View.GONE);
                fragment_news_lv.setVisibility(View.VISIBLE);
                adapter.setListData(proList);
                adapter.notifyDataSetChanged();
            }else {
                news_null.setVisibility(View.VISIBLE);
                fragment_news_lv.setVisibility(View.GONE);
            }
        } else {
            news_null.setVisibility(View.VISIBLE);
            fragment_news_lv.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        news_null = view.findViewById(R.id.news_null);
        fragment_news_lv = view.findViewById(R.id.fragment_news_lv);
        adapter = new AlarmLvAdapter(getActivity());
        fragment_news_lv.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    /**
     * 获取项目详细信息
     */
    public void getProjectInfo(final String title) {
        String url = MyApplication.IP + HttpsConts.PROJECT_INFO + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "url::" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        setData(response);

                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(getActivity(), status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(getActivity(), "网络有问题");
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
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void setData(String response) {
        Gson gson = new Gson();
        switch (alarmEntity.getDeviceName()) {
            case "隔油间监测":
                ProjectInfoEntity projectInfoEntity = gson.fromJson(response, ProjectInfoEntity.class);
                if (projectInfoEntity != null && projectInfoEntity.getData() != null) {
                    for (int i = 0; i < projectInfoEntity.getData().size(); i++) {
                        if (alarmEntity.getNumber().equals(projectInfoEntity.getData().get(i))) {
                            PartInfoActivity.start(getActivity(),
                                    "隔油间监测",
                                    projectInfoEntity.getData().get(i));
                        }
                    }
                }
                break;
            case "燃气监测":
                MyUtils.showToast(getActivity(), "智能楼宇系统：敬请期待");
                break;
            case "照明监控":
//                LightActivity.start(getActivity(), alarmEntity.getDeviceName());
                LightEntity lightEntity = gson.fromJson(response, LightEntity.class);
                if (lightEntity != null && lightEntity.getData() != null) {
                    for (int i = 0; i < lightEntity.getData().size(); i++) {
                        if (alarmEntity.getNumber().equals(lightEntity.getData().get(i).getName())) {
                            LightInfoActivity.start(getActivity(),
                                    "照明监控",
                                    lightEntity.getData().get(i));
                        }
                    }
                }
                break;
            case "空调监控":
//                ConditionActivity.start(getActivity(), alarmEntity.getDeviceName());
                ConditionEntity conditionEntity = gson.fromJson(response, ConditionEntity.class);
                if (conditionEntity != null && conditionEntity.getData() != null) {
                    for (int i = 0; i < conditionEntity.getData().size(); i++) {
                        if (alarmEntity.getNumber().equals(conditionEntity.getData().get(i).getName())) {
                            ConditionInfoActivity.start(getActivity(),
                                    "空调监控",
                                    conditionEntity.getData().get(i).getName(),
                                    conditionEntity.getData().get(i).getInfo());

                        }
                    }
                }
                break;
            case "风机监控":
//                FanActivity.start(getActivity(), alarmEntity.getDeviceName());
                FanEntity fanEntity = gson.fromJson(response, FanEntity.class);
                if (fanEntity != null && fanEntity.getData() != null) {
                    for (int i = 0; i < fanEntity.getData().size(); i++) {
                        if (alarmEntity.getNumber().equals(fanEntity.getData().get(i).getName())) {
                            FanInfoActivity.start(getActivity(),
                                    "风机监控",
                                    fanEntity.getData().get(i));
                        }
                    }
                }
                break;
            case "电力监控":
                MyUtils.showToast(getActivity(), "智能楼宇系统：敬请期待");
                break;
            case "远传抄表":
                TableActivity.start(getActivity(), alarmEntity.getDeviceName());
                break;
            case "空气质量":
                AirActivity.start(getActivity(), alarmEntity.getDeviceName());
                break;
            case "时间表管理":
                TimeManagerActivity.start(getActivity());
                break;
            default:
                break;
        }
    }

    @Override
    public void notifyAllActivity(String str) {
        list = SaveUtils.getAllEndWithKey("alarm");
        initData();

    }
}
