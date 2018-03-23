package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * Created by wy on 2017/11/20.
 * 认证信息
 */

public class CertificateMsgBean extends ErrBean implements Serializable {
    private CertificationOut result;

    public CertificationOut getResult() {
        return result;
    }

    public void setResult(CertificationOut result) {
        this.result = result;
    }
}
