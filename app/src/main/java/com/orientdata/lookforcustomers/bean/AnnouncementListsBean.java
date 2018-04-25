package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy
 * on 2017-12-03.
 */

public class AnnouncementListsBean extends ErrBean {
    List<AnnouncementBean> result;

    public List<AnnouncementBean> getResult() {
        return result;
    }

    public void setResult(List<AnnouncementBean> result) {
        this.result = result;
    }
}
