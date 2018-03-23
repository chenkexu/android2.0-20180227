package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 省市
 */
public class ProvinceBean extends ErrBean implements Serializable{
//"result": [
//
//    {
//        "name":"北京市",
//            "code":"110000",
//            "status":1,
//    }
    public List<Area> result;


    public List<Area> getResult() {
        return result;
    }

    public void setResult(List<Area> result) {
        this.result = result;
    }
}
