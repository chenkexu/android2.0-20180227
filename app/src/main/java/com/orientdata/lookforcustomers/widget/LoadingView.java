package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.orientdata.lookforcustomers.R;

/**
 * Created by wy on 2017/12/6.
 */
public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.loading_linear, this);
    }

}