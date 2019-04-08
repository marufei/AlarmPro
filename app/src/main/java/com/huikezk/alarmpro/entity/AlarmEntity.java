package com.huikezk.alarmpro.entity;

import java.io.Serializable;

/**
 * Created by MaRufei
 * on 2019/3/10.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AlarmEntity implements Serializable {

    /**
     * info : 照明故障
     * deviceName : 照明监控
     * target : Lighting
     * number : 1FB
     */

    private String info;
    private String deviceName;
    private String target;
    private String number;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
