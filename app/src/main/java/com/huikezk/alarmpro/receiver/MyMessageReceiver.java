package com.huikezk.alarmpro.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.google.gson.Gson;
import com.huikezk.alarmpro.activity.MainActivity;
import com.huikezk.alarmpro.entity.NotifyBean;
import com.huikezk.alarmpro.utils.MyUtils;

import java.util.Map;

public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        MyUtils.Loge("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        MyUtils.Loge("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        MyUtils.Loge("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        Gson gson = new Gson();
        NotifyBean notifyBean = gson.fromJson(extraMap, NotifyBean.class);
        MyUtils.Loge("MyMessageReceiver","MyMessageReceiver:---notify:"+notifyBean.toString());
        if (notifyBean != null &&
                !TextUtils.isEmpty(notifyBean.getProjectName()) &&
                !TextUtils.isEmpty(notifyBean.getMessageType())) {

            switch (notifyBean.getMessageType()) {
                case "alarm":
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("currentPage", 1);
                    intent.putExtra("projectName", notifyBean.getProjectName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "repair":
                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.putExtra("currentPage", 2);
                    intent1.putExtra("projectName", notifyBean.getProjectName());
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    break;
                default:
                    break;
            }


        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        MyUtils.Loge("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        MyUtils.Loge("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        MyUtils.Loge("MyMessageReceiver", "onNotificationRemoved--messageId:" + messageId);
    }

}
