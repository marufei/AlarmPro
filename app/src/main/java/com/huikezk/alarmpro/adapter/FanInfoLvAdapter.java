package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.Const;
import com.huikezk.alarmpro.utils.PicassoUtlis;
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
public class FanInfoLvAdapter extends BaseAdapter {
    private String TAG = "ConditionItemLvAdapter";
    private Context context;

    private List<String> listData = new ArrayList<>();
    private OnSwitchListener onSwitchListener;
    private String sendName;

    public FanInfoLvAdapter(Context context, String sendName) {
        this.context = context;
        this.sendName = sendName;
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
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
            view = View.inflate(context, R.layout.item_fan_info, null);
            viewHolder.item_fan_info_name = view.findViewById(R.id.item_fan_info_name);
            viewHolder.item_fan_info_open = view.findViewById(R.id.item_fan_info_open);
            viewHolder.item_fan_info_close = view.findViewById(R.id.item_fan_info_close);
            viewHolder.item_fan_info_error = view.findViewById(R.id.item_fan_info_error);
            viewHolder.item_fan_info_status = view.findViewById(R.id.item_fan_info_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String name_status = sendName + listData.get(i) + "/状态";
        String name_alarm = sendName + listData.get(i) + "/故障";
        viewHolder.item_fan_info_name.setText(listData.get(i));
        List<String> keyList = SaveUtils.getAllKeys();
//        for (int j = 0; j < keyList.size(); j++) {
//            if (keyList.get(j).equals(sendName + listData.get(i) + "/状态")) {
//                if (!TextUtils.isEmpty(SaveUtils.getString(keyList.get(j))) &&
//                        SaveUtils.getString(keyList.get(j)).equals("报警")) {
//                    viewHolder.item_fan_info_error.setText(SaveUtils.getString(keyList.get(j)));
//                    viewHolder.item_fan_info_error.setTextColor(context.getResources().getColor(R.color.red_f6));
//                }else {
//                    viewHolder.item_fan_info_error.setText("正常");
//                }
//            }
//
//        }
        if (keyList.contains(name_status) && !TextUtils.isEmpty(SaveUtils.getString(name_status))) {
            switch (SaveUtils.getString(name_status)) {
                case "开":
                    viewHolder.item_fan_info_status.setImageResource(R.drawable.vector_drawable_fan_open);
                    break;
                case "关":
                    viewHolder.item_fan_info_status.setImageResource(R.drawable.vector_drawable_fan_close);
                    break;
                case "离线":
                    viewHolder.item_fan_info_status.setImageResource(R.drawable.vector_drawable_fan_line);
                    break;
                default:
                    break;
            }

        }
        if (keyList.contains(name_alarm) && !TextUtils.isEmpty(SaveUtils.getString(name_alarm))) {
            switch (SaveUtils.getString(name_alarm)) {
                case "故障":
                    viewHolder.item_fan_info_error.setText("报警");
                    viewHolder.item_fan_info_error.setTextColor(context.getResources().getColor(R.color.red_f6));
                    break;
                default:
                    viewHolder.item_fan_info_error.setText("正常");
                    viewHolder.item_fan_info_error.setTextColor(context.getResources().getColor(R.color.gray_4a));
                    break;
            }
        }
        viewHolder.item_fan_info_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onOpen(i);
            }
        });
        viewHolder.item_fan_info_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onClose(i);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_fan_info_name, item_fan_info_open, item_fan_info_close, item_fan_info_error;
        ImageView item_fan_info_status;
    }

    public interface OnSwitchListener {
        void onOpen(int position);

        void onClose(int position);
    }
}
