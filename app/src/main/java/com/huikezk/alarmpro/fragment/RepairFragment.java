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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.activity.NewRepairActivity;
import com.huikezk.alarmpro.activity.RepairFinishActivity;
import com.huikezk.alarmpro.adapter.RepairLvAdapter;
import com.huikezk.alarmpro.entity.RepairEntity;
import com.huikezk.alarmpro.receiver.MyReceiver;
import com.huikezk.alarmpro.utils.JsonUtil;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MaRufei
 * on 2019/1/3.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class RepairFragment extends BaseFragment implements View.OnClickListener,MyReceiver.OnMyReceiverListener {
    private View view;
    private TextView repair_create;
    private ListView fragment_repair_lv;
    private RepairLvAdapter adapter;
    private List<String> list=new ArrayList<>();
    private List<RepairEntity> listData;
    private TextView fragment_repair_null;
    private MyReceiver myReceiver;

    @Override
    protected void lazyLoad() {
        if (fragment_repair_null!=null) {
            initData();
            initReceiver();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = View.inflate(getActivity(), R.layout.fragment_repair, null);
        initViews();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        fragment_repair_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepairFinishActivity.start(getActivity(), listData.get(position));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        list.clear();
        list.addAll(SaveUtils.getAllEndWithKey("repair"));
        if (list != null && list.size() > 0) {
            List<String> proList = new ArrayList<>();
            for (String str : list) {
                if (str.contains(SaveUtils.getString(KeyUtils.PROJECT_NAME)) && !TextUtils.isEmpty(SaveUtils.getString(str))) {
//                    proList.add(str);
                    proList.add(SaveUtils.getString(str));
                }
            }
            if (proList.size() > 0) {
                fragment_repair_lv.setVisibility(View.VISIBLE);
                fragment_repair_null.setVisibility(View.GONE);
                listData = JsonUtil.repair2Json(proList);
                Collections.sort(listData);
                adapter.setListData(listData);
                adapter.notifyDataSetChanged();
            } else {
                fragment_repair_lv.setVisibility(View.GONE);
                fragment_repair_null.setVisibility(View.VISIBLE);
            }
        } else {
            fragment_repair_lv.setVisibility(View.GONE);
            fragment_repair_null.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        fragment_repair_null = view.findViewById(R.id.fragment_repair_null);
        repair_create = view.findViewById(R.id.repair_create);
        repair_create.setOnClickListener(this);
        fragment_repair_lv = view.findViewById(R.id.fragment_repair_lv);
        adapter = new RepairLvAdapter(getActivity());
        fragment_repair_lv.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_create:
                NewRepairActivity.start(getActivity());
                break;
        }
    }

    private void initReceiver() {
        myReceiver = new MyReceiver();
        myReceiver.setOnMyReceive(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("myReceiver");
        getActivity().registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onMyReceiver(Context context, Intent intent) {
        MyUtils.Loge(TAG, "HomeFragment--收到广播");
        list.clear();
        list.addAll(SaveUtils.getAllEndWithKey("repair"));
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
        }

    }
}
