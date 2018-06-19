package vr.md.com.mdlibrary.okhttp.requestMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.Z on 16/3/17.
 */
public class MDBasicRequestMap extends HashMap {

    public MDBasicRequestMap(){
        super();
    }

    public MDBasicRequestMap(Map<String, String> map){
        super();
        this.putAll(map);
    }
}
