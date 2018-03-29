package vr.md.com.mdlibrary.okhttp.requestMap;

import vr.md.com.mdlibrary.UserDataManeger;

/**
 * Created by Mr.Z on 16/3/17.
 */
public class MDUserIdRequestMap extends MDTokenRequestMap {
    public MDUserIdRequestMap() {
        super();
        String userId = UserDataManeger.getInstance().getUserId() + "";
        if (null != userId) {
            this.put("userId", userId);
        } else {
            this.put("userId", "");

        }
    }
}
