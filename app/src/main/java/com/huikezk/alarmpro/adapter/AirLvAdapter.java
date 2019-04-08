package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AirEntity;
import com.huikezk.alarmpro.entity.RepairEntity;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;
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
public class AirLvAdapter extends BaseAdapter {
    private String TAG="RepairLvAdapter";
    private Context context;
    private List<AirEntity.DataBean> listData = new ArrayList<>();
    private OnAirClickListener onAirClickListener;

    public AirLvAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<AirEntity.DataBean> listData) {
        this.listData = listData;
    }

    public void setOnAirClickListener(OnAirClickListener onAirClickListener) {
        this.onAirClickListener = onAirClickListener;
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
            view = View.inflate(context, R.layout.item_air, null);
            viewHolder.item_air_lv = view.findViewById(R.id.item_air_lv);
            viewHolder.item_air_title = view.findViewById(R.id.item_air_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MyUtils.Loge(TAG,"NAME:"+listData.get(i).getName());
        viewHolder.item_air_title.setText("*"+listData.get(i).getName());
        AirItemLvAdapter airItemLvAdapter=new AirItemLvAdapter(context);
        airItemLvAdapter.setListData(listData.get(i).getInfo());
        viewHolder.item_air_lv.setAdapter(airItemLvAdapter);
        viewHolder.item_air_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onAirClickListener.onClick(i,position);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_air_title;
        ListViewForScrollView item_air_lv;
    }

    public interface OnAirClickListener{
        void onClick(int pos1,int pos2);
    }
}
