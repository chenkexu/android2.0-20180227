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
 * dialog
 */

public class StringDialog extends Dialog {
    private Context context;
    private String mExplainStr;


    /**
     *
     * @param context
     */
    public StringDialog(Context context, String mExplainStr) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.mExplainStr = mExplainStr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.string_explain_dialog, null);
        setContentView(view);
        TextView tv_explain = view.findViewById(R.id.tv_explain);
        if(!TextUtils.isEmpty(mExplainStr)){
            tv_explain.setText(mExplainStr);
        }


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }
}
