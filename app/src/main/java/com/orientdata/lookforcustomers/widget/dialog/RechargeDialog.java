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
import android.widget.ImageView;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;

/**
 * Created by wy on 2017/11/21.
 * 提交结果提示dialog
 */

public class RechargeDialog extends Dialog {
    private String submitStatus;//提示语,标题
    private int imgResId;//图片
    private String confirmText;//按钮提示
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private TextView tvSubmitRemind;
    private String submitRemind = "";

    public interface ClickListenerInterface {
        void doCertificate();
    }

    /**
     *
     * @param context
     * @param submitStatus 提示语
     * @param imgResId 图片
     * @param confirmText  按钮文字
     */
    public RechargeDialog(Context context, String submitStatus, String confirmText, int imgResId) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.imgResId = imgResId;
        this.submitStatus = submitStatus;
        this.confirmText = confirmText;
    }
    public RechargeDialog(Context context, String submitStatus, String confirmText, int imgResId, String submitRemind) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.imgResId = imgResId;
        this.submitStatus = submitStatus;
        this.confirmText = confirmText;
        this.submitRemind = submitRemind;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recharge_dialog, null);
        setContentView(view);
        TextView tvSubmitStatus = view.findViewById(R.id.tvSubmitStatus);
        ImageView ivSubmitStatus = view.findViewById(R.id.ivSubmitStatus);
        TextView tvConfirm = view.findViewById(R.id.tvConfirm);
        tvSubmitRemind = view.findViewById(R.id.tvSubmitRemind);
        if(!TextUtils.isEmpty(submitRemind)){
            tvSubmitRemind.setText(submitRemind);
            tvSubmitRemind.setVisibility(View.VISIBLE);
        }else{
            tvSubmitRemind.setVisibility(View.GONE);
        }

        tvSubmitStatus.setText(submitStatus);
        ivSubmitStatus.setImageResource(imgResId);
        tvConfirm.setText(confirmText);
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
                    clickListenerInterface.doCertificate();
                    break;
            }
        }

    }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }
}
