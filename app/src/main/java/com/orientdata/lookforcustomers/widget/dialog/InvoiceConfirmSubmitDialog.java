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

public class InvoiceConfirmSubmitDialog extends Dialog {
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private TextView tv_invoice_info;
    private TextView tv_address_info;
    private TextView tvRemind;
    private String invoiceStr = "";
    private String addressStr = "";
    private TextView tvRemindTitle;
    private int isEOrP = 0;//1电子发票，2纸质发票

    public interface ClickListenerInterface {
        void doCancel();

        void doConfirm();
    }

    public InvoiceConfirmSubmitDialog(Context context, String invoiceStr, String addressStr, int isEOrP) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.invoiceStr = invoiceStr;
        this.addressStr = addressStr;
        this.isEOrP = isEOrP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.invoice_confirm_submit_dialog, null);
        setContentView(view);
        tv_invoice_info = view.findViewById(R.id.tv_invoice_info);
        tv_address_info = view.findViewById(R.id.tv_address_info);
        tvRemind = view.findViewById(R.id.tvRemind);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvConfirm = view.findViewById(R.id.tvConfirm);
        if (!TextUtils.isEmpty(invoiceStr)) {
            tv_invoice_info.setText(invoiceStr);
        }
        if (!TextUtils.isEmpty(addressStr)) {
            tv_address_info.setText(addressStr);
        }
        if (isEOrP == 2) {
            tvRemind.setVisibility(View.GONE);
        }
        if (isEOrP == 1) {
            tvRemind.setVisibility(View.VISIBLE);
        }
        tvCancel.setOnClickListener(new clickListener());
        tvConfirm.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.8
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
