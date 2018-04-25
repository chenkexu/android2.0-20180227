package com.orientdata.lookforcustomers.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.ToastUtils;

/**
 * Created by wy on 2017/11/21.
 * 业务选择dialog
 */

public class TaskTypeDialog extends Dialog implements View.OnClickListener {
    private boolean isSmsOpen = true;
    private boolean isPageOpen = true;
    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private TextView tvUnitPrice, tvUnitPrice1;//短信业务 单价 页面业务 单价
    private String unitPrice, unitPrice1;
    RadioButton rbMsg;
    RadioButton rbPage;
    private RelativeLayout relaMsg, relaPage;




    public void setRioButtonStatus(String str){

        if (str.equals("短信任务")) {
            rbMsg.setChecked(true);
            rbPage.setChecked(false);
        }else if(str.equals("页面任务")){
            rbMsg.setChecked(false);
            rbPage.setChecked(true);
        }else{
            rbMsg.setChecked(false);
            rbPage.setChecked(false);
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relaMsg: //选择短信任务
            case R.id.rbMsg:
                if (!isSmsOpen) {
                    ToastUtils.showShort("短信页面未开通");
                    rbMsg.setChecked(false);
                    return;
                }
                rbMsg.setChecked(true);
                rbPage.setChecked(false);
                clickListenerInterface.doConfirm("短信任务");
                dismiss();
                break;
            case R.id.relaPage: //选择页面任务
            case R.id.rbPage:
                if (!isPageOpen) {
                    ToastUtils.showShort("页面任务未开通");
                    rbPage.setChecked(false);
                    return;
                }
                clickListenerInterface.doConfirm("页面任务");
                rbPage.setChecked(true);
                rbMsg.setChecked(false);
                dismiss();
                break;



           /* case R.id.rbMsg:
                rbMsg.setChecked(true);
                rbPage.setChecked(false);

                break;*/
           /* case R.id.rbPage:
                rbPage.setChecked(true);
                rbMsg.setChecked(false);

                break;*/
        }
    }


    public interface ClickListenerInterface {
        void doConfirm(String chooseType);//确定

//        void doCancel();
    }

    /**
     * @param context
     */
    public TaskTypeDialog(Context context, String unitPrice, String unitPrice1) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.unitPrice = unitPrice;
        this.unitPrice1 = unitPrice1;
    }

    /**
     * @param context
     */
    public TaskTypeDialog(Context context, String unitPrice, String unitPrice1, boolean isSmsOpen, boolean isPageOpen) {
        super(context, R.style.RemindDialog);
        this.context = context;
        this.unitPrice = unitPrice;
        this.unitPrice1 = unitPrice1;
        this.isSmsOpen = isSmsOpen;
        this.isPageOpen = isPageOpen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_type_dialog, null);
        setContentView(view);
        TextView tvUnitPrice = view.findViewById(R.id.tvUnitPrice);
        TextView tvUnitPrice1 = view.findViewById(R.id.tvUnitPrice1);
        rbMsg = view.findViewById(R.id.rbMsg);
        rbPage = view.findViewById(R.id.rbPage);
//        TextView tvCacel = view.findViewById(R.id.tvCacel);
//        TextView tvConfirm = view.findViewById(R.id.tvConfirm);
        relaMsg = view.findViewById(R.id.relaMsg);
        relaPage = view.findViewById(R.id.relaPage);

        tvUnitPrice.setText(unitPrice);
        tvUnitPrice1.setText(unitPrice1);
        relaMsg.setOnClickListener(this);
        relaPage.setOnClickListener(this);
        rbMsg.setOnClickListener(this);
        rbPage.setOnClickListener(this);


//        tvCacel.setOnClickListener(new clickListener());
//        tvConfirm.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
    }

   /* private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.tvCacel:
//                    clickListenerInterface.doCancel();
//                    break;
//                case tvConfirm:
//                    String chooseType = getChooseType();
//                    if (chooseType == null) {
//                        ToastUtils.showShort("请选择任务类型");
//                    } else {
//                        clickListenerInterface.doConfirm(chooseType);
//                    }
//                    break;
                case R.id.relaMsg:  //短信任务
                    clickListenerInterface.doConfirm("短信任务");
                    dismiss();
                    break;
                case R.id.relaPage: //页面任务
                    clickListenerInterface.doConfirm("页面任务");
                    dismiss();
                    break;
            }
        }

    }*/

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public String getChooseType() {
        if (rbPage.isChecked()) {
            return "页面任务";
        } else if (rbMsg.isChecked()) {
            return "短信任务";
        }
        return null;
    }
}
