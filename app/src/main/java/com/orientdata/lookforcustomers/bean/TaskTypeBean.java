package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class TaskTypeBean extends ErrBean {
    private Map<String, String> result;

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}
