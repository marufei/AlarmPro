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
public class RepairInfoEntity extends BaseEntity {

    /**
     * data : {"_id":"5c7f265e1f31911b18e088df","nickName":"常志明","username":"","repairInfo":"","imgs":["",""],"datetime":"2019-03-06 09:46:06","finishNickName":"","finishUsername":"","finishRepairInfo":"","finishImgs":["",""],"finishDatetime":"2019-03-06 09:46:06"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 5c7f265e1f31911b18e088df
         * nickName : 常志明
         * username :
         * repairInfo :
         * imgs : ["",""]
         * datetime : 2019-03-06 09:46:06
         * finishNickName :
         * finishUsername :
         * finishRepairInfo :
         * finishImgs : ["",""]
         * finishDatetime : 2019-03-06 09:46:06
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
        private String imgs;
        private String finishImgs;

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

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getFinishImgs() {
            return finishImgs;
        }

        public void setFinishImgs(String finishImgs) {
            this.finishImgs = finishImgs;
        }
    }
}
