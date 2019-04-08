package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.util.List;

public class AirInfoActivity extends BaseActivity {

    private TextView air_info_arg1, air_info_arg2, air_info_arg3, air_info_arg4;
    private String sendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_info);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        air_info_arg1 = findViewById(R.id.air_info_arg1);
        air_info_arg2 = findViewById(R.id.air_info_arg2);
        air_info_arg3 = findViewById(R.id.air_info_arg3);
        air_info_arg4 = findViewById(R.id.air_info_arg4);
    }

    private void initData() {
        String title = getIntent().getStringExtra("title");
        String part1 = getIntent().getStringExtra("part1");
        String part2 = getIntent().getStringExtra("part2");
        setTitle(part1 + " （" + part2 + "）");
        sendName = MyApplication.PROJECT_SEND + title + "/" + part1 + "/" + part2 + "/";
        setView();
    }

    private void setView() {
        List<String> keyList = SaveUtils.getAllKeys();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).equals(sendName + "温度读值")) {
                air_info_arg1.setText(SaveUtils.getString(keyList.get(i)) + " ℃");
            }
            if (keyList.get(i).equals(sendName + "湿度读值")) {
                air_info_arg2.setText(SaveUtils.getString(keyList.get(i)) + " %");
            }
            if (keyList.get(i).equals(sendName + "CO2读值")) {
                air_info_arg3.setText(SaveUtils.getString(keyList.get(i)) + " ppm");
            }
            if (keyList.get(i).equals(sendName + "TVOC读值")) {
                air_info_arg4.setText(SaveUtils.getString(keyList.get(i)) + " ppm");
            }
        }
    }

    public static void start(Context context, String title, String part1, String part2) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("part1", part1);
        intent.putExtra("part2", part2);
        intent.setClass(context, AirInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        setView();
    }
}
