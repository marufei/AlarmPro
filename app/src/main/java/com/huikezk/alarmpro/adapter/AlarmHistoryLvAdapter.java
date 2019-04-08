package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.AlarmHistoryEntity;
import com.huikezk.alarmpro.entity.RepairRecordEntity;
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
public class AlarmHistoryLvAdapter extends BaseAdapter {
    private Context context;
    private List<AlarmHistoryEntity.DataBean> listData=new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public AlarmHistoryLvAdapter(Context context){
        this.context=context;
    }

    public void setListData(List<AlarmHistoryEntity.DataBean> listData) {
        this.listData = listData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(context, R.layout.item_alarm_history,null);
            viewHolder.item_alarm_title=view.findViewById(R.id.item_alarm_title);
            viewHolder.item_alarm_lv=view.findViewById(R.id.item_alarm_lv);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        if (listData.get(i)!=null) {
            if (!TextUtils.isEmpty(listData.get(i).getTitle())) {
                viewHolder.item_alarm_title.setText("*"+listData.get(i).getTitle());
            }
            if (listData.get(i).getRecordList()!=null){
                AlarmHistoryItemLvAdapter adapter = new AlarmHistoryItemLvAdapter(context);
                adapter.setListData(listData.get(i).getRecordList());
                viewHolder.item_alarm_lv.setAdapter(adapter);
                viewHolder.item_alarm_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onItemClickListener.onItemClick(i,position);
                    }
                });
            }
        }

        return view;
    }

    class ViewHolder{
        TextView item_alarm_title;
        ListViewForScrollView item_alarm_lv;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos1,int pos2);
    }
}
