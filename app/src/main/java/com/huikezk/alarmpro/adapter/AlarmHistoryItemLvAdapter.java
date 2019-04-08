package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AlarmHistoryEntity;

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
public class AlarmHistoryItemLvAdapter extends BaseAdapter {
    private Context context;
    private List<String> listData = new ArrayList<>();

    public AlarmHistoryItemLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<String> listData) {
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
            view = View.inflate(context, R.layout.item_alarm_lv_history, null);
            viewHolder.item_alarm_lv_title = view.findViewById(R.id.item_alarm_lv_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (listData.get(i) != null) {
            if (!TextUtils.isEmpty(listData.get(i))) {
                viewHolder.item_alarm_lv_title.setText(listData.get(i));
            }
        }
        return view;
    }

    class ViewHolder {
        TextView item_alarm_lv_title;

    }
}
