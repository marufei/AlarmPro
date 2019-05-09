package com.huikezk.alarmpro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MaRufei
 * on 2019/5/9.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MyReceiver extends BroadcastReceiver{
    private OnMyReceiverListener onMyReceive;

    public void setOnMyReceive(OnMyReceiverListener onMyReceive) {
        this.onMyReceive = onMyReceive;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("myReceiver")) {
            onMyReceive.onMyReceiver(context, intent);
        }
    }

    public interface OnMyReceiverListener{
        void onMyReceiver(Context context,Intent intent);
    }
}

