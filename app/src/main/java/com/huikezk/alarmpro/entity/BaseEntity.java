package com.huikezk.alarmpro.entity;

import java.io.Serializable;

/**
 * Created by MaRufei
 * on 2019/1/9.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class BaseEntity  implements Serializable{
    /**
     * 状态 1.成功
     */
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
