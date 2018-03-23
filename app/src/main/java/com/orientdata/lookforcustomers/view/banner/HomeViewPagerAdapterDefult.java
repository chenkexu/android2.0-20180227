package com.orientdata.lookforcustomers.view.banner;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.orientdata.lookforcustomers.R;

/**
 * Project: dongfa
 * Version:  1.0
 * Date:    2017/3/28
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class HomeViewPagerAdapterDefult extends PagerAdapter {
    private FragmentActivity activity;
    public HomeViewPagerAdapterDefult(FragmentActivity mActivity) {
        this.activity = mActivity;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = View.inflate(activity,
                R.layout.viewpager_item_shibai, null);
        container.addView(rootView);

        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}


