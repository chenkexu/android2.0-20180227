package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;

/**
 * Created by wy on 2017/10/27.
 */

public class CommonTitleBar extends RelativeLayout {

    private View parent;
    private ImageView ivBack;
    private TextView tvTitle;

    public CommonTitleBar(Context context) {
        this(context, null);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parent = LayoutInflater.from(context).inflate(R.layout.item_common_title, this, true);
        initView(parent);
    }

    private void initView(View parent) {
        ivBack = parent.findViewById(R.id.iv_back);
        tvTitle = parent.findViewById(R.id.tv_title);
    }

    public void setOnBack(OnClickListener onClickListener) {
        ivBack.setOnClickListener(onClickListener);
    }

    public void setTitle(String name) {
        tvTitle.setText(name);
    }


}
