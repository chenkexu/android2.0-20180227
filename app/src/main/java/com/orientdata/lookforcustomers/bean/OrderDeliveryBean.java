package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by ckx on 2018/6/28.
 */

public class OrderDeliveryBean implements Serializable {

//
//    public OrderDeliveryBean(int expediteId, String criticalValue, TaskBean task, String accelerateValueS, double rate, int expediteValue, int expireDate, int surplusDate) {
//        this.expediteId = expediteId;
//        this.criticalValue = criticalValue;
//        this.task = task;
//        this.accelerateValueS = accelerateValueS;
//        this.rate = rate;
//        this.expediteValue = expediteValue;
//        this.expireDate = expireDate;
//        this.surplusDate = surplusDate;
//    }

    private static final long serialVersionUID = 6404301322531901366L;


    /**
     * err : {"code":0,"msg":"正常","eventId":""}
     * result : {"expediteId":25,"criticalValue":"60","task":{"taskNo":"120180627120244128515","taskName":"20180627_120215_山东省泰安市岱岳区","userId":518,"type":1,"status":7,"rangeRadius":"2000","budget":1000000,"actualAmount":0,"invoiceStatus":2,"orientationSettingsId":752,"throwStartdate":"2018-06-27 00:00:00","throwEnddate":"2018-06-27 00:00:00","estimatePeoplerno":1000000,"content":"【不不3122313回复TD，退订","provinceCode":"370000","cityCode":"370900","throwAddress":"山东省泰安市岱岳区","createDate":"2018-06-27 12:02:44","delFlag":0,"longitude":116.43,"dimension":116.42,"adImgid":"116.42","address":"山东省泰安市岱岳区","plat":"ios","taskId":27},"accelerateValueS":"10","rate":0.02,"expediteValue":113,"expireDate":0,"surplusDate":3616092}
     */


        /**
         * expediteId : 25       //加速ID ，调用戳我加速接口的的时候用的
         * criticalValue : 60     //加到60s就不能戳了
         * task : {"taskNo":"120180627120244128515","taskName":"20180627_120215_山东省泰安市岱岳区","userId":518,"type":1,"status":7,"rangeRadius":"2000","budget":1000000,"actualAmount":0,"invoiceStatus":2,"orientationSettingsId":752,"throwStartdate":"2018-06-27 00:00:00","throwEnddate":"2018-06-27 00:00:00","estimatePeoplerno":1000000,"content":"【不不3122313回复TD，退订","provinceCode":"370000","cityCode":"370900","throwAddress":"山东省泰安市岱岳区","createDate":"2018-06-27 12:02:44","delFlag":0,"longitude":116.43,"dimension":116.42,"adImgid":"116.42","address":"山东省泰安市岱岳区","plat":"ios","taskId":27}
         * accelerateValueS : 10   //戳一次加10s
         * rate : 0.02             //0.02s加一条数据
         * expireDate : 0        //开始倒计时的时间
         * surplusDate : 3616092   //总的投放时间
         */

        private int expediteId;
        private String criticalValue;
        private TaskBean task;
        private String accelerateValueS;
        private double rate;
        private int expediteValue;
        private int expireDate;
        private int surplusDate;

        public int getExpediteId() {
            return expediteId;
        }

        public void setExpediteId(int expediteId) {
            this.expediteId = expediteId;
        }

        public String getCriticalValue() {
            return criticalValue;
        }

        public void setCriticalValue(String criticalValue) {
            this.criticalValue = criticalValue;
        }

        public TaskBean getTask() {
            return task;
        }

        public void setTask(TaskBean task) {
            this.task = task;
        }

        public String getAccelerateValueS() {
            return accelerateValueS;
        }

