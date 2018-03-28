package com.orientdata.lookforcustomers.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BasePermissionActivity;
import com.orientdata.lookforcustomers.view.adapter.DepthPageTransformer;
import com.orientdata.lookforcustomers.view.adapter.ViewPagerAdatper;
import com.orientdata.lookforcustomers.widget.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




/**
 * 用户引导界面
 */
public class SplashActivity extends BasePermissionActivity {

    @BindView(R.id.in_viewpager)
    ViewPager inViewpager;

    private List<View> mViewList;

    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;

    @BindView(R.id.view_red_point)
    View viewRedPoint;

    private int mPointWidth; // 两点间距


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initData();
        inViewpager.setAdapter(new ViewPagerAdatper(mViewList));
        viewRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    // 完成布局后会回调改方法, 改方法可能会被回调多次
                    @Override
                    public void onGlobalLayout() {
                        // 此方法只需要执行一次就可以: 把当前的监听事件从视图树中移除掉, 以后就不会在回调此事件了.
                        viewRedPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);

                        mPointWidth = llPointGroup.getChildAt(1).getLeft()
                                - llPointGroup.getChildAt(0).getLeft();

                        System.out.println("间距: " + mPointWidth);
                    }
                });

        inViewpager.setPageTransformer(true, new DepthPageTransformer());

    }


    @Override
    protected void permissionSuccess() {

    }



    @Override
    protected void permissionFail() {
//        T.showShort(getApplicationContext(),"您拒绝了权限，某些功能APP无法使用");
//        finish();
    }


//    private void skipActivity() {
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
//    }


    private void initData() {
        mViewList = new ArrayList<>();
        LayoutInflater lf = getLayoutInflater().from(SplashActivity.this);
        View view1 = lf.inflate(R.layout.we_indicator1, null);
        View view2 = lf.inflate(R.layout.we_indicator2, null);
        View view3 = lf.inflate(R.layout.we_indicator3, null);
        View view4 = lf.inflate(R.layout.we_indicator4, null);
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);

        for (int i = 0; i <3; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_guide_point_default);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 10));

            if (i != 0) {
                params.leftMargin = DensityUtils.dp2px(this, 10);
            }

            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }

        inViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin = (int) (mPointWidth * (positionOffset + position));
                // Log.d(TAG, "当前位置:" + position + ";偏移比例:" + positionOffset
                // + ";点偏移:" + leftMargin);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewRedPoint
                        .getLayoutParams();
                lp.leftMargin = leftMargin;
                viewRedPoint.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }


}
