package com.orientdata.lookforcustomers.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lvr on 2017/1/16.
 */

public class ViewPagerAdatper extends PagerAdapter {
    private Context context;
    private OnImageClickListener mOnImageClickListener;

    public interface OnImageClickListener{
        void imageonClick();
    }
    public void setOnItemClickListener(OnImageClickListener onItemClickListener ){
        this.mOnImageClickListener=onItemClickListener;
    }



    private List<View> mViewList ;

    public ViewPagerAdatper(List<View> mViewList) {
        this.mViewList = mViewList;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        View view = mViewList.get(mViewList.size() - 1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnImageClickListener.imageonClick(); // 2
            }
        });

        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}
