package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.FanInfoLvAdapter;
import com.huikezk.alarmpro.entity.FanEntity;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FanInfoActivity extends BaseActivity {

    private FanEntity.DataBean bean;
    private ListView fan_info_lv;
    private FanInfoLvAdapter adapter;
    private String sendName;
    private List<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_info);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
        initListener();


    }

    private void initListener() {
        adapter.setOnSwitchListener(new FanInfoLvAdapter.OnSwitchListener() {
            @Override
            public void onOpen(final int position) {
                showAlertDialog("温馨提示", "确定开启？", "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.Loge(TAG,"送风机："+sendName+list.get(position)+"/order");
                                publish(sendName+list.get(position)+"/order","开");
                                dialog.dismiss();

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
                showAlertDialog("温馨提示", "确定关闭？", "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                publish(sendName+list.get(position)+"/order","关");

                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
    }

    public static void start(Context context, String title, FanEntity.DataBean bean) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("bean",  bean);
        intent.setClass(context, FanInfoActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
        String title=getIntent().getStringExtra("title");
        bean= (FanEntity.DataBean) getIntent().getSerializableExtra("bean");
        if (bean!=null&&!TextUtils.isEmpty(bean.getName())&&bean.getInfo()!=null){
            list.clear();
            sendName= SaveUtils.getString(KeyUtils.PROJECT_SEND)+title+"/"+bean.getName()+"/";
            setTitle(bean.getName());
            list.addAll(bean.getInfo());
            setViews( sendName,list);
        }
    }

    private void setViews(String sendName,List<String> list) {

        adapter=new FanInfoLvAdapter(this,sendName);
        adapter.setListData(list);
        fan_info_lv.setAdapter(adapter);
    }

    private void initView() {
        fan_info_lv=findViewById(R.id.fan_info_lv);

    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        adapter.notifyDataSetChanged();
    }
}
