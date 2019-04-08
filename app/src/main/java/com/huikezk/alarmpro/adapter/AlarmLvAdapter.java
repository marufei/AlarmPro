package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AlarmEntity;
import com.huikezk.alarmpro.entity.RepairRecordEntity;
import com.huikezk.alarmpro.utils.SaveUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by MaRufei
 * on 2018/5/29.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AlarmLvAdapter extends BaseAdapter {
    private Context context;
    private List<String> list=new ArrayList<>();

    public AlarmLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<String> list) {
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_alarm, null);
            viewHolder.item_alarm_name = view.findViewById(R.id.item_alarm_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            String jsonStr = SaveUtils.getString(list.get(i));
            Gson gson = new Gson();
            AlarmEntity alarmEntity = gson.fromJson(jsonStr, AlarmEntity.class);
            viewHolder.item_alarm_name.setText(alarmEntity.getInfo());
        }catch (Exception e){

        }

        return view;
    }

    class ViewHolder {
        TextView item_alarm_name;
    }
}
