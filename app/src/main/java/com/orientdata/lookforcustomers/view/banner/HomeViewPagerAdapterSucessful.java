package com.orientdata.lookforcustomers.view.banner;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.view.agreement.MyWebViewActivity;

import java.util.List;

/**
 * Project: dongfa
 * Author:  郑康
 * Version:  1.0
 * Date:    2017/3/28
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class HomeViewPagerAdapterSucessful extends PagerAdapter {
    private FragmentActivity mActivity;
    private List<String> imagerUrls;
    private List<String> imagerClickUrls;
    private Handler handler;
    public HomeViewPagerAdapterSucessful(Handler handler, FragmentActivity mActivity, List<String> imagerUrls) {
        this.handler = handler;
        this.imagerUrls = imagerUrls;
        this.mActivity = mActivity;
    }

    public HomeViewPagerAdapterSucessful(FragmentActivity mActivity, List<String> imagerUrls, List<String> imagerClickUrls, Handler handler) {
        this.mActivity = mActivity;
        this.imagerUrls = imagerUrls;
        this.imagerClickUrls = imagerClickUrls;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return imagerUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View rootView = View.inflate(mActivity,
                R.layout.home_viewpager_sucessful, null);
        ImageView mIcon = (ImageView) rootView
                .findViewById(R.id.home_Viewpager_image);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,MyWebViewActivity.class);
                intent.putExtra("url",imagerClickUrls.get(position));
                intent.putExtra("title","");
                mActivity.startActivity(intent);
            }
        });


        Glide.with(mActivity.getApplicationContext())
                .load(imagerUrls.get(position)).into(mIcon);

        container.addView(rootView);

        // 设置view的触摸事件事件，实现按下viewpager停止自动滑动，抬起，viewpager重新进行自动滑动操作
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);// 取消handler发送延迟消息，如果是null，全部handler都会被取消发送消息
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(0, 3000);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        handler.sendEmptyMessageDelayed(0, 3000);
                        break;
                }

                return true;
            }
        });
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
