package com.orientdata.lookforcustomers.network.okhttp.manager;



import com.orientdata.lookforcustomers.network.okhttp.API;
import com.orientdata.lookforcustomers.network.okhttp.bean.RequestParam;

import java.io.File;

/**
 * 参数管理类,所有方法都是静态方法，不能获得该类的实例
 */
public class ParamManager {
    private static final String TAG = ParamManager.class.getSimpleName();

    private ParamManager() {

    }

    //————————————————————————————————————————————这里是分割线————————————————————————————————————————————

    public static RequestParam getNewsListParam(int id, int rows){
        RequestParam params = new RequestParam(API.TAG_NEWS_LIST,API.API_NEWS_LIST);
        params.put("id",id+"");
        params.put("rows", rows+"");
        return params;
    }

    public static RequestParam getPost(int id,int rows){
        RequestParam params = new RequestParam(API.POST,API.TAG_NEWS_LIST,API.API_NEWS_LIST);
        params.put("id",id+"");
        params.put("rows", rows+"");
        return params;
    }
    public static RequestParam upload(int id,File rows){
        RequestParam params = new RequestParam(API.UPLOAD,API.TAG_NEWS_LIST,"your url");
        params.put("id",id+"");
        params.put("rows", rows+"");
        return params;
    }

    public static RequestParam download(String downurl){
        RequestParam params = new RequestParam(API.DOWNLOAD,API.TAG_NEWS_LIST,downurl,false);
        return params;
    }
}
