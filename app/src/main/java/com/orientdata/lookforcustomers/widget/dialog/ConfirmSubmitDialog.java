package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;

/**
 * Created by wy on 2017/11/21.
 * 确定提交dialog
 */

public class ConfirmSubmitDialog extends Dialog {
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private String remindString = "";
    private TextView tvRemindTitle;
    private String remindTitle = "";


    public interface ClickListenerInterface {
        void doCancel();
        void doConfirm();
    }

    /**
     *
     * @param context
     */
    public ConfirmSubmitDialog(Context context, String remindString) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.remindString = remindString;
    }
    public ConfirmSubmitDialog(Context context,String remindTitle, String remindString) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.remindTitle = remindTitle;
        this.remindString = remindString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.confirm_submit_dialog, null);
        setContentView(view);
        TextView tvRemind = view.findViewById(R.id.tvRemind);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvConfirm = view.findViewById(R.id.tvConfirm);
        TextView tvRemindTitle = view.findViewById(R.id.tvRemindTitle);
        if(!TextUtils.isEmpty(remindString)){
            tvRemind.setText(remindString);
        }
        if(!TextUtils.isEmpty(remindTitle)){
            tvRemindTitle.setText(remindTitle);
        }
        tvCancel.setOnClickListener(new clickListener());
        tvConfirm.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tvConfirm:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.tvCancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }
}
