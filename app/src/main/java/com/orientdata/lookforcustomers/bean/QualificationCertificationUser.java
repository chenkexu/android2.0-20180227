package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wy on 2017/11/20.
 */

public class QualificationCertificationUser implements Serializable {
    private int userId;	//是	int	用户id
    private String certificationImgid;	//是	String	图片路径
    private Date createDate;	//是	Date	创建日期

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCertificationImgid() {
        return certificationImgid;
    }

    public void setCertificationImgid(String certificationImgid) {
        this.certificationImgid = certificationImgid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
