package vr.md.com.mdlibrary.okhttp.Response;

/**
 * 错误代码实体类
 * Created by Mr.Z on 16/3/17.
 */
public class ErrorModel {
    private int code;
    private String msg;
    private String eventid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", eventid='" + eventid + '\'' +
                '}';
    }
}