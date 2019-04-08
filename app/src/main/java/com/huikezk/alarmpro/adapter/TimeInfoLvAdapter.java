package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.TimeInfoEntity;
import com.huikezk.alarmpro.entity.TimeManagerEntity;
import com.huikezk.alarmpro.views.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/7.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class TimeInfoLvAdapter extends BaseAdapter {

    private List<TimeInfoEntity.DataBean> list = new ArrayList<>();
    private Context context;

    public TimeInfoLvAdapter(Context context) {
        this.context = context;
    }

    public OnTimeSelectListener onTimeSelectListener;

    public void setOnTimeSelectListener(OnTimeSelectListener onTimeSelectListener) {
        this.onTimeSelectListener = onTimeSelectListener;
    }

    public void setList(List<TimeInfoEntity.DataBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_time_info, null);
            viewHolder.item_time_info_date = convertView.findViewById(R.id.item_time_info_date);
            viewHolder.item_time_info_open = convertView.findViewById(R.id.item_time_info_open);
            viewHolder.item_time_info_close = convertView.findViewById(R.id.item_time_info_close);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (list.get(position).getWeek()) {
            case 1:
                viewHolder.item_time_info_date.setText("周一");
                break;
            case 2:
                viewHolder.item_time_info_date.setText("周二");
                break;
            case 3:
                viewHolder.item_time_info_date.setText("周三");
                break;
            case 4:
                viewHolder.item_time_info_date.setText("周四");
                break;
            case 5:
                viewHolder.item_time_info_date.setText("周五");
                break;
            case 6:
                viewHolder.item_time_info_date.setText("周六");
                break;
            case 7:
                viewHolder.item_time_info_date.setText("周日");
                break;
            default:
                viewHolder.item_time_info_date.setText("星期");
                break;
        }

        if (!TextUtils.isEmpty(list.get(position).getOpenTime())) {
            viewHolder.item_time_info_open.setText(list.get(position).getOpenTime());
        }
        if (!TextUtils.isEmpty(list.get(position).getCloseTime())) {
            viewHolder.item_time_info_close.setText(list.get(position).getCloseTime());
        }

        viewHolder.item_time_info_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSelectListener.onOpenClick(position);
            }
        });

        viewHolder.item_time_info_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSelectListener.onCloseClick(position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView item_time_info_date, item_time_info_open, item_time_info_close;

    }

    public interface OnTimeSelectListener {
        void onOpenClick(int position);

        void onCloseClick(int position);
    }
}
