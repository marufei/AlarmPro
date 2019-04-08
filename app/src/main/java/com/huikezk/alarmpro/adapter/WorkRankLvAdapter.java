package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.RepairHistoryEntity;

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
public class WorkRankLvAdapter extends BaseAdapter {
    private Context context;
    private List<RepairHistoryEntity.DataBeanX.DataBean> listData=new ArrayList<>();
    /**
     * 报修总数
     */
    private int repairAll;
    /**
     * 维修总数
     */
    private int finishrepairAll;

    private RepairHistoryEntity.DataBeanX data=new RepairHistoryEntity.DataBeanX();
    public WorkRankLvAdapter(Context context){
        this.context=context;
    }

    public void setData(RepairHistoryEntity.DataBeanX data) {
        this.data = data;
        listData.clear();
        if (data.getData()!=null) {
            listData.addAll(data.getData());
        }
        finishrepairAll=data.getFinishrepairAll();
        repairAll=data.getRepairAll();
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
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(context, R.layout.item_work_rank,null);
            viewHolder.item_work_rank_name=view.findViewById(R.id.item_work_rank_name);
            viewHolder.item_work_rank_num1=view.findViewById(R.id.item_work_rank_num1);
            viewHolder.item_work_rank_num2=view.findViewById(R.id.item_work_rank_num2);
            viewHolder.item_work_rank_pg1=view.findViewById(R.id.item_work_rank_pg1);
            viewHolder.item_work_rank_pg2=view.findViewById(R.id.item_work_rank_pg2);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.item_work_rank_name.setText(listData.get(i).getName());
        viewHolder.item_work_rank_num1.setText(String.valueOf(listData.get(i).getRank().getRepair())+"单");
        viewHolder.item_work_rank_num2.setText(String.valueOf(listData.get(i).getRank().getFinishRepair())+"单");
        viewHolder.item_work_rank_pg1.setProgress(listData.get(i).getRank().getRepair());
        viewHolder.item_work_rank_pg1.setMax(repairAll);
        viewHolder.item_work_rank_pg2.setProgress(listData.get(i).getRank().getFinishRepair());
        viewHolder.item_work_rank_pg2.setMax(finishrepairAll);

        return view;
    }

    class ViewHolder{
        TextView item_work_rank_name,item_work_rank_num1,item_work_rank_num2;
        ProgressBar item_work_rank_pg1,item_work_rank_pg2;
    }
}
