package com.orientdata.lookforcustomers.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.orientdata.lookforcustomers.R;


public class ColorControlView extends View {

    private Paint mPaint;
    private Paint mControlPaint;
    private int mSpace;
    private int mSelectColor = 0;
    private RectF mRect = new RectF(0, 0, 0,dpTopx(50));
    private RectF mRect1 = new RectF(0, 0, 0,dpTopx(23));
    private Rect mBitmapRect = null;
    private SingleTouchListener mListener;
    private int[] mColors = new int[]{0xFFDC0100, 0xFFF88D20, 0xFFF4ED22, 0xFF5CEA1D,
            0xFF05A822, 0xFF20EDF9, 0xFF22AEF4, 0xFF0058FD,
            0xFF141EE3, 0xFF6C22F8, 0xFFCA24F3, 0xFFFD019D,
            0xFFF7A6B0, 0xFFF6F6F6,0xFFAC6230,0xFF85430A,0xFF878686,0xFF414141,0xFF000000};
    //
    private int index = dpTopx(8);

    Bitmap mBitmap;
    public ColorControlView(Context context) {
        this(context, null);
    }

    public ColorControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mControlPaint = new Paint();
        mPaint = new Paint();
        mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.selector_color)).getBitmap();
        mBitmapRect = new Rect(0, 0, mBitmap.getWidth()*2, mBitmap.getHeight()*2);

    }

    public void setSingeTouchListener(SingleTouchListener mListener) {
        this.mListener = mListener;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mSpace = getWidth() / (mColors.length+1);
        mPaint.setStrokeWidth(dpTopx(25));
        for (int i = 0; i < mColors.length; i++) {
            mPaint.setColor(mColors[i]);
            canvas.drawLine(mSpace * i+index, getHeight()-dpTopx(25), mSpace * (i + 1)+index, getHeight()-dpTopx(25), mPaint);
        }

        if (mSelectColor < mColors.length) {
            mRect.left = mSpace * mSelectColor-mSpace/2+index;
            mRect1.left = mSpace * mSelectColor-mSpace/2+index+dpTopx(2);
            mRect.right = (mSpace * (mSelectColor == 0 ? 1 : mSelectColor + 1))+mSpace/2+index;
            mRect1.right = (mSpace * (mSelectColor == 0 ? 1 : mSelectColor + 1))+mSpace/2+index-dpTopx(2);
            mRect1.top = dpTopx(2);
            mControlPaint.setColor(mColors[mSelectColor]);
            if(isVisible){
                canvas.drawBitmap(mBitmap,null,mRect,null);
                canvas.drawRect(mRect1,mControlPaint);
            }
        }
    }

    private boolean isVisible = false;//是否显示
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = 0;
        int y = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
//                if (mListener != null && mSelectColor < mColors.length){
//                    mListener.onChangeColor(mColors[mSelectColor]);
//                }
                isVisible = false;
                postInvalidate();
                break;

            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                float location = event.getX() / mSpace;
                int selectColor = location < 1 ? 0 : (int) location;
                if (mSelectColor != selectColor) {
                    mSelectColor = selectColor;
                    isVisible = true;
                    postInvalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float moveLocation = event.getX() / mSpace;
                int selectMoveColor = moveLocation < 1 ? 0 : (int) moveLocation;
                if (selectMoveColor < mColors.length) {
                    mSelectColor = selectMoveColor;
                    if (mListener != null && mSelectColor < mColors.length){
                        mListener.onChangeColor(mColors[mSelectColor]);
                    }
                    isVisible = true;
                    postInvalidate();
                }
                break;

            default:
                break;
        }
        return true;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface SingleTouchListener {
        public void onChangeColor(int color);
    }

    private int dpTopx(float dp){
        return DensityUtils.dp2px(getContext(), dp);
    }
}
