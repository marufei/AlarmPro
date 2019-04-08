package com.huikezk.alarmpro.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/10.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class ConditionEntity extends BaseEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * name : K-1F-1-1
         * info : {"AUH":["送风机","排风机"],"frequency":["送风机变频","排风机变频"],"valve":["新风阀门","排风阀门","分流阀门","水阀门"],"humiture":["送风温度","回风温度","回水温度"],"alarm":["防冻","压差"]}
         */

        private String name;
        private InfoBean info;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean implements Serializable {
            private List<String> AUH;
            private List<String> frequency;
            private List<String> valve;
            private List<String> humiture;
            private List<String> alarm;

            public List<String> getAUH() {
                return AUH;
            }

            public void setAUH(List<String> AUH) {
                this.AUH = AUH;
            }

            public List<String> getFrequency() {
                return frequency;
            }

            public void setFrequency(List<String> frequency) {
                this.frequency = frequency;
            }

            public List<String> getValve() {
                return valve;
            }

            public void setValve(List<String> valve) {
                this.valve = valve;
            }

            public List<String> getHumiture() {
                return humiture;
            }

            public void setHumiture(List<String> humiture) {
                this.humiture = humiture;
            }

            public List<String> getAlarm() {
                return alarm;
            }

            public void setAlarm(List<String> alarm) {
                this.alarm = alarm;
            }

            @Override
            public String toString() {
                return "InfoBean{" +
                        "AUH=" + AUH +
                        ", frequency=" + frequency +
                        ", valve=" + valve +
                        ", humiture=" + humiture +
                        ", alarm=" + alarm +
                        '}';
            }
        }
    }
}
