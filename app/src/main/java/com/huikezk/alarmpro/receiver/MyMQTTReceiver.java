package com.huikezk.alarmpro.receiver;

import android.content.Context;

import net.igenius.mqttservice.MQTTServiceReceiver;

/**
 * Created by MaRufei
 * on 2019/4/21.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MyMQTTReceiver extends MQTTServiceReceiver {
    @Override
    public void onPublishSuccessful(Context context, String requestId, String topic) {

    }

    @Override
    public void onSubscriptionSuccessful(Context context, String requestId, String topic) {

    }

    @Override
    public void onSubscriptionError(Context context, String requestId, String topic, Exception exception) {

    }

    @Override
    public void onMessageArrived(Context context, String topic, byte[] payload) {

    }

//    @Override
//    public void onMessageArrived(Context context, String topic, String payload) {
//
//    }

    @Override
    public void onConnectionSuccessful(Context context, String requestId) {

    }

    @Override
    public void onException(Context context, String requestId, Exception exception) {

    }

    @Override
    public void onConnectionStatus(Context context, boolean connected) {

    }
}
