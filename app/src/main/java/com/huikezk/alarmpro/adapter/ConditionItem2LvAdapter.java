package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
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
public class ConditionItem2LvAdapter extends BaseAdapter {
    private String TAG="ConditionItemLvAdapter";
    private Context context;
    private String sendName;
    private List<String> listData = new ArrayList<>();

    public ConditionItem2LvAdapter(Context context, String sendName) {
        this.context = context;
        this.sendName=sendName;
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
            view = View.inflate(context, R.layout.item_condition2_info, null);
            viewHolder.item_condition2_content1 = view.findViewById(R.id.item_condition2_content1);
            viewHolder.item_condition2_name1 = view.findViewById(R.id.item_condition2_name1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.item_condition2_name1.setText(listData.get(i));
        List<String> keyList = SaveUtils.getAllKeys();
        for (int j = 0; j < keyList.size(); j++) {
            if (keyList.get(j).equals(sendName+listData.get(i)+"状态")){
                if (!TextUtils.isEmpty(SaveUtils.getString(keyList.get(j)))&&
                        SaveUtils.getString(keyList.get(j)).equals("报警")) {
                    viewHolder.item_condition2_content1.setText(SaveUtils.getString(keyList.get(j)));
                    viewHolder.item_condition2_content1.setTextColor(context.getResources().getColor(R.color.red_f6));
                }
            }

        }
        return view;
    }

    class ViewHolder {
        TextView item_condition2_content1,item_condition2_name1;
    }
}
