package com.orientdata.lookforcustomers.bean;

/**
 * Created by wy on 2017/10/31.
 */

public class LoginResultBean {

    /**
     * err : {"code":0,"msg":"正常","eventId":"xxxx-xxxx-xxxx"}
     * token : asdadnjkahsduipdnwjknqi
     * user : {"userId":"1"}
     */

    private ErrBean err;
    private String token;
    private UserBean user;
    private boolean isNewUser;

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
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

    public static class UserBean {
        /**
         * userId : 1
         */

        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
