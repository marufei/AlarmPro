package com.huikezk.alarmpro.entity;

import java.util.List;

/**
 * Created by MaRufei
 * on 2019/3/6.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class RepairHistoryEntity extends BaseEntity {

    /**
     * data : {"repairAll":4,"finishrepairAll":0,"data":[{"name":"常志明","rank":{"repair":4,"finishRepair":0}}]}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * repairAll : 4
         * finishrepairAll : 0
         * data : [{"name":"常志明","rank":{"repair":4,"finishRepair":0}}]
         */

        private int repairAll;
        private int finishrepairAll;
        private List<DataBean> data;

        public int getRepairAll() {
            return repairAll;
        }

        public void setRepairAll(int repairAll) {
            this.repairAll = repairAll;
        }

        public int getFinishrepairAll() {
            return finishrepairAll;
        }

        public void setFinishrepairAll(int finishrepairAll) {
            this.finishrepairAll = finishrepairAll;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * name : 常志明
             * rank : {"repair":4,"finishRepair":0}
             */

            private String name;
            private RankBean rank;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public RankBean getRank() {
                return rank;
            }

            public void setRank(RankBean rank) {
                this.rank = rank;
            }

            public static class RankBean {
                /**
                 * repair : 4
                 * finishRepair : 0
                 */

                private int repair;
                private int finishRepair;

                public int getRepair() {
                    return repair;
                }

                public void setRepair(int repair) {
                    this.repair = repair;
                }

                public int getFinishRepair() {
                    return finishRepair;
                }

                public void setFinishRepair(int finishRepair) {
                    this.finishRepair = finishRepair;
                }
            }
        }
    }
}
