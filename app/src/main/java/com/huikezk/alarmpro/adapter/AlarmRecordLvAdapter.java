package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AlarmRecordEntity;
import com.huikezk.alarmpro.utils.MyUtils;

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
public class AlarmRecordLvAdapter extends BaseAdapter {
    private String TAG="RepairLvAdapter";
    private Context context;
    private List<AlarmRecordEntity.DataBean> listData = new ArrayList<>();

    public AlarmRecordLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<AlarmRecordEntity.DataBean> listData) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_alarm, null);
            viewHolder.item_alarm_time = view.findViewById(R.id.item_alarm_time);
            viewHolder.item_alarm_name = view.findViewById(R.id.item_alarm_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.item_alarm_name.setText(listData.get(i).getInfo());
        viewHolder.item_alarm_time.setVisibility(View.VISIBLE);
        viewHolder.item_alarm_time.setText(listData.get(i).getDatetime());

        return view;
    }

    class ViewHolder {
        TextView item_alarm_name,item_alarm_time;
    }
}
