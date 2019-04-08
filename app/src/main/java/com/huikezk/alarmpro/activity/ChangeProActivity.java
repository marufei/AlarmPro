package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.ChangeProLvAdapter;
import com.huikezk.alarmpro.entity.LoginEntity;
import com.huikezk.alarmpro.service.ListenerManager;
import com.huikezk.alarmpro.utils.MyUtils;

import java.util.List;

public class ChangeProActivity extends BaseActivity {

    private ListView change_pro_lv;
    private ChangeProLvAdapter adapter;
    private List<LoginEntity.DataBean.ProjectNameBean> proList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pro);
        setBack(true);
        ToolBarStyle(1);
        setTitle("切换门店");
        initView();
        initData();
        initListener();
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ChangeProActivity.class);
        context.startActivity(intent);
    }

    private void initListener() {
        change_pro_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.PROJECT_NAME = proList.get(position).getProjectName();
                MyApplication.PROJECT_SEND = "/" + proList.get(position).getProjectName() + "/";
                MyApplication.PROJECT_NUM = proList.get(position).getProjectNum();
                MyApplication.MOUDLE = proList.get(position).getModules();
                MyUtils.Loge(TAG, "项目名：" + MyApplication.PROJECT_NAME + "--发送指令头："
                        + MyApplication.PROJECT_SEND + "--项目号：" + MyApplication.PROJECT_NUM +
                        "--模块：" + MyApplication.MOUDLE);
                ListenerManager.getInstance().sendBroadCast("项目名改变");
                Intent intent = new Intent();
                intent.putExtra("name", proList.get(position).getProjectName());
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    private void initData() {
        proList = MyApplication.loginEntity.getData().getProjectName();
        adapter.setListData(proList);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        change_pro_lv = findViewById(R.id.change_pro_lv);
        adapter = new ChangeProLvAdapter(this);
        change_pro_lv.setAdapter(adapter);
    }
}
