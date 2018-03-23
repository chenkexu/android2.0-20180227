package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy on 2017/10/31.
 * 发短信
 */

public class SendSmsResultBean {

    /**
     * err : {"code":0,"msg":"正常","eventId":"xxxx-xxxx-xxxx"}
     * codeId : 1
     */

    private ErrBean err;
    private int codeId;

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public static class ErrBean {
        /**
         * code : 0
         * msg : 正常
         * eventId : xxxx-xxxx-xxxx
         */

        private int code;
        private String msg;
        private String eventId;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }
    }
}
