package com.huikezk.alarmpro.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by MaRufei
 * on 2019/3/10.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class RepairEntity implements Comparable<RepairEntity>,Serializable {

    /**
     * id : 5c7f25bf07199b1c88a03d66
     * nickName : 常志明
     * username :
     * repairInfo :
     * datetime : 2019-03-06 09:43:27
     */

    private String id;
    private String nickName;
    private String username;
    private String repairInfo;
    private String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepairInfo() {
        return repairInfo;
    }

    public void setRepairInfo(String repairInfo) {
        this.repairInfo = repairInfo;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "RepairEntity{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", username='" + username + '\'' +
                ", repairInfo='" + repairInfo + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull RepairEntity o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(this.getDatetime());
            dt2 = sdf.parse(o.getDatetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            return -1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return 1;
        }
        return 0;
    }
}
