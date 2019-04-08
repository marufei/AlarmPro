package com.huikezk.alarmpro.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/13.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class LightEntity extends BaseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * name : 1FA
         * info : [{"title":"4井副AL-1-1","number":20,"type":"照明"},{"title":"4井副AL-1-2","number":20,"type":"插座"}]
         */

        private String name;
        private List<InfoBean> info;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean implements Serializable{
            /**
             * title : 4井副AL-1-1
             * number : 20
             * type : 照明
             */

            private String title;
            private int number;
            private String type;

            private boolean select;

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
