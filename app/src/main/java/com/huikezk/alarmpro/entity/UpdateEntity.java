package com.huikezk.alarmpro.entity;

/**
 * Created by MaRufei
 * on 2019/5/4.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class UpdateEntity extends BaseEntity {


    /**
     * data : {"version_name":"0.0.2","version_code":2,"title":"发现新版本,快来使用吧!","description":"","platform":"android","type":"apk","hotupdate_type":"tip","android_url":"http://mqtt.huikezk.com:3000/apk/H58233138_0114110017.apk"}
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
         * version_name : 0.0.2
         * version_code : 2
         * title : 发现新版本,快来使用吧!
         * description :
         * platform : android
         * type : apk
         * hotupdate_type : tip
         * android_url : http://mqtt.huikezk.com:3000/apk/H58233138_0114110017.apk
         */

        private String version_name;
        private int version_code;
        private String description;
        private String platform;
        private String type;
        private String hotupdate_type;
        private String android_url;

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHotupdate_type() {
            return hotupdate_type;
        }

        public void setHotupdate_type(String hotupdate_type) {
            this.hotupdate_type = hotupdate_type;
        }

        public String getAndroid_url() {
            return android_url;
        }

        public void setAndroid_url(String android_url) {
            this.android_url = android_url;
        }
    }
}
