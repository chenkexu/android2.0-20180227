package vr.md.com.mdlibrary.myView.dialog;
import android.app.Activity;

import vr.md.com.mdlibrary.BaseActivity;
import vr.md.com.mdlibrary.R;

/**
 * Created by pizhuang on 2015/7/3.
 */
public class WaitDialog extends MyBasicDialog {
    public WaitDialog(BaseActivity baseActivity) {
        super(baseActivity);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_wait);
    }

    public WaitDialog(Activity activity) {
        super(activity);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_wait);
    }
}
