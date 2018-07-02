package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/12/6.
 */
public class TaskThrowInfo implements Serializable {

    private static final long serialVersionUID = -5707839244027586543L;
    private int id;
        private int taskId;
        private int man;
        private int woman;
        private double age0_20;
        private double age20_25;
        private double age25_30;
        private double age30_35;
        private double age35_40;
        private double age40_45;
        private double age45_50;
        private double age50_55;
        private double age55_60;
        private double age60_;
        private String createDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int getMan() {
            return man;
        }

        public void setMan(int man) {
            this.man = man;
        }

        public int getWoman() {
            return woman;
        }

        public void setWoman(int woman) {
            this.woman = woman;
        }

        public double getAge0_20() {
            return age0_20;
        }

        public void setAge0_20(double age0_20) {
            this.age0_20 = age0_20;
        }

        public double getAge20_25() {
            return age20_25;
        }

        public void setAge20_25(double age20_25) {
            this.age20_25 = age20_25;
        }

        public double getAge25_30() {
            return age25_30;
        }

        public void setAge25_30(double age25_30) {
            this.age25_30 = age25_30;
        }

        public double getAge30_35() {
            return age30_35;
        }

        public void setAge30_35(double age30_35) {
            this.age30_35 = age30_35;
        }

        public double getAge35_40() {
            return age35_40;
        }

        public void setAge35_40(double age35_40) {
            this.age35_40 = age35_40;
        }

        public double getAge40_45() {
            return age40_45;
        }

        public void setAge40_45(double age40_45) {
            this.age40_45 = age40_45;
        }

        public double getAge45_50() {
            return age45_50;
        }

        public void setAge45_50(double age45_50) {
            this.age45_50 = age45_50;
        }

        public double getAge50_55() {
            return age50_55;
        }

        public void setAge50_55(double age50_55) {
            this.age50_55 = age50_55;
        }

        public double getAge55_60() {
            return age55_60;
        }

        public void setAge55_60(double age55_60) {
            this.age55_60 = age55_60;
        }

        public double getAge60_() {
            return age60_;
        }

        public void setAge60_(double age60_) {
            this.age60_ = age60_;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }
}
