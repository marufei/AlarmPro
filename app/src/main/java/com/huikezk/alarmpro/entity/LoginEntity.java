package com.huikezk.alarmpro.entity;


import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/1.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class LoginEntity extends BaseEntity{


    /**
     * data : {"_id":"5c330e771805bc1b806fb2d4","username":"18511834487","password":"123456","projectName":[{"projectNum":1,"projectName":"北京华联回龙观","modules":["隔油间监测","空调监控","照明监控","风机监控","空气质量","燃气监测","远程抄表","电力监控"]}],"mqttUrl":"183.230.40.39:1883","projectNum":1,"ip":"mqtt.huikezk.com:3000","image":"/upload/12780c6e2f75939ea88a33ae7c06d076.jpg","nickName":"常志明","CID":"44f381c3aac87713424a59d3859c969b","mqttClientId":"519682992"}
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
         * _id : 5c330e771805bc1b806fb2d4
         * username : 18511834487
         * password : 123456
         * projectName : [{"projectNum":1,"projectName":"北京华联回龙观","modules":["隔油间监测","空调监控","照明监控","风机监控","空气质量","燃气监测","远程抄表","电力监控"]}]
         * mqttUrl : 183.230.40.39:1883
         * projectNum : 1
         * ip : mqtt.huikezk.com:3000
         * image : /upload/12780c6e2f75939ea88a33ae7c06d076.jpg
         * nickName : 常志明
         * CID : 44f381c3aac87713424a59d3859c969b
         * mqttClientId : 519682992
         */

        private String _id;
        private String username;
        private String password;
        private String mqttUrl;
        private int projectNum;
        private String ip;
        private String image;
        private String nickName;
        private String CID;
        private String mqttClientId;
        private List<ProjectNameBean> projectName;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMqttUrl() {
            return mqttUrl;
        }

        public void setMqttUrl(String mqttUrl) {
            this.mqttUrl = mqttUrl;
        }

        public int getProjectNum() {
            return projectNum;
        }

        public void setProjectNum(int projectNum) {
            this.projectNum = projectNum;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCID() {
            return CID;
        }

        public void setCID(String CID) {
            this.CID = CID;
        }

        public String getMqttClientId() {
            return mqttClientId;
        }

        public void setMqttClientId(String mqttClientId) {
            this.mqttClientId = mqttClientId;
        }

        public List<ProjectNameBean> getProjectName() {
            return projectName;
        }

        public void setProjectName(List<ProjectNameBean> projectName) {
            this.projectName = projectName;
        }

        public static class ProjectNameBean {
            /**
             * projectNum : 1
             * projectName : 北京华联回龙观
             * modules : ["隔油间监测","空调监控","照明监控","风机监控","空气质量","燃气监测","远程抄表","电力监控"]
             */

            private int projectNum;
            private String projectName;
            private List<String> modules;

            public int getProjectNum() {
                return projectNum;
            }

            public void setProjectNum(int projectNum) {
                this.projectNum = projectNum;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public List<String> getModules() {
                return modules;
            }

            public void setModules(List<String> modules) {
                this.modules = modules;
            }
        }
    }
}
