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
 * 确定取消dialog
 */

public class ConfirmDialog2 extends Dialog {
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private String remindString;
    private String rightText;
    private String leftText;
    TextView tvConfirm;
    TextView tvCancel;


    public interface ClickListenerInterface {
        void doCancel();
        void doConfirm();
    }

    /**
     *
     * @param context
     */
    public ConfirmDialog2(Context context, String remindString) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.remindString = remindString;
    }

    //设置右边字体
    public ConfirmDialog2(Context context, String remindString, String rightText) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.remindString = remindString;
        this.rightText = rightText;
    }

    //设置左右字体
    public ConfirmDialog2(Context context, String remindString, String rightText, String leftText) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.remindString = remindString;
        this.rightText = rightText;
        this.leftText = leftText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cancel_confirm_dialog, null);
        setContentView(view);
        TextView tvRemind = view.findViewById(R.id.tvRemind);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        if (!TextUtils.isEmpty(leftText)){
            tvCancel.setText(leftText);
        }

        if(!TextUtils.isEmpty(rightText)){
            tvConfirm.setText(rightText);
        }
        tvRemind.setText(remindString);



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
    public void setConfirmVisibility(int visibility){
        tvConfirm.setVisibility(visibility);
    }

    public void setCancelVisibility(int visibility){
        tvCancel.setVisibility(visibility);
    }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }
}
