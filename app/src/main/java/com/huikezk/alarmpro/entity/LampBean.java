package com.huikezk.alarmpro.entity;

/**
 * Created by MaRufei
 * on 2019/3/17.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class LampBean {
    private int num;
    private boolean select;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "LampBean{" +
                "num=" + num +
                ", select=" + select +
                ", type='" + type + '\'' +
                '}';
    }
}
