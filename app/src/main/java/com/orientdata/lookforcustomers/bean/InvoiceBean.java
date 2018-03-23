package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class InvoiceBean implements Serializable {
    private int taskId;
    //是  自增id
    private String taskNo;
    //    是  任务编号
    private String taskName;
    //是  任务名称
    private Date throwStartdate;
    //是  投放开始日期
    private Date throwEnddate;
    //    是  投放结束日期
    private int invoiceStatus;
    //是  开发票状态 1已开 2未开 3开票申请中
    private BigDecimal actualAmount;
//    是 bigdecimal 实际金额


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

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

    public Date getThrowStartdate() {
        return throwStartdate;
    }

    public void setThrowStartdate(Date throwStartdate) {
        this.throwStartdate = throwStartdate;
    }

    public Date getThrowEnddate() {
        return throwEnddate;
    }

    public void setThrowEnddate(Date throwEnddate) {
        this.throwEnddate = throwEnddate;
    }

    public int getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(int invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }
}
