package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.Const;
import com.huikezk.alarmpro.utils.PicassoUtlis;


import java.util.ArrayList;


/**
 * Created by MaRufei
 * on 2018/5/29.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class RepairGvAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> listData=new ArrayList<>();
    public RepairGvAdapter(Context context){
        this.context=context;
    }

    public void setListData(ArrayList<String> listData) {
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
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(context, R.layout.item_repair,null);
            viewHolder.item_repair_img=view.findViewById(R.id.item_repair_img);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
//        PicassoUtlis.Cornersimg(listData.get(i),viewHolder.item_repair_img, Const.CONNER_BIG);
        Glide.with(context).load("file://"+listData.get(i)).into(viewHolder.item_repair_img);
        return view;
    }

    class ViewHolder{
        ImageView item_repair_img;
    }
}
