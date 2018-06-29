package com.orientdata.lookforcustomers.bean;

/**
 * Created by ckx on 2018/6/26.
 */

public class TaskBasicInfo  {

        /**
         * smsPriceouter : 1
         * reservationCycle : 3
         * balance : 904415
         * signAndTd : {"id":4,"provincecode":"110000","signstate":1,"sign":"【北京】","tdcontent":"回复TD，退订"}
         */

        private int smsPriceouter;
        private String reservationCycle;
        private double balance;
        private SignAndTdBean signAndTd;

        public int getSmsPriceouter() {
            return smsPriceouter;
        }

        public void setSmsPriceouter(int smsPriceouter) {
            this.smsPriceouter = smsPriceouter;
        }

        public String getReservationCycle() {
            return reservationCycle;
        }

        public void setReservationCycle(String reservationCycle) {
            this.reservationCycle = reservationCycle;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public SignAndTdBean getSignAndTd() {
            return signAndTd;
        }

        public void setSignAndTd(SignAndTdBean signAndTd) {
            this.signAndTd = signAndTd;
        }

        public static class SignAndTdBean {
            /**
             * id : 4
             * provincecode : 110000
             * signstate : 1
             * sign : 【北京】
             * tdcontent : 回复TD，退订
             */

            private int id;
            private String provincecode;
            private int signstate;
            private String sign;
            private String tdcontent;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getProvincecode() {
                return provincecode;
            }

            public void setProvincecode(String provincecode) {
                this.provincecode = provincecode;
            }

            public int getSignstate() {
                return signstate;
            }

            public void setSignstate(int signstate) {
                this.signstate = signstate;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getTdcontent() {
                return tdcontent;
            }

            public void setTdcontent(String tdcontent) {
                this.tdcontent = tdcontent;
            }
        }
    }

