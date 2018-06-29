package com.orientdata.lookforcustomers.zfb;

/**
 * Created by zhouqiang on 2017/7/18.
 */


/**
 * Auto-generated: 2017-07-18 14:22:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PayBean {

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


    public class NeedToSignData {

        private String app_id;
        private String method;
        private String format;
        private String charset;
        private String sign_type;
        private String timestamp;
        private String version;
        private String notify_url;
        private String biz_content;

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getCharset() {
            return charset;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        public void setNotify_url(String notify_url) {
            this.notify_url = notify_url;
        }

        public String getNotify_url() {
            return notify_url;
        }

        public void setBiz_content(String biz_content) {
            this.biz_content = biz_content;
        }

        public String getBiz_content() {
            return biz_content;
        }

    }

    public class Biz_content {

        private String body;
        private String subject;
        private String out_trade_no;
        private String total_amount;
        private String product_code;
        private String timeout_express;

        public String getTimeout_express() {
            return timeout_express;
        }

        public void setTimeout_express(String timeout_express) {
            this.timeout_express = timeout_express;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getBody() {
            return body;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setProduct_code(String product_code) {
            this.product_code = product_code;
        }

        public String getProduct_code() {
            return product_code;
        }

    }

    public class Data {

        private NeedToSignData needToSignData;
        private Biz_content biz_content;
        private String sign;
        private String urlencode_sign;
        private String sign_content;
        private String urlencode_sign_content;

        public void setNeedToSignData(NeedToSignData needToSignData) {
            this.needToSignData = needToSignData;
        }

        public NeedToSignData getNeedToSignData() {
            return needToSignData;
        }

        public void setBiz_content(Biz_content biz_content) {
            this.biz_content = biz_content;
        }

        public Biz_content getBiz_content() {
            return biz_content;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }

        public void setUrlencode_sign(String urlencode_sign) {
            this.urlencode_sign = urlencode_sign;
        }

        public String getUrlencode_sign() {
            return urlencode_sign;
        }

        public void setSign_content(String sign_content) {
            this.sign_content = sign_content;
        }

        public String getSign_content() {
            return sign_content;
        }

        public void setUrlencode_sign_content(String urlencode_sign_content) {
            this.urlencode_sign_content = urlencode_sign_content;
        }

        public String getUrlencode_sign_content() {
            return urlencode_sign_content;
        }

    }


}