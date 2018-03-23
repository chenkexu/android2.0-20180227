package com.orientdata.lookforcustomers.event;

import android.graphics.Bitmap;

import com.orientdata.lookforcustomers.bean.TradeSelfout;

import java.util.List;

/**
 * 模版制作列表
 */

public class ImgClipResultEvent {
    //是否登录成功
    public String path;
    public Bitmap bitmap;
    public int templateId;
    public String adImgid;
    public List<TradeSelfout> modelList;
    public String library_url;
}
