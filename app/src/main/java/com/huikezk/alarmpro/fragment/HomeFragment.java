package com.huikezk.alarmpro.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.activity.AirActivity;
import com.huikezk.alarmpro.activity.ChangeProActivity;
import com.huikezk.alarmpro.activity.ConditionActivity;
import com.huikezk.alarmpro.activity.FanActivity;
import com.huikezk.alarmpro.activity.LightActivity;
import com.huikezk.alarmpro.activity.LoginActivity;
import com.huikezk.alarmpro.activity.MainActivity;
import com.huikezk.alarmpro.activity.PartActivity;
import com.huikezk.alarmpro.activity.SplashActivity;
import com.huikezk.alarmpro.activity.TableActivity;
import com.huikezk.alarmpro.activity.TimeManagerActivity;
import com.huikezk.alarmpro.entity.BannerEntity;
import com.huikezk.alarmpro.entity.UpdateEntity;
import com.huikezk.alarmpro.receiver.MyReceiver;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.GlideImageLoader;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.UpdateManger;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.youth.banner.Banner;

import net.igenius.mqttservice.MQTTServiceCommand;

import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MaRufei
 * on 2019/1/3.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, MyReceiver.OnMyReceiverListener {
    private View view;
    private LinearLayout home_part1, home_part2, home_part3, home_part4,
            home_part5, home_part6, home_part7, home_part8, home_part9;
    private TextView home_pro_name, home_pro_change;
    private static final int PRO_CHANGE = 0x004;
    private Banner banner;
    private MyReceiver myReceiver;
    private boolean isReceiverRegister;

    @Override
    protected void lazyLoad() {
        MyUtils.Loge(TAG,"HomeFragment--lazyLoad");
    }

    @Override
    protected void unLazyLoad() {
        MyUtils.Loge(TAG,"HomeFragment---unLazyLoad");
        try {
            if (myReceiver != null && isReceiverRegister == true) {
                getActivity().unregisterReceiver(myReceiver);
                myReceiver=null;
            }
        }catch (Exception e){}
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = View.inflate(getActivity(), R.layout.fragment_home, null);
        initViews();
        getBanner();
        initListener();
        initReceiver();
        return view;
    }

    public MyReceiver getMyReceiver() {
        return myReceiver;
    }

    private void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
    }

    private void initData() {

        home_pro_name.setText(SaveUtils.getString(KeyUtils.PROJECT_NAME));
        if (MyApplication.projectList != null) {
            if (MyApplication.projectList.length > 1) {
                home_pro_change.setVisibility(View.VISIBLE);
            } else {
                home_pro_change.setVisibility(View.GONE);
            }
        } else {
            MQTTServiceCommand.disconnect(getActivity().getApplicationContext());
            MyApplication.finishAllActivity();
            ActivityUtil.exitAll();
            SplashActivity.start(getActivity());
        }


    }

    private void initViews() {
        banner = view.findViewById(R.id.banner);
        home_pro_name = view.findViewById(R.id.home_pro_name);
        home_pro_change = view.findViewById(R.id.home_pro_change);
        home_pro_change.setOnClickListener(this);


        home_part1 = view.findViewById(R.id.home_part1);
        home_part1.setOnClickListener(this);
        home_part2 = view.findViewById(R.id.home_part2);
        home_part2.setOnClickListener(this);
        home_part3 = view.findViewById(R.id.home_part3);
        home_part3.setOnClickListener(this);
        home_part4 = view.findViewById(R.id.home_part4);
        home_part4.setOnClickListener(this);
        home_part5 = view.findViewById(R.id.home_part5);
        home_part5.setOnClickListener(this);
        home_part6 = view.findViewById(R.id.home_part6);
        home_part6.setOnClickListener(this);
        home_part7 = view.findViewById(R.id.home_part7);
        home_part7.setOnClickListener(this);
        home_part8 = view.findViewById(R.id.home_part8);
        home_part8.setOnClickListener(this);
        home_part9 = view.findViewById(R.id.home_part9);
        home_part9.setOnClickListener(this);


    }

    /**
     * 获取banner,类目
     */
    public void getBanner() {
        String url = HttpsConts.BASE_URL + HttpsConts.BANNER;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        BannerEntity bannerEntity = gson.fromJson(response, BannerEntity.class);
                        if (bannerEntity != null && bannerEntity.getData() != null) {
                            setView(bannerEntity.getData());
                        }
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
                MyUtils.Loge(TAG, "getBanner--------网络有问题");
                MyUtils.showToast(getActivity(), "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void setView(List<String> data) {
        MyUtils.Loge(TAG, "data.size():" + data.size());
        if (data.size() > 0) {
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            banner.setImages(data);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_part1:
                if (!isHaveMoudle("隔油间监测")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                PartActivity.start(getActivity(), "隔油间监测");
                break;
            case R.id.home_part2:
                if (!isHaveMoudle("燃气监测")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                MyUtils.showToast(getActivity(), "智能楼宇系统：敬请期待");
                break;
            case R.id.home_part3:
                if (!isHaveMoudle("照明监控")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                LightActivity.start(getActivity(), "照明监控");
                break;
            case R.id.home_part4:
                if (!isHaveMoudle("空调监控")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                ConditionActivity.start(getActivity(), "空调监控");
                break;
            case R.id.home_part5:
                if (!isHaveMoudle("风机监控")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                FanActivity.start(getActivity(), "风机监控");
                break;
            case R.id.home_part6:
                if (!isHaveMoudle("电力监控")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                MyUtils.showToast(getActivity(), "智能楼宇系统：敬请期待");
                break;
            case R.id.home_part7:
//                if (!isHaveMoudle("远传抄表")) {
//                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
//                    return;
//                }
                TableActivity.start(getActivity(), "远传抄表");
                break;
            case R.id.home_part8:
                if (!isHaveMoudle("空气质量")) {
                    MyUtils.showToast(getActivity(), "暂未开通，请联系商务");
                    return;
                }
                AirActivity.start(getActivity(), "空气质量");
                break;
            case R.id.home_part9:
                TimeManagerActivity.start(getActivity());
                break;
            case R.id.home_pro_change:
                Intent intent = new Intent(getActivity(), ChangeProActivity.class);
                startActivityForResult(intent, PRO_CHANGE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRO_CHANGE && resultCode == RESULT_OK) {
            if (!TextUtils.isEmpty(data.getExtras().getString("name"))) {
                home_pro_name.setText(data.getExtras().getString("name"));

            }
        }
    }

    public boolean isHaveMoudle(String moudle) {
//        MyUtils.Loge(TAG, "模块：" + MyApplication.MOUDLE);
        if (null != MyApplication.MOUDLE && MyApplication.MOUDLE.contains(moudle)) {
            return true;
        } else {
            return false;
        }
    }

    private void initReceiver() {
        isReceiverRegister=true;
        myReceiver = new MyReceiver();
        MyUtils.Loge(TAG,"HomeFragment创建receiver");
        myReceiver.setOnMyReceive(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("myReceiver");
        getActivity().registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onMyReceiver(Context context, Intent intent) {
        MyUtils.Loge(TAG, "HomeFragment--收到广播");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        MyUtils.Loge(TAG,"HomeFragment---onDestroy");
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
        }

    }
}
