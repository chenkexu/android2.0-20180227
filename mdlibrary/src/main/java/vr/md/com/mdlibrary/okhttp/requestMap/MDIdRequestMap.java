package vr.md.com.mdlibrary.okhttp.requestMap;

/**
 * Created by Mr.Z on 16/3/17.
 */
public class MDIdRequestMap extends MDTokenRequestMap {
    public MDIdRequestMap(){
        super();
        //TODO get lawyerId
        String lawyerId = "1001";
        if (null != lawyerId){
            this.put("lawyerId", lawyerId);
        }
    }
}
