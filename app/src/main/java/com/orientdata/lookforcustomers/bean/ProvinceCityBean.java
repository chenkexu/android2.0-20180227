package com.orientdata.lookforcustomers.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 省市
 */
public class ProvinceCityBean extends ErrBean implements Serializable{
//"result": [
//    {
//              "name": "北京市",
//            "code": "110000",
//            "status": 1,
//            "list": [
//        {
//            "name": "北京市",
//                "code": "110100",
//                "status": 0,
//                "areaId": "3522"
//        }
//            ],
//        "areaId": "1"
//    },
//    {
//        "name": "江苏省",
//            "code": "320000",
//            "status": 0,
//            "list": [
//        {
//            "name": "南京市",
//                "code": "320100",
//                "status": 0,
//                "areaId": "3595"
//        },
//            ],
//        "areaId": "10"
//    },
    public List<AreaOut> result;


    public List<AreaOut> getResult() {
        return result;
    }

    public void setResult(List<AreaOut> result) {
        this.result = result;
    }
}
