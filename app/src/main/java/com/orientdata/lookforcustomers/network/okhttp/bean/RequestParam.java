package com.orientdata.lookforcustomers.network.okhttp.bean;



import com.orientdata.lookforcustomers.network.okhttp.API;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class RequestParam {
    private int tag ;
    private int method;
    private String url;
    protected Map<String, String> params;
    protected Map<String, File> files;
    protected Map<String, String> headers;


    public RequestParam(int tag, String url){
        this(API.GET, tag, url);
    }
    public RequestParam(int method, int tag, String url) {
       this(method, tag, url, true);
    }

    /**
     * @param method
     * @param url
     * @param withBaseURL 是否使用baseURL
     */
    public RequestParam(int method, int tag, String url, boolean withBaseURL) {
        init(method,tag,url,withBaseURL);
    }

    private void init(int method, int tag, String url, boolean withBaseURL){
        this.method = method;
        this.tag = tag;
        if (withBaseURL) {
            this.url = API.SERVER_URL + url;
        }else{
            this.url = url;
        }
        this.params = new HashMap<>();
        this.files = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void put(String key, Object value) {
        if (key != null && value != null) {
            if (value instanceof File){
                putFile(key,(File)value);
            }else {
                params.put(key, value+"");
            }
        }
    }

    public void putFile(String key, File file) {
        if (key != null && file != null) {
            files.put(key, file);
        }
    }

    public void putHeader(String key, String value) {
        if (key != null && value != null) {
            headers.put(key, value);
        }
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

}
