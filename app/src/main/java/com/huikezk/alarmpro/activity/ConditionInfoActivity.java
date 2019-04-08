package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.ConditionItem2LvAdapter;
import com.huikezk.alarmpro.adapter.ConditionItem3LvAdapter;
import com.huikezk.alarmpro.adapter.ConditionItem4LvAdapter;
import com.huikezk.alarmpro.adapter.ConditionItem5LvAdapter;
import com.huikezk.alarmpro.adapter.ConditionItemLvAdapter;
import com.huikezk.alarmpro.entity.ConditionEntity;
import com.huikezk.alarmpro.service.IListener;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.views.DialogInputView;
import com.huikezk.alarmpro.views.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class ConditionInfoActivity extends BaseActivity{

    private ListViewForScrollView condition_info_lv1, condition_info_lv2, condition_info_lv3, condition_info_lv4, condition_info_lv5;
    private ConditionEntity.DataBean.InfoBean infoBean;
    private ConditionItemLvAdapter lv1_adapter;
    private List<String> lv1_list=new ArrayList<>();
    private List<String> lv2_list=new ArrayList<>();
    private List<String> lv3_list=new ArrayList<>();
    private List<String> lv4_list=new ArrayList<>();
    private List<String> lv5_list=new ArrayList<>();
    private String sendName;
    private ConditionItem2LvAdapter lv2_adapter;
    private ConditionItem3LvAdapter lv3_adapter;
    private ConditionItem4LvAdapter lv4_adapter;
    private ConditionItem5LvAdapter lv5_adapter;
    private DialogInputView dialogInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_info);
        setBack(true);
        ToolBarStyle(1);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lv1_adapter.setOnSwitchListener(new ConditionItemLvAdapter.OnSwitchListener() {
            @Override
            public void onOpen(final int position) {
                showAlertDialog("温馨提示", "确定开启？", "确定",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        publish(sendName+lv1_list.get(position)+"/order","开");
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
                                publish(sendName+lv1_list.get(position)+"/order","关");

                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
        lv3_adapter.setOnRateListener(new ConditionItem3LvAdapter.OnRateListener() {
            @Override
            public void onRate(final int position) {
                dialogInputView.setOnEventClickListenner(new DialogInputView.OnEventClickListenner() {
                    @Override
                    public void onSure(String string) {
//                        MyUtils.Loge(TAG,"topic:"+sendName+lv3_list.get(position)+"/频率给定"+"--message:"+string);
                        publish(sendName+lv3_list.get(position)+"/频率给定/order",string);
                    }
                });
                dialogInputView.showDialog();
            }
        });

        lv5_adapter.setOnRateListener(new ConditionItem5LvAdapter.OnRateListener() {
            @Override
            public void onRate(final int position) {
                dialogInputView.setOnEventClickListenner(new DialogInputView.OnEventClickListenner() {
                    @Override
                    public void onSure(String string) {
//                        MyUtils.Loge(TAG,"topic:"+sendName+lv3_list.get(position)+"/频率给定"+"--message:"+string);
                        publish(sendName+lv5_list.get(position)+"阀门设置/order",string);
                    }
                });
                dialogInputView.showDialog();
            }
        });
    }

    private void initData() {
        String title=getIntent().getStringExtra("title");
        String name = getIntent().getStringExtra("name");
        sendName= MyApplication.PROJECT_SEND+title+"/"+name+"/";
        setTitle(name);
        lv1_adapter=new ConditionItemLvAdapter(this,sendName);
        condition_info_lv1.setAdapter(lv1_adapter);

        lv2_adapter=new ConditionItem2LvAdapter(this,sendName);
        condition_info_lv2.setAdapter(lv2_adapter);

        lv3_adapter=new ConditionItem3LvAdapter(this,sendName);
        condition_info_lv3.setAdapter(lv3_adapter);

        lv4_adapter=new ConditionItem4LvAdapter(this,sendName);
        condition_info_lv4.setAdapter(lv4_adapter);

        lv5_adapter=new ConditionItem5LvAdapter(this,sendName);
        condition_info_lv5.setAdapter(lv5_adapter);

        infoBean=(ConditionEntity.DataBean.InfoBean)getIntent().getSerializableExtra("infoBean");
        if (infoBean!=null) {
            setAllData(infoBean);
        }

    }

    private void setAllData(ConditionEntity.DataBean.InfoBean infoBean) {
        lv1_list.addAll(infoBean.getAUH());
        lv1_adapter.setListData(lv1_list);
        lv1_adapter.notifyDataSetChanged();

        lv2_list.addAll(infoBean.getAlarm());
        lv2_adapter.setListData(lv2_list);
        lv2_adapter.notifyDataSetChanged();

        lv3_list.addAll(infoBean.getFrequency());
        lv3_adapter.setListData(lv3_list);
        lv3_adapter.notifyDataSetChanged();

        lv4_list.addAll(infoBean.getHumiture());
        lv4_adapter.setListData(lv4_list);
        lv4_adapter.notifyDataSetChanged();

        lv5_list.addAll(infoBean.getValve());
        lv5_adapter.setListData(lv5_list);
        lv5_adapter.notifyDataSetChanged();

    }

    private void initView() {
        condition_info_lv1 = findViewById(R.id.condition_info_lv1);

        condition_info_lv2 = findViewById(R.id.condition_info_lv2);

        condition_info_lv3 = findViewById(R.id.condition_info_lv3);

        condition_info_lv4 = findViewById(R.id.condition_info_lv4);

        condition_info_lv5 = findViewById(R.id.condition_info_lv5);

        dialogInputView=new DialogInputView(ConditionInfoActivity.this,R.layout.dialog_input);


    }

    public static void start(Context context,String title, String name,ConditionEntity.DataBean.InfoBean infoBean) {
        Intent intent = new Intent();
        intent.putExtra("title",title);
        intent.putExtra("name",name);
        intent.putExtra("infoBean", infoBean);
        intent.setClass(context, ConditionInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void notifyAllActivity(String str) {
        super.notifyAllActivity(str);
        lv1_adapter.notifyDataSetChanged();
        lv2_adapter.notifyDataSetChanged();
        lv3_adapter.notifyDataSetChanged();
        lv4_adapter.notifyDataSetChanged();
        lv5_adapter.notifyDataSetChanged();
    }
}
