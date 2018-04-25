package com.orientdata.lookforcustomers.bean;

import java.util.Map;

/**
 * Created by wy
 * on 2017-12-03.
 * 个人信息
 */

public class UserInfoBean extends ErrBean {
    Map<Object, Object> result;

    public Map<Object, Object> getResult() {
        return result;
    }

    public void setResult(Map<Object, Object> result) {
        this.result = result;
    }

}
