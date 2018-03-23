package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;

/**
 * 定向设置
 */
public class SelectSettingBean extends ErrBean implements Serializable{

    public SettingOut result;

    public SettingOut getResult() {
        return result;
    }

    public void setResult(SettingOut result) {
        this.result = result;
    }
}
