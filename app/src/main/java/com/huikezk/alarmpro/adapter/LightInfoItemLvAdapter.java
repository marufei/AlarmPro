package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AirEntity;
import com.huikezk.alarmpro.entity.LampBean;
import com.huikezk.alarmpro.utils.KeyUtils;
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
public class LightInfoItemLvAdapter extends BaseAdapter {
    private String sendName;
    private String TAG = "LightInfoItemLvAdapter";
    private Context context;
    private List<LampBean> list = new ArrayList<>();
    private List<String> keyList;
    private String type;

    public LightInfoItemLvAdapter(Context context, String sendName) {
        this.context = context;
        this.sendName = sendName;
        keyList = SaveUtils.getAllKeys();
    }

    public void setList(List<LampBean> list) {
        this.list = list;
    }


    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            view = View.inflate(context, R.layout.item_lv_light_info, null);
            viewHolder.item_lv_light_info_number = view.findViewById(R.id.item_lv_light_info_number);
            viewHolder.item_lv_light_info_light = view.findViewById(R.id.item_lv_light_info_light);
            viewHolder.item_ll_light_info_bac = view.findViewById(R.id.item_ll_light_info_bac);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.item_lv_light_info_number.setText(String.valueOf(i + 1));
        String sendName1 = sendName + String.valueOf(i + 1);
        MyUtils.Loge(TAG, "list.get(" + i + ").getType():" + list.get(i).getType());
        switch (type) {
            case "照明":
                if (keyList != null && keyList.contains(sendName1) && !TextUtils.isEmpty(SaveUtils.getString(sendName1))) {
                    if (SaveUtils.getString(sendName1).equals("开")) {
                        viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_light_open);
                    } else if (SaveUtils.getString(sendName1).equals("离线")) {
                        viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_light_line);
                    } else {
                        viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_light_close);
                    }
                } else {
                    viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_light_close);
                }
                break;
            case "插座":
                if (keyList != null && keyList.contains(sendName1) && !TextUtils.isEmpty(SaveUtils.getString(sendName1))) {
                    if (SaveUtils.getString(sendName1).equals("开")) {
                        viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_socket_open);
                    } else if (SaveUtils.getString(sendName1).equals("离线")) {
                        viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_socket_line);
                    } else {
                        viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_socket_close);
                    }
                } else {
                    viewHolder.item_lv_light_info_light.setImageResource(R.drawable.vector_drawable_socket_close);
                }
                break;

        }


        if (list.get(i).isSelect()) {
            viewHolder.item_ll_light_info_bac.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.item_ll_light_info_bac.setBackgroundColor(context.getResources().getColor(R.color.bac));
        }

        return view;
    }

    class ViewHolder {
        TextView item_lv_light_info_number;
        ImageView item_lv_light_info_light;
        LinearLayout item_ll_light_info_bac;
    }

}
