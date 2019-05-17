package com.huikezk.alarmpro.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.MainVpAdapter;
import com.huikezk.alarmpro.entity.LoginEntity;
import com.huikezk.alarmpro.entity.UpdateEntity;
import com.huikezk.alarmpro.fragment.HomeFragment;
import com.huikezk.alarmpro.fragment.MineFragment;
import com.huikezk.alarmpro.fragment.NewsFragment;
import com.huikezk.alarmpro.fragment.RepairFragment;
import com.huikezk.alarmpro.receiver.MyReceiver;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.UpdateManger;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.huikezk.alarmpro.views.MyViewPager;

import net.igenius.mqttservice.MQTTServiceCommand;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends CheckPermissionsActivity implements MyApplication.PushSuccessListener,
        RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, MyReceiver.OnMyReceiverListener {

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
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};
    /**
     * 进入第几个界面
     */
    private int currentPage;
    /**
     * 推送过来的项目名
     */
    private String projectName;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().setPushSuccessListener(this);
        initView();
        initEvent();
        initReceiver();
        initData();
        String token = PushServiceFactory.getCloudPushService().getDeviceId();
        updateUmengToken(token);
        initMQTT();
        initSMSBind();
        setPermission(permissions);
        getUpdateInfo();
    }

    private void initSMSBind() {
        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.TEL))) {
            PushServiceFactory.getCloudPushService().bindPhoneNumber(SaveUtils.getString(KeyUtils.TEL), new CommonCallback() {
                @Override
                public void onSuccess(String s) {

                    MyUtils.Loge(TAG, "手机号：" + s + "绑定成功");
                }

                @Override
                public void onFailed(String s, String s1) {
                    MyUtils.Loge(TAG, "手机号绑定失败");
                }
            });
        }
    }

    private void initData() {
        MyApplication.getInstance().setPushSuccessListener(this);
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
                if (str.contains(SaveUtils.getString(KeyUtils.PROJECT_NAME))) {
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
        currentPage = getIntent().getIntExtra("currentPage", 0);
        projectName = getIntent().getStringExtra("projectName");
        if (!TextUtils.isEmpty(projectName)) {
            resetProject(projectName);
        }
        switch (currentPage) {
            case 0:
                //设置主页第一个分页被选中
                rb0.setSelected(true);
                rb0.setChecked(true);
                break;
            case 1:
                //设置主页第一个分页被选中
                rb1.setSelected(true);
                rb1.setChecked(true);
                break;
            case 2:
                //设置主页第一个分页被选中
                rb2.setSelected(true);
                rb2.setChecked(true);
                break;
            case 3:
                //设置主页第一个分页被选中
                rb3.setSelected(true);
                rb3.setChecked(true);
                break;
        }


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
        vp_show.setCurrentItem(currentPage);
        vp_show.setOffscreenPageLimit(3);
        vp_show.addOnPageChangeListener(this);
    }

    /**
     * 根据推送重置当前门店
     *
     * @param projectName
     */
    private void resetProject(String projectName) {
        if (MyApplication.loginEntity!=null) {
            List<LoginEntity.DataBean.ProjectNameBean> proList = MyApplication.loginEntity.getData().getProjectName();
            SaveUtils.setString(KeyUtils.PROJECT_NAME, projectName);
            for (int i = 0; i < proList.size(); i++) {
                if (proList.get(i).getProjectName().equals(projectName)) {
                    SaveUtils.setString(KeyUtils.PROJECT_NUM, String.valueOf(proList.get(i).getProjectNum()));
                    MyApplication.MOUDLE = proList.get(i).getModules();
                }
            }
            SaveUtils.setString(KeyUtils.PROJECT_SEND, "/" + projectName + "/");
            Intent intent = new Intent();
            intent.setAction("myReceiver");
            sendBroadcast(intent);
        }else {
            MQTTServiceCommand.disconnect(getApplicationContext());
            MyApplication.finishAllActivity();
            ActivityUtil.exitAll();
            SplashActivity.start(MainActivity.this);
        }

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

    private void updateUmengToken(final String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        String url = HttpsConts.BASE_URL + HttpsConts.UMENG_TOKEN;
        MyUtils.Loge(TAG, "url::" + url);
        MyUtils.Loge(TAG, "USER_ID:" + SaveUtils.getString(KeyUtils.USER_ID));
        MyUtils.Loge(TAG, "token:" + token);
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
                MyUtils.Loge(TAG, "updateUmengToken--------网络有问题");
                MyUtils.showToast(MainActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", SaveUtils.getString(KeyUtils.USER_ID));
                map.put("CID", token);
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

    public void getUpdateInfo() {
        String url = HttpsConts.BASE_URL + HttpsConts.UPDATE;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "getUpdateInfo（）-- response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        UpdateEntity updateEntity = gson.fromJson(response, UpdateEntity.class);
                        if (updateEntity != null && updateEntity.getData() != null) {
                            MyApplication.update_url = updateEntity.getData().getAndroid_url();
                            MyApplication.update_content = updateEntity.getData().getDescription();
                            if (Double.valueOf(updateEntity.getData().getVersion_code()) > MyUtils.getVersionCode(MainActivity.this)
                                    && !TextUtils.isEmpty(updateEntity.getData().getAndroid_url())) {
                                // TODO 下载
                                downloadApk(updateEntity.getData().getVersion_code());
                            }
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(MainActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.Loge(TAG, "getUpdateInfo()--网络有问题");
                MyUtils.showToast(MainActivity.this, "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    private void downloadApk(int version) {
        new UpdateManger(MainActivity.this, 1).checkUpdateInfo();
    }

    @Override
    public void onSuccess(String string) {
        updateUmengToken(string);
    }

    private void initMQTT() {
        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.MQTT_URL))) {
            MyUtils.Loge(TAG, "clientId：" + Build.SERIAL);
            MQTTServiceCommand.connect(getApplicationContext(),
                    "tcp://" + SaveUtils.getString(KeyUtils.MQTT_URL),
                    Build.SERIAL,
                    "admin",
                    "123456");
        }
    }

    private void initReceiver() {
        myReceiver = new MyReceiver();
        myReceiver.setOnMyReceive(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("myReceiver");
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onMyReceiver(Context context, Intent intent) {
        MyUtils.Loge(TAG, "MAinActivity--收到广播");
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

    }

}
