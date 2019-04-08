package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.util.List;

public class PartInfoActivity extends BaseActivity {

    private String title;
    private TextView part_info_name1, part_info_name2, part_info_name3, part_info_name4, part_info_name5,
            part_info_name6, part_info_name7, part_info_name8, part_info_name9, part_info_name10, part_info_name11,
            part_info_name12, part_info_name13, part_info_name14;
    private String part;
    private String sendOrder;
    private static final String OUT_LINE = "离线";
    private static final String ALARM = "报警";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_info);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        part = getIntent().getStringExtra("part");
        sendOrder = MyApplication.PROJECT_SEND + part + "/" + title + "/";
        setTitle(title);
        setView();
    }

    private void setView() {
        List<String> keyList = SaveUtils.getAllKeys();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).equals(sendOrder + "液位控制开关状态监测-低液位")) {
                setViewData(part_info_name1, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "液位控制开关状态监测-高液位")) {
                setViewData(part_info_name2, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "液位控制开关状态监测-超高液位")) {
                setViewData(part_info_name3, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "污泥泵浮球状态监测-低液位")) {
                setViewData(part_info_name4, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "污泥泵浮球状态监测-高液位")) {
                setViewData(part_info_name5, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-地面溢水监测")) {
                setViewData(part_info_name6, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-设备手自动状态")) {
                setViewData(part_info_name7, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-刮板运行状态监测")) {
                setViewData(part_info_name8, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-提升泵1运行状态监测")) {
                setViewData(part_info_name9, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-提升泵2运行状态监测")) {
                setViewData(part_info_name10, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-除渣状态")) {
                setViewData(part_info_name11, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-清洁状态")) {
                setViewData(part_info_name12, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-气浮状态")) {
                setViewData(part_info_name13, keyList.get(i));
            }
            if (keyList.get(i).equals(sendOrder + "运行状态监测-加热状态")) {
                setViewData(part_info_name14, keyList.get(i));
            }
        }
    }

    private void initView() {
        part_info_name1 = findViewById(R.id.part_info_name1);
        part_info_name2 = findViewById(R.id.part_info_name2);
        part_info_name3 = findViewById(R.id.part_info_name3);
        part_info_name4 = findViewById(R.id.part_info_name4);
        part_info_name5 = findViewById(R.id.part_info_name5);
        part_info_name6 = findViewById(R.id.part_info_name6);
        part_info_name7 = findViewById(R.id.part_info_name7);
        part_info_name8 = findViewById(R.id.part_info_name8);
        part_info_name9 = findViewById(R.id.part_info_name9);
        part_info_name10 = findViewById(R.id.part_info_name10);
        part_info_name11 = findViewById(R.id.part_info_name11);
        part_info_name12 = findViewById(R.id.part_info_name12);
        part_info_name13 = findViewById(R.id.part_info_name13);
        part_info_name14 = findViewById(R.id.part_info_name14);

    }

    public static void start(Context context, String part, String title) {
        Intent intent = new Intent();
        intent.putExtra("part", part);
        intent.putExtra("title", title);
        intent.setClass(context, PartInfoActivity.class);
        context.startActivity(intent);
    }

    public void setViewData(TextView view, String content) {
        if (!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(SaveUtils.getString(content))) {
            if (SaveUtils.getString(content).equals(OUT_LINE)) {
                view.setTextColor(getResources().getColor(R.color.org_f7));
            } else if (SaveUtils.getString(content).equals(ALARM)) {
                view.setTextColor(getResources().getColor(R.color.red_f6));
            } else {
                view.setTextColor(getResources().getColor(R.color.red_f6));
            }
            view.setText(SaveUtils.getString(content));

        }

    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        setView();
    }
}
