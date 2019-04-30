package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.huikezk.alarmpro.BuildConfig;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.MainVpAdapter;
import com.huikezk.alarmpro.fragment.HomeFragment;
import com.huikezk.alarmpro.fragment.MineFragment;
import com.huikezk.alarmpro.fragment.NewsFragment;
import com.huikezk.alarmpro.fragment.RepairFragment;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.huikezk.alarmpro.views.MyViewPager;

import net.igenius.mqttservice.MQTTService;
import net.igenius.mqttservice.MQTTServiceCommand;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private MyViewPager vp_show;
    private RadioButton rb0, rb1, rb2, rb3;
    private RadioGroup rg_bottom;
    private HomeFragment fragment1;
    private NewsFragment fragment2;
    private RepairFragment fragment3;
    private MineFragment fragment4;
    /**
     * 盛放fragment容器
     */
    private List<Fragment> listFragnet = new ArrayList<>();
    long[] mHits = new long[2];
    boolean showToast = true;
    private String TAG = "MainActivity";
    private RelativeLayout main_rl2, main_rl3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
//        updateUmengToken();
    }

    private void initData() {
        List<String> alarmList = SaveUtils.getAllEndWithKey("alarm");
        List<String> repair = SaveUtils.getAllEndWithKey("repair");
        showPoint(alarmList, main_rl2);
        showPoint(repair, main_rl3);

    }

    /**
     * 判断展示消息提示红点
     *
     * @param list
     * @param view
     */
    private void showPoint(List<String> list, View view) {
        if (list != null && list.size() > 0) {
            List<String> proList = new ArrayList<>();
            for (String str : list) {
                if (str.contains(MyApplication.PROJECT_NAME)) {
                    proList.add(str);
                }
            }
            if (proList.size() > 0) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        vp_show = findViewById(R.id.vp_show);
        rb0 = findViewById(R.id.rb0);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rg_bottom = findViewById(R.id.rg_bottom);
        main_rl2 = findViewById(R.id.main_rl2);
        main_rl3 = findViewById(R.id.main_rl3);
    }

    private void initEvent() {

        //设置主页第一个分页被选中
        rb0.setSelected(true);
        rb0.setChecked(true);

        fragment1 = new HomeFragment();
        fragment2 = new NewsFragment();
        fragment3 = new RepairFragment();
        fragment4 = new MineFragment();
        listFragnet.add(fragment1);
        listFragnet.add(fragment2);
        listFragnet.add(fragment3);
        listFragnet.add(fragment4);

        rg_bottom.setOnCheckedChangeListener(this);
        vp_show.setAdapter(new MainVpAdapter(getSupportFragmentManager()));
        vp_show.setCurrentItem(0);
        vp_show.setOffscreenPageLimit(3);
        vp_show.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (vp_show.getCurrentItem()) {
                case 0:
                    rb0.setChecked(true);
                    break;
                case 1:
                    rb1.setChecked(true);
                    break;
                case 2:
                    rb2.setChecked(true);
                    break;
                case 3:
                    rb3.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb0:
                vp_show.setCurrentItem(0);
                break;
            case R.id.rb1:
                vp_show.setCurrentItem(1);
                break;
            case R.id.rb2:
                vp_show.setCurrentItem(2);
                break;
            case R.id.rb3:
                vp_show.setCurrentItem(3);
                break;
        }
    }

    private void updateUmengToken() {
        String url = HttpsConts.BASE_URL + HttpsConts.UMENG_TOKEN;
        MyUtils.Loge(TAG, "url::" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "updateUmengToken()--response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(MainActivity.this, status, msg);
                    }
                } catch (Exception e) {
                    MyUtils.Loge(TAG, "updateUmengToken()--exception:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(MainActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", MyApplication.USER_ID);
                map.put("CID", MyApplication.UMENG_TOKEN);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

//    @Override
//    public void onBackPressed() {
//        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);// 数组向左移位操作
//        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
//        if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
//            MyApplication.AppExit();
//            finish();
//        } else {
//            showToast = true;
//        }
//        if (showToast) {
//            Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_LONG).show();
//            showToast = false;
//        }
//    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        initData();
    }

    //把返回键改成home键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
