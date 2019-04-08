package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.FanEntity;
import com.huikezk.alarmpro.entity.TableEntity;
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
public class TableLvAdapter extends BaseAdapter {
    private final String sendName;
    private Context context;
    private List<TableEntity.DataBean> list = new ArrayList<>();
    private OnSwitchListener onSwitchListener;

    public TableLvAdapter(Context context, String sendName) {
        this.context = context;
        this.sendName = sendName;
    }

    public void setListData(List<TableEntity.DataBean> list) {
        this.list = list;
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_table, null);
            viewHolder.item_table_name = view.findViewById(R.id.item_table_name);
            viewHolder.item_table_id = view.findViewById(R.id.item_table_id);
            viewHolder.item_table_number = view.findViewById(R.id.item_table_number);
            viewHolder.item_table_open = view.findViewById(R.id.item_table_open);
            viewHolder.item_table_close = view.findViewById(R.id.item_table_close);
            viewHolder.item_table_refresh = view.findViewById(R.id.item_table_refresh);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(list.get(i).getDescribe())) {
            viewHolder.item_table_name.setText(list.get(i).getDescribe());
        }
        if (!TextUtils.isEmpty(list.get(i).getDeviceId())) {
            viewHolder.item_table_id.setText(list.get(i).getDeviceId());
        }
        String sendName1=sendName+list.get(i).getDeviceId()+"/度数";
        MyUtils.Loge("aaa","sendName1:"+sendName1);
        List<String> keyList = SaveUtils.getAllKeys();
        for (int j = 0; j < keyList.size(); j++) {
            if (keyList.get(j).equals(sendName1)) {
                if (!TextUtils.isEmpty(SaveUtils.getString(keyList.get(j))) &&
                        !TextUtils.isEmpty(SaveUtils.getString(keyList.get(j)))) {
                    viewHolder.item_table_number.setText(SaveUtils.getString(sendName1) + " Kw/h");
                }
            }

        }
        viewHolder.item_table_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onOpen(i);
            }
        });
        viewHolder.item_table_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onClose(i);
            }
        });
        viewHolder.item_table_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onRefresh(i);
            }
        });

        return view;
    }

    class ViewHolder {
        TextView item_table_name, item_table_id, item_table_number, item_table_open, item_table_close;
        ImageView item_table_refresh;
    }

    public interface OnSwitchListener {
        void onOpen(int i);

        void onClose(int i);

        void onRefresh(int i);
    }
}
