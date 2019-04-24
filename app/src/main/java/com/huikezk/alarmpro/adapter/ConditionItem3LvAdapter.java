package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
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
public class ConditionItem3LvAdapter extends BaseAdapter {
    private String TAG = "ConditionItemLvAdapter";
    private Context context;
    private String sendName;
    private List<String> listData = new ArrayList<>();
    private OnRateListener onRateListener;

    public ConditionItem3LvAdapter(Context context, String sendName) {
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
            view = View.inflate(context, R.layout.item_condition3_info, null);
            viewHolder.item_condition3_title = view.findViewById(R.id.item_condition3_title);
            viewHolder.item_condition3_content1 = view.findViewById(R.id.item_condition3_content1);
            viewHolder.item_condition3_content2 = view.findViewById(R.id.item_condition3_content2);
            viewHolder.item_condition3_content3 = view.findViewById(R.id.item_condition3_content3);
            viewHolder.item_condition3_content4 = view.findViewById(R.id.item_condition3_content4);
            viewHolder.item_condition3_ll = view.findViewById(R.id.item_condition3_ll);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.item_condition3_title.setText(listData.get(i));

        List<String> keyList = SaveUtils.getAllKeys();
        for (int j = 0; j < keyList.size(); j++) {
            if (keyList.get(j).equals(sendName + listData.get(i) + "/频率反馈")) {
                viewHolder.item_condition3_content1.setText(SaveUtils.getString(keyList.get(j)) + " HZ");
            }
            if (keyList.get(j).equals(sendName + listData.get(i) + "/输出电流")) {
                viewHolder.item_condition3_content2.setText(SaveUtils.getString(keyList.get(j)) + " A");
            }
            if (keyList.get(j).equals(sendName + listData.get(i) + "/输出功率")) {
                viewHolder.item_condition3_content3.setText(SaveUtils.getString(keyList.get(j)) + " KW/h");
            }
            if (keyList.get(j).equals(sendName + listData.get(i) + "/频率给定")) {
                viewHolder.item_condition3_content4.setText(SaveUtils.getString(keyList.get(j)) + " HZ");
            }
        }
        if (keyList.contains(sendName + listData.get(i) + "/频率反馈")) {
            viewHolder.item_condition3_content1.setText(SaveUtils.getString(sendName + listData.get(i) + "/频率反馈") + " HZ");
        } else {
            viewHolder.item_condition3_content1.setText("0 HZ");
        }
        if (keyList.contains(sendName + listData.get(i) + "/输出电流")) {
            viewHolder.item_condition3_content2.setText(SaveUtils.getString(sendName + listData.get(i) + "/输出电流") + " A");
        } else {
            viewHolder.item_condition3_content2.setText("0 A");
        }
        if (keyList.contains(sendName + listData.get(i) + "/输出功率")) {
            viewHolder.item_condition3_content3.setText(SaveUtils.getString(sendName + listData.get(i) + "/输出功率") + " KW/h");
        } else {
            viewHolder.item_condition3_content3.setText("0 KW/h");
        }
        if (keyList.contains(sendName + listData.get(i) + "/频率给定")) {
            viewHolder.item_condition3_content4.setText(SaveUtils.getString(sendName + listData.get(i) + "/频率给定") + " HZ");
        } else {
            viewHolder.item_condition3_content4.setText("0 HZ");
        }
        viewHolder.item_condition3_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRateListener.onRate(i);

            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_condition3_title, item_condition3_content1, item_condition3_content2, item_condition3_content3, item_condition3_content4;
        LinearLayout item_condition3_ll;
    }

    public interface OnRateListener {
        void onRate(int position);
    }
}
