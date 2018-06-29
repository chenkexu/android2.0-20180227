/**
 * Copyright 2017 bejson.com
 */
package com.orientdata.lookforcustomers.wx;

/**
 * Auto-generated: 2017-07-25 19:45:35
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class WXPayBean {

    private boolean isok;
    private Data data;
    public void setIsok(boolean isok) {
        this.isok = isok;
    }
    public boolean getIsok() {
        return isok;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }


    public class Data {

        private String appid;
        private String partnerid;
        private String prepayid;
        private String package_val;
        private String noncestr;
        private int timestamp;
        private String sign;
        public void setAppid(String appid) {
            this.appid = appid;
        }
        public String getAppid() {
            return appid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }
        public String getPartnerid() {
            return partnerid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }
        public String getPrepayid() {
            return prepayid;
        }

        public void setPackage_val(String package_val) {
            this.package_val = package_val;
        }
        public String getPackage_val() {
            return package_val;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }
        public String getNoncestr() {
            return noncestr;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
        public int getTimestamp() {
            return timestamp;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
        public String getSign() {
            return sign;
        }

    }

}