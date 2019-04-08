package com.huikezk.alarmpro.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/7.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class TimeManagerEntity extends BaseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * type : 空调监控
         * times : [{"name":"K-1-1","closeValue":0},{"name":"K-1-2","closeValue":0}]
         */

        private String type;
        private List<TimesBean> times;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<TimesBean> getTimes() {
            return times;
        }

        public void setTimes(List<TimesBean> times) {
            this.times = times;
        }

        public static class TimesBean {
            /**
             * name : K-1-1
             * closeValue : 0
             */

            private String name;
            private int closeValue;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCloseValue() {
                return closeValue;
            }

            public void setCloseValue(int closeValue) {
                this.closeValue = closeValue;
            }
        }
    }
}
