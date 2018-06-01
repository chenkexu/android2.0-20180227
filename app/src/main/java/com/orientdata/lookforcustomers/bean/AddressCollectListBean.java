package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy on 2017/12/10.
 */

public class AddressCollectListBean extends ErrBean{

    private List<AddressSearchRecode> result;

    public List<AddressSearchRecode> getResult() {
        return result;
    }

    public void setResult(List<AddressSearchRecode> result) {
        this.result = result;
    }
}
