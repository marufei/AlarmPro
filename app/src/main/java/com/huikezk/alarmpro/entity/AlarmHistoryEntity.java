package com.huikezk.alarmpro.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/9.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class AlarmHistoryEntity extends BaseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 隔油间监测
         * recordList : ["高液位报警","超高液位报警"]
         */

        private String title;
        private List<String> recordList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<String> recordList) {
            this.recordList = recordList;
        }
    }
}
