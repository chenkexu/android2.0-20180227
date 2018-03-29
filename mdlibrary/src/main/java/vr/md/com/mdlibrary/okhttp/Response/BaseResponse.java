package vr.md.com.mdlibrary.okhttp.Response;


import java.io.Serializable;

/**
 * Created by Mr.Z on 16/3/29.
 */
public class BaseResponse implements Serializable {
    ErrorModel err;

    public ErrorModel getErrorModel() {
        return err;
    }

    public void setErrorModel(ErrorModel errorModel) {
        this.err = errorModel;
    }
}
