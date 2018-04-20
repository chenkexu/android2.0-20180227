package com.orientdata.lookforcustomers.view.certification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;

import java.util.List;


/**
 * @author wy
 * @date 2017/11/19
 * 自定义-认证图片上传view
 */
public class CustomizeCredentialUploadView extends LinearLayout {

    private TextView tv_customize_creadential_name;//资质文件。
    private TextView tv_customize_creadential_welcome;//请选择上传以下文件。
    private RadioGroup rg_customize_creadential_container;
    private ImageView iv_customize_creadential_upload;//上传按钮


    public CustomizeCredentialUploadView(Context context) {
        super(context);
        initView(context);
    }

    public CustomizeCredentialUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }


    public void setUploadOnClickListener(OnClickListener onClickListener) {
        iv_customize_creadential_upload.setOnClickListener(onClickListener);
    }

    public void initRadioGroup(Context context, List<String> lists) {

        if (lists != null && lists.size() > 0) {
            RadioButton rb = null;
            for (int i = 0; i < lists.size(); i++) {
                rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.customize_creadential_upload_rg_item, null);
                rb.setText(lists.get(i));
                rg_customize_creadential_container.addView(rb);
            }
        }
    }


    //初始化下面的圆圈按钮
    public RadioGroup initRadioGroup(Context context, String[] strs) {

        if (strs != null && strs.length > 0) {
            RadioButton rb = null;
            for (int i = 0; i < strs.length; i++) {
                rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.customize_creadential_upload_rg_item, null);
                rb.setText(strs[i]);
                if (i == 0 && strs.length == 1) {
                    rb.setChecked(true);
                }
                rg_customize_creadential_container.addView(rb);
            }
        }
        return rg_customize_creadential_container;
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customize_creadential_upload_view, this, true);
        setOrientation(VERTICAL);
        tv_customize_creadential_name = (TextView) findViewById(R.id.tv_customize_creadential_name);
        tv_customize_creadential_welcome = (TextView) findViewById(R.id.tv_customize_creadential_welcome);
        rg_customize_creadential_container = (RadioGroup) findViewById(R.id.rg_customize_creadential_container);
        iv_customize_creadential_upload = (ImageView) findViewById(R.id.iv_customize_creadential_upload);
    }

    public ImageView getIv_customize_creadential_upload() {
        return iv_customize_creadential_upload;
    }

    public TextView getTv_customize_creadential_name() {
        return tv_customize_creadential_name;
    }

    public TextView getTv_customize_creadential_welcome() {
        return tv_customize_creadential_welcome;
    }

    public RadioGroup getRg_customize_creadential_container() {
        return rg_customize_creadential_container;
    }
}
