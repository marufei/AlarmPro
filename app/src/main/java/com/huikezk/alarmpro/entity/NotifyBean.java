package com.huikezk.alarmpro.entity;

/**
 * Created by MaRufei
 * on 2019/5/1.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class NotifyBean {
    private String projectName;
    private String messageType;
    private String _ALIYUN_NOTIFICATION_ID_;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String get_ALIYUN_NOTIFICATION_ID_() {
        return _ALIYUN_NOTIFICATION_ID_;
    }

    public void set_ALIYUN_NOTIFICATION_ID_(String _ALIYUN_NOTIFICATION_ID_) {
        this._ALIYUN_NOTIFICATION_ID_ = _ALIYUN_NOTIFICATION_ID_;
    }

    @Override
    public String toString() {
        return "NotifyBean{" +
                "projectName='" + projectName + '\'' +
                ", messageType='" + messageType + '\'' +
                ", _ALIYUN_NOTIFICATION_ID_='" + _ALIYUN_NOTIFICATION_ID_ + '\'' +
                '}';
    }
}