        public void setAccelerateValueS(String accelerateValueS) {
            this.accelerateValueS = accelerateValueS;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public int getExpediteValue() {
            return expediteValue;
        }

        public void setExpediteValue(int expediteValue) {
            this.expediteValue = expediteValue;
        }

        public int getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(int expireDate) {
            this.expireDate = expireDate;
        }

        public int getSurplusDate() {
            return surplusDate;
        }

        public void setSurplusDate(int surplusDate) {
            this.surplusDate = surplusDate;
        }




     private TaskOut result;




    public TaskOut getResult() {
        return result;
    }

    public void setResult(TaskOut result) {
        this.result = result;
    }


















    public static class TaskBean implements Serializable{
        private static final long serialVersionUID = -1802663880762982743L;
        /**
             * taskNo : 120180627120244128515
             * taskName : 20180627_120215_山东省泰安市岱岳区
             * userId : 518
             * type : 1
             * status : 7
             * rangeRadius : 2000
             * budget : 1000000.0
             * actualAmount : 0.0
             * invoiceStatus : 2
             * orientationSettingsId : 752
             * throwStartdate : 2018-06-27 00:00:00
             * throwEnddate : 2018-06-27 00:00:00
             * estimatePeoplerno : 1000000
             * content : 【不不3122313回复TD，退订
             * provinceCode : 370000
             * cityCode : 370900
             * throwAddress : 山东省泰安市岱岳区
             * createDate : 2018-06-27 12:02:44
             * delFlag : 0
             * longitude : 116.43
             * dimension : 116.42
             * adImgid : 116.42
             * address : 山东省泰安市岱岳区
             * plat : ios
             * taskId : 27
             */

            private String taskNo;
            private String taskName;
            private int userId;
            private int type;
            private int status;
            private String rangeRadius;
            private double budget;
            private double actualAmount;
            private int invoiceStatus;
            private int orientationSettingsId;
            private String throwStartdate;
            private String throwEnddate;
            private int estimatePeoplerno;
            private String content;
            private String provinceCode;
            private String cityCode;
            private String throwAddress;
            private String createDate;
            private int delFlag;
            private double longitude;
            private double dimension;
            private String adImgid;
            private String address;
            private String plat;
            private int taskId;

            public String getTaskNo() {
                return taskNo;
            }

            public void setTaskNo(String taskNo) {
                this.taskNo = taskNo;
            }

            public String getTaskName() {
                return taskName;
            }

            public void setTaskName(String taskName) {
                this.taskName = taskName;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getRangeRadius() {
                return rangeRadius;
            }

            public void setRangeRadius(String rangeRadius) {
                this.rangeRadius = rangeRadius;
            }

            public double getBudget() {
                return budget;
            }

            public void setBudget(double budget) {
                this.budget = budget;
            }

            public double getActualAmount() {
                return actualAmount;
            }

            public void setActualAmount(double actualAmount) {
                this.actualAmount = actualAmount;
            }

            public int getInvoiceStatus() {
                return invoiceStatus;
            }

            public void setInvoiceStatus(int invoiceStatus) {
                this.invoiceStatus = invoiceStatus;
            }

            public int getOrientationSettingsId() {
                return orientationSettingsId;
            }

            public void setOrientationSettingsId(int orientationSettingsId) {
                this.orientationSettingsId = orientationSettingsId;
            }

            public String getThrowStartdate() {
                return throwStartdate;
            }

            public void setThrowStartdate(String throwStartdate) {
                this.throwStartdate = throwStartdate;
            }

            public String getThrowEnddate() {
                return throwEnddate;
            }

            public void setThrowEnddate(String throwEnddate) {
                this.throwEnddate = throwEnddate;
            }

            public int getEstimatePeoplerno() {
                return estimatePeoplerno;
            }

            public void setEstimatePeoplerno(int estimatePeoplerno) {
                this.estimatePeoplerno = estimatePeoplerno;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getProvinceCode() {
                return provinceCode;
            }

            public void setProvinceCode(String provinceCode) {
                this.provinceCode = provinceCode;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getThrowAddress() {
                return throwAddress;
            }

            public void setThrowAddress(String throwAddress) {
                this.throwAddress = throwAddress;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public int getDelFlag() {
                return delFlag;
            }

            public void setDelFlag(int delFlag) {
                this.delFlag = delFlag;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getDimension() {
                return dimension;
            }

            public void setDimension(double dimension) {
                this.dimension = dimension;
            }

            public String getAdImgid() {
                return adImgid;
            }

            public void setAdImgid(String adImgid) {
                this.adImgid = adImgid;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPlat() {
                return plat;
            }

            public void setPlat(String plat) {
                this.plat = plat;
            }

            public int getTaskId() {
                return taskId;
            }

            public void setTaskId(int taskId) {
                this.taskId = taskId;
            }
        }












    }
