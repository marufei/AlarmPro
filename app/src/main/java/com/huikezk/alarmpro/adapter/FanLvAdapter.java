package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.FanEntity;

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
public class FanLvAdapter extends BaseAdapter {
    private Context context;
    private List<FanEntity.DataBean> list=new ArrayList<>();

    public FanLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<FanEntity.DataBean> list) {
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
            view = View.inflate(context, R.layout.item_part, null);
            viewHolder.item_part_name = view.findViewById(R.id.item_part_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.item_part_name.setText(list.get(i).getName());
        return view;
    }

    class ViewHolder {
        TextView item_part_name;
    }
}
