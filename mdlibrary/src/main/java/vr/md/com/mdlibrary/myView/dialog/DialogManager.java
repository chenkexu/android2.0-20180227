package vr.md.com.mdlibrary.myView.dialog;

import android.app.Dialog;
import android.view.WindowManager;

/**
 * Created by pi on 15-6-16.
 */
public class DialogManager {

    private static volatile DialogManager dialogManager;

    private Dialog dialog;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (null == dialogManager) {
            synchronized (DialogManager.class) {
                if (null == dialogManager) {
                    dialogManager = new DialogManager();
                }
            }
        }
        return dialogManager;
    }

    /**
     * 隐藏当前大activity并且显示新大dialog
     *
     * @param myBasicDialog
     */
    public void showDialog(MyBasicDialog myBasicDialog) {
        if (null != dialog) {
            if (dialog.isShowing()) {
                hideDialog();
            }
            dialog = null;
        }
        dialog = myBasicDialog;
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏并销毁dialog
     */
    public void hideDialog() {
        if (null != dialog && dialog.isShowing()) {
//            dialog.hide();
//            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }
}
