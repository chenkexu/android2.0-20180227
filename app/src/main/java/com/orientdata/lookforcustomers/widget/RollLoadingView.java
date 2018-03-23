package com.orientdata.lookforcustomers.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.orientdata.lookforcustomers.R;


/**
 * Created by wy on 2017/7/5.
 */

public class RollLoadingView extends View {

    private Paint paint;
    private int pointColor;
    private float pointRadius;
    private float pointSpace;
    private final int DEFAULT_POINT_COLOR = Color.GREEN;
    private final float DEFAULT_POINT_RADIUS = 50;
    private final float DEFAULT_POINT_SPACE = DEFAULT_POINT_RADIUS * 6;
    private int measureWith;
    private int measureHeight;
    private float leftRate;
    private float rightRate;
    private float rightRadiusRate;
    private ValueAnimator valueAnimator;


    public RollLoadingView(Context context) {
        this(context, null);
    }

    public RollLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RollLoadingView);
        pointColor = typedArray.getColor(R.styleable.RollLoadingView_point_color, DEFAULT_POINT_COLOR);
        pointRadius = typedArray.getDimension(R.styleable.RollLoadingView_point_radius, DEFAULT_POINT_RADIUS);
        pointSpace = typedArray.getDimension(R.styleable.RollLoadingView_point_space, DEFAULT_POINT_SPACE);
        typedArray.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWith = getMeasuredWidth();
        measureHeight = getMeasuredHeight();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(pointColor);
        paint.setStyle(Paint.Style.FILL);
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightRate = leftRate = (float) animation.getAnimatedValue();
                if (rightRate >= 0.5) {
                    rightRadiusRate = rightRate;
                } else {
                    rightRadiusRate = 1 - rightRate;
                }
                invalidate();
            }
        });
        valueAnimator.setDuration(700);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRightCircle(canvas);

        drawLeftCircle(canvas);
    }

    private void drawLeftCircle(Canvas canvas) {
        canvas.drawCircle(measureWith / 2 + pointSpace * (leftRate - 0.5f), measureHeight / 2, pointRadius, paint);
    }

    private void drawRightCircle(Canvas canvas) {
        canvas.drawCircle(measureWith / 2 + (0.5f - rightRate) * pointSpace, measureHeight / 2, pointRadius * rightRadiusRate, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        valueAnimator.removeAllUpdateListeners();
        valueAnimator = null;
        super.onDetachedFromWindow();
    }
}
