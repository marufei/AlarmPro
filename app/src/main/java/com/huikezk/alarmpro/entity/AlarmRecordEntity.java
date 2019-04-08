package com.huikezk.alarmpro.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/17.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AlarmRecordEntity extends BaseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 5c8dc502abd1b216583bd484
         * type : 隔油间监测
         * info : 1号高液位报警
         * datetime : 2019-03-17 11:54:42
         */

        private String _id;
        private String type;
        private String info;
        private String datetime;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }
}
