package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.ChangeProLvAdapter;
import com.huikezk.alarmpro.entity.LoginEntity;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

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
                MyApplication.MOUDLE = proList.get(position).getModules();
                SaveUtils.setString(KeyUtils.PROJECT_NUM,String.valueOf(proList.get(position).getProjectNum()));
                SaveUtils.setString(KeyUtils.PROJECT_NAME,proList.get(position).getProjectName());
                SaveUtils.setString(KeyUtils.PROJECT_SEND,"/" + proList.get(position).getProjectName() + "/");
                Intent intent1=new Intent();
                intent1.setAction("myReceiver");
                sendBroadcast(intent1);

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
