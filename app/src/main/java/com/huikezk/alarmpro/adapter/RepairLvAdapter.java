package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.RepairEntity;
import com.huikezk.alarmpro.entity.RepairRecordEntity;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MaRufei
 * on 2018/5/29.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class RepairLvAdapter extends BaseAdapter {
    private String TAG="RepairLvAdapter";
    private Context context;
    private List<RepairEntity> listData = new ArrayList<>();

    public RepairLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<RepairEntity> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_repair_history, null);
            viewHolder.item_repair_history_title = view.findViewById(R.id.item_repair_history_title);
            viewHolder.item_repair_history_time = view.findViewById(R.id.item_repair_history_time);
            viewHolder.item_repair_history_name = view.findViewById(R.id.item_repair_history_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (!TextUtils.isEmpty(listData.get(i).getRepairInfo())) {
            viewHolder.item_repair_history_title.setText(listData.get(i).getRepairInfo());
        }
        if (!TextUtils.isEmpty(listData.get(i).getNickName())) {
            viewHolder.item_repair_history_name.setText("报  修  人:"+listData.get(i).getNickName());
        }
        if (!TextUtils.isEmpty(listData.get(i).getDatetime())) {
            viewHolder.item_repair_history_time.setText("报修时间:"+listData.get(i).getDatetime());
        }

        return view;
    }

    class ViewHolder {
        TextView item_repair_history_title, item_repair_history_time, item_repair_history_name;
    }
}
