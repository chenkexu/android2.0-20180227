package com.orientdata.lookforcustomers.network.okhttp;


import com.orientdata.lookforcustomers.network.okhttp.manager.NetManager;

/**
 * Created by Paul on 16/1/31.
 */
public class API {
    public static final int GET= NetManager.GET;
    public static final int POST=NetManager.POST;
    public static final int PUT=NetManager.PUT;
    public static final int DELETE=NetManager.DELETE;
    public static final int UPLOAD=NetManager.UPLOAD;
    public static final int DOWNLOAD=NetManager.DOWNLOAD;

    public static final String SERVER_URL ="http://www.tngou.net";

    public static final String API_NEWS_LIST = "/api/lore/news";
    public static final int TAG_NEWS_LIST=1000;
}
