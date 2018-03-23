package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
 * 提示认证的dialog
 */

public class RemindDialog extends Dialog {
    private String cerStatus;//认证状态
    private String remindString;//提示语
    private int imgResId;//图片
    private String btText;//按钮提示
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    TextView tvCerStatus;

    public interface ClickListenerInterface {
        void doCertificate();
    }

    /**
     *
     * @param context
     * @param cerStatus 认证状态：未认证 认证中 审核未通过
     * @param remindString 提示语
     * @param imgResId 图片
     * @param btText  按钮文字
     */
    public RemindDialog(Context context, String cerStatus, String remindString, int imgResId, String btText) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.cerStatus = cerStatus;
        this.remindString = remindString;
        this.imgResId = imgResId;
        this.btText = btText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.remind_dialog, null);
        setContentView(view);
        TextView tvCerRemind = view.findViewById(R.id.tvCerRemind);
        ImageView ivCerStatus = view.findViewById(R.id.ivCerStatus);
        tvCerStatus = view.findViewById(R.id.tvCerStatus);
        TextView tvToCert = view.findViewById(R.id.tvToCert);

        tvCerRemind.setText(remindString);
        ivCerStatus.setImageResource(imgResId);
        tvCerStatus.setText(cerStatus);
        tvToCert.setText(btText);
        tvToCert.setOnClickListener(new clickListener());

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
                case R.id.tvToCert:
                    clickListenerInterface.doCertificate();
                    break;
            }
        }

    }

    /**
     * 设置头是否显示
     * @param visibility
     */
    public void setTvCerStatusVisible(int visibility){
        tvCerStatus.setVisibility(visibility);
    }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }
}
