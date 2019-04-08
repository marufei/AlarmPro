package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.RepairRecordEntity;
import com.huikezk.alarmpro.entity.TimeManagerEntity;

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
public class TimeMangerItemLvAdapter extends BaseAdapter {
    private Context context;
    private List<TimeManagerEntity.DataBean.TimesBean> listData=new ArrayList<>();
    public TimeMangerItemLvAdapter(Context context){
        this.context=context;
    }

    public void setListData(List<TimeManagerEntity.DataBean.TimesBean> listData) {
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
        ViewHolder viewHolder;
        if(view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(context, R.layout.item_time_manager_lv,null);
            viewHolder.item_time_manager_lv_title=view.findViewById(R.id.item_time_manager_lv_title);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.item_time_manager_lv_title.setText(listData.get(i).getName());

        return view;
    }

    class ViewHolder{
        TextView item_time_manager_lv_title;
    }
}
