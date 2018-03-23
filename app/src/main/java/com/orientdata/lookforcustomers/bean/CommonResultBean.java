package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy on 2017/10/31.
 */

public class CommonResultBean {

    /**
     * err : {"code":0,"msg":"正常","eventId":""}
     */

    private ErrBean err;

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public static class ErrBean {
        /**
         * code : 0
         * msg : 正常
         * eventId :
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
