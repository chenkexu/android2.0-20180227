package com.orientdata.lookforcustomers.bean;

/**
 * Created by ckx on 2018/6/26.
 */

public class TaskCountBean  {
    private  int throwingTaskCount;
    private  int examineTaskCount;
    private  int waitThrowTaskCount;

    public int getThrowingTaskCount() {
        return throwingTaskCount;
    }

    public void setThrowingTaskCount(int throwingTaskCount) {
        this.throwingTaskCount = throwingTaskCount;
    }

    public int getExamineTaskCount() {
        return examineTaskCount;
    }

    public void setExamineTaskCount(int examineTaskCount) {
        this.examineTaskCount = examineTaskCount;
    }

    public int getWaitThrowTaskCount() {
        return waitThrowTaskCount;
    }

    public void setWaitThrowTaskCount(int waitThrowTaskCount) {
        this.waitThrowTaskCount = waitThrowTaskCount;
    }
}
