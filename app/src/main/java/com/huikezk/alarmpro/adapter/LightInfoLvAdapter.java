package com.huikezk.alarmpro.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.entity.LampBean;
import com.huikezk.alarmpro.entity.LightEntity;
import com.huikezk.alarmpro.utils.Const;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.views.GridViewForScrollView;

import net.igenius.mqttservice.MQTTServiceCommand;

import java.util.ArrayList;
import java.util.List;

import anet.channel.security.ISecurity;


/**
 * Created by MaRufei
 * on 2018/5/29.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class LightInfoLvAdapter extends BaseAdapter {
    private String sendName;
    private String TAG = "RepairLvAdapter";
    private Context context;
    private List<LightEntity.DataBean.InfoBean> listData = new ArrayList<>();

    public LightInfoLvAdapter(Context context, String sendName) {
        this.context = context;
        this.sendName = sendName;

    }

    public void setListData(List<LightEntity.DataBean.InfoBean> listData) {
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
            view = View.inflate(context, R.layout.item_light_info, null);
            viewHolder.item_light_info_title = view.findViewById(R.id.item_light_info_title);
            viewHolder.item_light_info_open = view.findViewById(R.id.item_light_info_open);
            viewHolder.item_light_info_close = view.findViewById(R.id.item_light_info_close);
            viewHolder.item_light_info_gv = view.findViewById(R.id.item_light_info_gv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.item_light_info_title.setText(listData.get(i).getTitle() + ":" +
                listData.get(i).getNumber() + "（" + listData.get(i).getType() + "）");
        final String sendName1 = sendName + listData.get(i).getTitle() + "/";
        final LightInfoItemLvAdapter adapter = new LightInfoItemLvAdapter(context, sendName1);
        final List<LampBean> list = new ArrayList<>();
        for (int j = 0; j < listData.get(i).getNumber(); j++) {
            LampBean lampBean = new LampBean();
            lampBean.setNum(j);
            lampBean.setSelect(false);
            lampBean.setType(listData.get(i).getType());
            list.add(lampBean);
        }
        MyUtils.Loge(TAG, "LampBean:" + list.toString());
        adapter.setList(list);
        adapter.setType(list.get(i).getType());
        viewHolder.item_light_info_gv.setAdapter(adapter);
        viewHolder.item_light_info_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).isSelect()) {
                    for (LampBean lampBean : list) {
                        lampBean.setSelect(false);
                    }
                } else {
                    for (int k = 0; k < list.size(); k++) {
                        if (k == position) {
                            list.get(k).setSelect(true);
                        } else {
                            list.get(k).setSelect(false);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        viewHolder.item_light_info_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否全部选中
                boolean isSelect = true;
                int index = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        index = i + 1;
                        isSelect = false;
                    }
                }
                if (isSelect) {//打开全部
                    showAlertDialog("温馨提示",
                            "确定执行全部开启？",
                            "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    for (int i = 0; i < list.size(); i++) {
                                        MQTTServiceCommand.publish(context,
                                                sendName1 + (i + 1) + "/order",
                                                Const.OPEN.getBytes());
                                    }
                                }
                            }, "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                } else {//打开一个
                    final int finalIndex = index;
                    showAlertDialog("温馨提示",
                            "确定执行开启？",
                            "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    MQTTServiceCommand.publish(context,
                                            sendName1 + finalIndex + "/order",
                                            Const.OPEN.getBytes());
                                }
                            }, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });

        viewHolder.item_light_info_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否全部选中
                boolean isSelect = true;
                int index = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        index = i + 1;
                        isSelect = false;
                    }
                }

                if (isSelect) {//关闭全部
                    showAlertDialog("温馨提示",
                            "确定执行全部关闭？",
                            "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    for (int i = 0; i < list.size(); i++) {
                                        MQTTServiceCommand.publish(context,
                                                sendName1 + (i + 1) + "/order",
                                                Const.CLOSE.getBytes());
                                    }
                                }
                            }, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                } else {//关闭一个
                    final int finalIndex = index;
                    showAlertDialog("温馨提示",
                            "确定执行关闭？",
                            "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    MQTTServiceCommand.publish(context,
                                            sendName1 + finalIndex + "/order",
                                            Const.CLOSE.getBytes());
                                }
                            }, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });
        return view;
    }

    class ViewHolder {
        TextView item_light_info_title, item_light_info_open, item_light_info_close;
        GridViewForScrollView item_light_info_gv;
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     **/
    public void showAlertDialog(String title, String message,
                                String positiveText,
                                DialogInterface.OnClickListener onClickListener,
                                String negativeText,
                                DialogInterface.OnClickListener onClickListener2) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener2).setCancelable(false)
                .show();
    }
}
