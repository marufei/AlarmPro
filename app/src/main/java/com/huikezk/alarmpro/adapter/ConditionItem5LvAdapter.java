package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
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
public class ConditionItem5LvAdapter extends BaseAdapter {
    private String TAG = "ConditionItemLvAdapter";
    private Context context;
    private String sendName;
    private List<String> listData = new ArrayList<>();
    private OnRateListener onRateListener;

    public ConditionItem5LvAdapter(Context context, String sendName) {
        this.context = context;
        this.sendName = sendName;
    }

    public void setOnRateListener(OnRateListener onRateListener) {
        this.onRateListener = onRateListener;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_condition5_info, null);
            viewHolder.item_condition5_title = view.findViewById(R.id.item_condition5_title);
            viewHolder.item_condition5_content1 = view.findViewById(R.id.item_condition5_content1);
            viewHolder.item_condition5_content2 = view.findViewById(R.id.item_condition5_content2);
            viewHolder.item_condition5_ll = view.findViewById(R.id.item_condition5_ll);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.item_condition5_title.setText(listData.get(i));
        List<String> keyList = SaveUtils.getAllKeys();
        for (int j = 0; j < keyList.size(); j++) {
            if (keyList.get(j).equals(sendName+ listData.get(i) + "开度")) {
                viewHolder.item_condition5_content1.setText(SaveUtils.getString(keyList.get(j)) + " %");
            }
            if (keyList.get(j).equals(sendName+ listData.get(i) + "设置")) {
                viewHolder.item_condition5_content2.setText(SaveUtils.getString(keyList.get(j)) + " %");
            }
        }
        viewHolder.item_condition5_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRateListener.onRate(i);

            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_condition5_title, item_condition5_content1, item_condition5_content2;
        LinearLayout item_condition5_ll;
    }

    public interface OnRateListener{
        void onRate(int position);
    }
}
