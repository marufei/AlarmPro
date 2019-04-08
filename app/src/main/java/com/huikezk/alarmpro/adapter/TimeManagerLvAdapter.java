package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.TimeManagerEntity;
import com.huikezk.alarmpro.utils.Const;
import com.huikezk.alarmpro.utils.PicassoUtlis;
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
public class TimeManagerLvAdapter extends BaseAdapter {

    private List<TimeManagerEntity.DataBean> list = new ArrayList<>();
    private Context context;

    public TimeManagerLvAdapter(Context context) {
        this.context = context;
    }

    public OnTimeSelectListener onTimeSelectListener;

    public void setOnTimeSelectListener(OnTimeSelectListener onTimeSelectListener) {
        this.onTimeSelectListener = onTimeSelectListener;
    }

    public void setList(List<TimeManagerEntity.DataBean> list) {
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
            convertView = View.inflate(context, R.layout.item_time_manager, null);
            viewHolder.item_time_manager_title = convertView.findViewById(R.id.item_time_manager_title);
            viewHolder.item_time_manager_lv = convertView.findViewById(R.id.item_time_manager_lv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_time_manager_title.setText(list.get(position).getType());
        if (list.get(position) != null && list.get(position).getTimes() != null) {
            TimeMangerItemLvAdapter adapter = new TimeMangerItemLvAdapter(context);
            adapter.setListData(list.get(position).getTimes());
            viewHolder.item_time_manager_lv.setAdapter(adapter);
        }
        viewHolder.item_time_manager_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                onTimeSelectListener.onSelected(position, i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView item_time_manager_title;
        ListViewForScrollView item_time_manager_lv;
    }

    public interface OnTimeSelectListener {
        void onSelected(int pos1, int pos2);
    }
}
