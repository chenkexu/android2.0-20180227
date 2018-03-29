package vr.md.com.mdlibrary.okhttp.requestMap;

import vr.md.com.mdlibrary.UserDataManeger;

/**
 * Created by Mr.Z on 16/3/17.
 */
public class MDTokenRequestMap extends MDBasicRequestMap {
    public MDTokenRequestMap(){
        super();
        //TODO get token
        String token = UserDataManeger.getInstance().getUserToken();
        if (null != token) {
            this.put("token", token);
        }else{
            this.put("token", "");

        }
    }
}
