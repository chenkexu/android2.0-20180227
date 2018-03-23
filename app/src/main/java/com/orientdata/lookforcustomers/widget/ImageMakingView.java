package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by wy on 2017/12/3.
 */

public class ImageMakingView extends AppCompatImageView{
    public ImageMakingView(Context context) {
        super(context);
    }

    public ImageMakingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageMakingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.onTouchEvent(event);
    }
}
