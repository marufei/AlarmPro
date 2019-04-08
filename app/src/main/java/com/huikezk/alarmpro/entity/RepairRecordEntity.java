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
public class RepairRecordEntity extends BaseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 5c7f258ea41d1a1dec0f5832
         * nickName : 常志明
         * username :
         * repairInfo :
         * datetime : 2019-03-06 09:42:38
         * finishNickName :
         * finishUsername :
         * finishRepairInfo :
         * finishDatetime : 2019-03-06 09:42:38
         */

        private String _id;
        private String nickName;
        private String username;
        private String repairInfo;
        private String datetime;
        private String finishNickName;
        private String finishUsername;
        private String finishRepairInfo;
        private String finishDatetime;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
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

        public String getFinishNickName() {
            return finishNickName;
        }

        public void setFinishNickName(String finishNickName) {
            this.finishNickName = finishNickName;
        }

        public String getFinishUsername() {
            return finishUsername;
        }

        public void setFinishUsername(String finishUsername) {
            this.finishUsername = finishUsername;
        }

        public String getFinishRepairInfo() {
            return finishRepairInfo;
        }

        public void setFinishRepairInfo(String finishRepairInfo) {
            this.finishRepairInfo = finishRepairInfo;
        }

        public String getFinishDatetime() {
            return finishDatetime;
        }

        public void setFinishDatetime(String finishDatetime) {
            this.finishDatetime = finishDatetime;
        }
    }
}
