package com.orientdata.lookforcustomers.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orientdata.lookforcustomers.R;

/**
 * activity 标题
 * Created by Phoenix on 2016/9/26.
 */
public class MyTitle extends RelativeLayout {
    private Context mContext;
    private View mView;//根View
    private ImageView mImageViewBack;//左边的ImageView
    private TextView mTextViewLeft;//左边的TextView
    private TextView mTextViewTitle;//Title
    private ViewGroup mViewLeft, mViewRight; //左右两边的View
    private OnClickListener backOnClickListener;
    private ImageView mImageViewRight;//右边的Image
    private TextView mTextViewRight;//右边的TextView
    private RelativeLayout layout_title;
    private View mViewUnderLine;//底部的线

    private TextView textViewLeftTwo;

    public MyTitle(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_mytitle, this, true);
        layout_title = (RelativeLayout) mView.findViewById(R.id.layout_title);
        mViewLeft = (ViewGroup) mView.findViewById(R.id.back_layout);
        mImageViewBack = (ImageView) mView.findViewById(R.id.back_btn);
        mTextViewLeft = (TextView) mView.findViewById(R.id.left_text);
        mTextViewTitle = (TextView) mView.findViewById(R.id.text_title);
        mImageViewRight = (ImageView) mView.findViewById(R.id.right_btn);
        mTextViewRight = (TextView) mView.findViewById(R.id.right_text);
        mViewRight = (ViewGroup) mView.findViewById(R.id.right);
        mViewUnderLine = mView.findViewById(R.id.view_under_line);
        textViewLeftTwo = (TextView) mView.findViewById(R.id.left_text_two);
    }

    /**
     * 设置底部线是否显示
     *
     * @param b true,显示；false，不显示
     */
    public void setUnderLineVisibility(boolean b) {
        if (b) {
            mViewUnderLine.setVisibility(VISIBLE);
        } else {
            mViewUnderLine.setVisibility(GONE);
        }
    }

    /**
     * 设置左侧图片或文字是否显示
     */
    public void setLeft(){

    }
    /**
     * 设置左侧按钮的图片，显示图片时，文字隐藏
     */
    public void setLeftImage(int resId, OnClickListener listener) {
        if (resId > 0) {
            mViewLeft.setVisibility(VISIBLE);
            mImageViewBack.setImageResource(resId);
            mImageViewBack.setVisibility(VISIBLE);
            mTextViewLeft.setVisibility(GONE);
        } else {
            mViewLeft.setVisibility(GONE);
        }
        if (listener != null) {
            mViewLeft.setOnClickListener(listener);
        }
    }






    /**
     * 设置左侧按钮的文字，显示文字时，图片隐藏
     */
    public void setLeftText(String text, OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            mViewLeft.setVisibility(VISIBLE);
            mTextViewLeft.setText(text);
            mImageViewBack.setVisibility(GONE);
            mTextViewLeft.setVisibility(VISIBLE);
        } else {
            mViewLeft.setVisibility(GONE);
        }
        if (listener != null) {
            mViewLeft.setOnClickListener(listener);
        }
    }

    public void setLeftText(@StringRes int resId, OnClickListener listener) {
        if (resId > 0) {
            mViewLeft.setVisibility(VISIBLE);
            mTextViewLeft.setText(resId);
            mImageViewBack.setVisibility(GONE);
            mTextViewLeft.setVisibility(VISIBLE);
        } else {
            mViewLeft.setVisibility(GONE);
        }
        if (listener != null) {
            mViewLeft.setOnClickListener(listener);
        }
    }





    /**
     * 设置点击左侧图片，关闭当前activity
     */
    public void setImageBack(final Activity activity) {
        if (activity != null) {
            setLeftImage(R.mipmap.ic_navigate_before, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    /**
     * 设置点击左侧文字，关闭当前activity
     */
    public void setTextBack(final Activity activity) {
        if (activity != null) {
            setLeftText(R.string.quit_activity, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    /**
     * 设置右侧按钮的图片，显示图片时，文字隐藏
     */
    public void setRightImage(int resId, OnClickListener listener) {
        if (resId > 0) {
            mViewRight.setVisibility(VISIBLE);
            mImageViewRight.setImageResource(resId);
            mImageViewRight.setVisibility(VISIBLE);
            mTextViewRight.setVisibility(GONE);
        } else {
            mViewRight.setVisibility(GONE);
        }
        if (listener != null) {
            mViewRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置左侧按钮的文字，显示文字时，图片隐藏
     */
    public void setRightText(String text, OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            mViewRight.setVisibility(VISIBLE);
            mTextViewRight.setText(text);
            mImageViewRight.setVisibility(GONE);
            mTextViewRight.setVisibility(VISIBLE);
        } else {
            mViewRight.setVisibility(GONE);
        }
        if (listener != null) {
            mViewRight.setOnClickListener(listener);
        }
    }

    public void setRightText(@StringRes int resId, OnClickListener listener) {
        if (resId > 0) {
            mViewRight.setVisibility(VISIBLE);
            mTextViewRight.setText(resId);
            mImageViewRight.setVisibility(GONE);
            mTextViewRight.setVisibility(VISIBLE);
        } else {
            mViewRight.setVisibility(GONE);
        }
        if (listener != null) {
            mViewRight.setOnClickListener(listener);
        }
    }

    public void setRightTextClickListener(OnClickListener listener) {
        if (listener != null) {
            mViewRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置标题文字
     */
    public void setTitleName(String name) {
        if (name != null) {
            mTextViewTitle.setVisibility(VISIBLE);
            mTextViewTitle.setText(name);
        }
    }

    /**
     * 设置是否显示标题
     */
    public void setTitleVisible(boolean visible) {
        if (visible) {
            mTextViewTitle.setVisibility(VISIBLE);
        } else {
            mTextViewTitle.setVisibility(GONE);
        }
    }

    /**
     * 设置左侧按钮是否显示
     */
    public void setLeftButtonVisible(boolean visible) {
        if (visible) {
            mViewLeft.setVisibility(VISIBLE);
        } else {
            mViewLeft.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧按钮是否显示
     */
    public void setRightButtonVisible(boolean visible) {
        if (visible) {
            mViewRight.setVisibility(View.VISIBLE);
        } else {
            mViewRight.setVisibility(View.GONE);
        }
    }

    /**
     * 修改右侧按钮文字
     */
    public void setRightText(String str) {
        if (!TextUtils.isEmpty(str)) {
            mViewRight.setVisibility(VISIBLE);
            mTextViewRight.setVisibility(VISIBLE);
            mImageViewRight.setVisibility(GONE);
            mTextViewRight.setText(str);
        } else {
            mViewRight.setVisibility(GONE);
        }
    }
    public void setLeftText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mViewLeft.setVisibility(VISIBLE);
            mTextViewLeft.setText(text);
            mImageViewBack.setVisibility(GONE);
            mTextViewLeft.setVisibility(VISIBLE);
        } else {
            mViewLeft.setVisibility(GONE);
        }
    }
    public void setRightText(@StringRes int resId) {
        if (resId > 0) {
            mViewRight.setVisibility(VISIBLE);
            mTextViewRight.setVisibility(VISIBLE);
            mImageViewRight.setVisibility(GONE);
            mTextViewRight.setText(resId);
        } else {
            mViewRight.setVisibility(GONE);
        }
    }

    /**
     * 设置右侧图案
     */
    public void setRightImage(@DrawableRes int resId) {
        if (resId > 0) {
            mViewRight.setVisibility(VISIBLE);
            mImageViewRight.setVisibility(VISIBLE);
            mTextViewRight.setVisibility(GONE);
            mImageViewRight.setImageResource(resId);
        } else {
            mViewRight.setVisibility(GONE);
        }
    }

    /**
     * 设置MyTitle背景颜色
     */
    public void setBackgroundColor(int color) {
        layout_title.setBackgroundColor(color);
    }

    /**
     * 获取左侧ImageView
     */
    public ImageView getImageViewLeft() {
        return mImageViewBack;
    }

    /**
     * 获取左侧的TextView
     */
    public TextView getTextViewLeft() {
        return mTextViewLeft;
    }

    /**
     * 获得右侧的ImageView
     */
    public ImageView getImageViewRight() {
        return mImageViewRight;
    }

    /**
     * 获取右侧的TextView
     */
    public TextView getTextViewRight() {
        return mTextViewRight;
    }


    public TextView getTextViewLeftTwo() {
        return textViewLeftTwo;
    }

    /**
     * 获取右侧的button
     */
    public View getRightView() {
        return mImageViewRight;
    }

    /**
     * 标题文字和设置文字颜色
     *
     * @param name
     */
    public void setTitleNameAndColor(String name, int redId) {
        if (name != null) {
            mTextViewTitle.setTextColor(redId);
            mTextViewTitle.setText(name);
        }
    }

    public void setRightTextColor(int colorId) {
        if (colorId != 0 && mTextViewRight != null) {
            mTextViewRight.setTextColor(colorId);
        }
    }

    public void setTextViewLeftTwoVisible(boolean visible) {
        if (visible) {
            textViewLeftTwo.setVisibility(View.VISIBLE);
        } else {
            textViewLeftTwo.setVisibility(View.GONE);
        }
    }

    public void setTextViewLeftTwoText(String text) {
        if (!TextUtils.isEmpty(text)) {
            textViewLeftTwo.setVisibility(VISIBLE);
            textViewLeftTwo.setText(text);
        } else {
            textViewLeftTwo.setVisibility(GONE);
        }
    }
}
