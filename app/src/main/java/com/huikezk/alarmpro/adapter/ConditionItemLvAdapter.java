package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class ConditionItemLvAdapter extends BaseAdapter {
    private String TAG="ConditionItemLvAdapter";
    private Context context;
    private String sendName;
    private List<String> listData = new ArrayList<>();
    private OnSwitchListener onSwitchListener;

    public ConditionItemLvAdapter(Context context,String sendName) {
        this.context = context;
        this.sendName=sendName;
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
            view = View.inflate(context, R.layout.item_condition1_info, null);
            viewHolder.item_condition1_title = view.findViewById(R.id.item_condition1_title);
            viewHolder.item_condition1_content1 = view.findViewById(R.id.item_condition1_content1);
            viewHolder.item_condition1_content2 = view.findViewById(R.id.item_condition1_content2);
            viewHolder.item_condition1_open = view.findViewById(R.id.item_condition1_open);
            viewHolder.item_condition1_close = view.findViewById(R.id.item_condition1_close);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MyUtils.Loge(TAG,"title:"+listData.get(i));
        viewHolder.item_condition1_title.setText(listData.get(i)+"控制");
        List<String> keyList = SaveUtils.getAllKeys();
//        for (int j = 0; j < keyList.size(); j++) {
//            if (keyList.get(j).equals(sendName+listData.get(i)+"状态")){
//                viewHolder.item_condition1_content1.setText(SaveUtils.getString(keyList.get(j)));
//            }
//            if (keyList.get(j).equals(sendName+listData.get(i)+"手自动")){
//                viewHolder.item_condition1_content2.setText(SaveUtils.getString(keyList.get(j)));
//            }
//
//        }
        MyUtils.Loge(TAG,"title:"+sendName+listData.get(i)+"状态");
        MyUtils.Loge(TAG,"title:"+sendName+listData.get(i)+"手自动");
        if (keyList.contains(sendName+listData.get(i)+"状态")){
            viewHolder.item_condition1_content1.setText(SaveUtils.getString(sendName+listData.get(i)+"状态"));
        }
        if(keyList.contains(sendName+listData.get(i)+"手自动")){
            viewHolder.item_condition1_content2.setText(SaveUtils.getString(sendName+listData.get(i)+"手自动"));
        }
        viewHolder.item_condition1_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onOpen(i);
            }
        });
        viewHolder.item_condition1_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchListener.onClose(i);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_condition1_content1,item_condition1_content2,item_condition1_title,
                item_condition1_open,item_condition1_close;

    }

    public interface OnSwitchListener{
        void onOpen(int position);
        void onClose(int position);
    }
}
