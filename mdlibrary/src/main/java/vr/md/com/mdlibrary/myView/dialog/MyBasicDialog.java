package vr.md.com.mdlibrary.myView.dialog;

import android.app.Activity;
import android.app.Dialog;

import vr.md.com.mdlibrary.BaseActivity;
import vr.md.com.mdlibrary.R;

/**
 * Created by pi on 15-6-16.
 */
public class MyBasicDialog extends Dialog {

    protected BaseActivity baseActivity;

    public MyBasicDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.Dialog_Legend);
        this.baseActivity = baseActivity;
    }

    public MyBasicDialog(BaseActivity baseActivity, int theme) {
        super(baseActivity, theme);
        this.baseActivity = baseActivity;
    }

    //与baseActivity无关
    public MyBasicDialog(Activity activity) {
        super(activity, R.style.Dialog_Legend);
    }
}
