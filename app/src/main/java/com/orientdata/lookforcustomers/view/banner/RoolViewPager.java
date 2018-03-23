package com.orientdata.lookforcustomers.view.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Project: dongfa
 * Author:  郑康
 * Version:  1.0
 * Date:    2017/3/28
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */

public class RoolViewPager extends ViewPager {
    private int downX;
    private int downY;

    public RoolViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public RoolViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    //事件分发的
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父控件不要拦截事件，true:不拦截，false:拦截
        //getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
        //1.需要判断是左右滑动还是上下滑动，因为只有左右才是viewpager手动滑动的操作
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //getParent().requestDisallowInterceptTouchEvent(false);
                //获取按下的x和y的坐标
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取移动的x和y的坐标
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                //判断是上下还是左右滑动
                if (Math.abs(moveX-downX) > Math.abs(moveY-downY)) {
                    //左右
                    //从右往左，如果是最后一个条目，父控件拦截事件，实现切换界面的操作，如果不是最后一个条目，切换下一张图片
                    //getAdapter() : 获取ViewPager设置的adapter
                    if (downX - moveX > 0 && getCurrentItem() == getAdapter().getCount()-1) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if(downX - moveX > 0 && getCurrentItem() < getAdapter().getCount()-1){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    //从左往右,如果是第一个条目，父控件拦截事件，打开侧拉菜单，如果不是第一个条目，切换到上一张图片
                    else if(downX - moveX < 0 && getCurrentItem() == 0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if(downX - moveX < 0 && getCurrentItem() > 0){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }else{
                    //上下
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
