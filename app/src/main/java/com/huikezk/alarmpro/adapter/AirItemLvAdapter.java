package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AirEntity;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.views.ListViewForScrollView;

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
public class AirItemLvAdapter extends BaseAdapter {
    private String TAG="RepairLvAdapter";
    private Context context;
    private List<String> listData = new ArrayList<>();

    public AirItemLvAdapter(Context context) {
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
            view = View.inflate(context, R.layout.item_part, null);
            viewHolder.item_part_name = view.findViewById(R.id.item_part_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MyUtils.Loge(TAG,"title:"+listData.get(i));
        viewHolder.item_part_name.setText(listData.get(i));

        return view;
    }

    class ViewHolder {
        TextView item_part_name;
    }
}
