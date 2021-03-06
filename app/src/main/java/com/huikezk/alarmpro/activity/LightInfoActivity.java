package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.LightInfoLvAdapter;
import com.huikezk.alarmpro.adapter.LightLvAdapter;
import com.huikezk.alarmpro.entity.LightEntity;
import com.huikezk.alarmpro.receiver.MyReceiver;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LightInfoActivity extends BaseActivity implements MyReceiver.OnMyReceiverListener {

    private ListView light_info_lv;
    private String title;
    private String name;
    private List<LightEntity.DataBean.InfoBean> listData=new ArrayList<>();
    private LightInfoLvAdapter adapter;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_info);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
        initReceiver();
    }

    public static void start(Context context, String title, LightEntity.DataBean dataBean) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("dataBean", (Serializable) dataBean);
        intent.setClass(context, LightInfoActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
        title=getIntent().getStringExtra("title");
        LightEntity.DataBean dataBean = (LightEntity.DataBean) getIntent().getSerializableExtra("dataBean");
        if (dataBean!=null){
            name=dataBean.getName();
            String sendName= SaveUtils.getString(KeyUtils.PROJECT_SEND)+title+"/"+name+"/";
            setTitle(name);
            listData.clear();
            listData.addAll(dataBean.getInfo());
            adapter=new LightInfoLvAdapter(LightInfoActivity.this,sendName);
            adapter.setListData(listData);
            light_info_lv.setAdapter(adapter);
        }
    }

    private void initView() {
        light_info_lv=findViewById(R.id.light_info_lv);

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
        MyUtils.Loge(TAG,"LightInfoActivity--收到广播");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver!=null) {
            unregisterReceiver(myReceiver);
        }

    }
}
