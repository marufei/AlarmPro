package com.huikezk.alarmpro.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.Const;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.PicassoUtlis;

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
public class RepairInfoGvAdapter extends BaseAdapter {
    private Context context;
    private List<String> listData=new ArrayList<>();
    public RepairInfoGvAdapter(Context context){
        this.context=context;
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
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(context, R.layout.item_repair,null);
            viewHolder.item_repair_img=view.findViewById(R.id.item_repair_img);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
//        viewHolder.item_repair_img.setImageBitmap(listData.get(i));
        MyUtils.Loge("AAA", listData.get(i));
        Glide.with(context).load(listData.get(i)).into(viewHolder.item_repair_img);
//        PicassoUtlis.Cornersimg(listData.get(i),viewHolder.item_repair_img, Const.CONNER_BIG);
        return view;
    }

    class ViewHolder{
        ImageView item_repair_img;
    }
}
