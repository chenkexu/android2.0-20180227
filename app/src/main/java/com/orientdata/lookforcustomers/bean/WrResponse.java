package com.orientdata.lookforcustomers.bean;


/**
 * Created by 陈柯旭 on 2018/4/4.
 */

public class WrResponse<T> extends ErrBean {


    /**
     * err : {"code":0,"msg":"正常","eventId":""}
     * result : {"name":"啦咯啦咯啦咯啦咯啦咯啦","userHead":"http://58.87.92.145:8084/2018/4/4/2/8ef8b3e7c8a64b689184c716b3d2136c_tempHeadPortrait.jpg","phone":"18401512374","commission":99996,"balance":9.99996666E8,"frozenAmount":3333,"moreMoney":"1","subCount":"100","moneyStatus":2,"upMoney":"100"}
     */

    private ErrBean err;
    private T result;


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }




}
