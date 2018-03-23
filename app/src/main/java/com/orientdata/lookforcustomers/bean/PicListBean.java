package com.orientdata.lookforcustomers.bean;

import java.util.List;

/**
 * Created by wy on 2017/12/2.
 * 图片列表
 */

public class PicListBean extends ErrBean{
    private List<UserPicStore> result;

    public List<UserPicStore> getResult() {
        return result;
    }

    public void setResult(List<UserPicStore> result) {
        this.result = result;
    }
}
