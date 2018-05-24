package com.orientdata.lookforcustomers.network.api;

/**
 * Created by huang on 2018/4/16.
 */

public class ApiResponse<T> {
    private static int SUCCESS_CODE = 0;//成功的code
    int state;
    String msgCode;
    String msgText;
    T data;


    public boolean isSuccess(){
        return getState()==SUCCESS_CODE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "state=" + state +
                ", msgCode='" + msgCode + '\'' +
                ", msgText='" + msgText + '\'' +
                ", data=" + data +
                '}';
    }
}
