package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by wy on 2017/12/3.
 */

public class DrawableCenterTextView  extends AppCompatTextView {

    public DrawableCenterTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //在布局文件中设置TextView的四周图片，用getCompoundDrawables方法可以获取这4个位置的图片
        Drawable[]  drawables =  getCompoundDrawables();
        if(drawables !=  null){
            Drawable left = drawables[0];
            if(left != null){

                float textWidth = getPaint().measureText(getText().toString());
                int  padding = getCompoundDrawablePadding();
                int width = 0;
                width = left.getIntrinsicHeight();
                float bodyWidth = textWidth + width + padding;

                canvas.translate((getWidth() - bodyWidth)/2, 0);

            }

        }

        super.onDraw(canvas);
    }

}