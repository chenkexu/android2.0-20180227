package com.orientdata.lookforcustomers.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.view.adapter.DepthPageTransformer;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;
import com.orientdata.lookforcustomers.widget.DensityUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 三张图片,欢迎体验
 */
public class LaunchActivity extends AppCompatActivity {

    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.view_red_point)
    View viewRedPoint;



    private ViewPager mViewPager;

    private int[] mImageIds = new int[]{R.mipmap.group_1, R.mipmap.group_2, R.mipmap.group_3, R.mipmap.splash};

    private List<ImageView> imageViews;

    private Button mStart;

    private LinearLayout mLLDot;

    private ImageView mRedDot;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private int mPointWidth; // 两点间距

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_vp_viewpager);
        mStart = (Button) findViewById(R.id.guide_btn_start);
        imageViews = new ArrayList<>();
        imageViews.clear();
        for (int i = 0; i < mImageIds.length; i++) {
            //创建相应的imageView
            createImageView(i);
        }

        for (int i = 0; i <3; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_guide_point_default);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(this, 7), DensityUtils.dp2px(this, 7));

            if (i != 0) {
                params.leftMargin = DensityUtils.dp2px(this, 10);
            }

            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }
        mViewPager.setAdapter(new Myadapter());

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

        mViewPager.setPageTransformer(true, new DepthPageTransformer());



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        //实现viewpager的界面切换动画

        //mViewPager.setPageTransformer(true, new RotatePageTransformer());

        //viewpager的界面切换监听

      /*  mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == imageViews.size() - 1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferencesTool.saveBoolean(LaunchActivity.this, Constants.ISFIRSTENTER, false);
                            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, 200);
                } else {
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/

    }

    /**
     * 创建图片对应的imageView
     * i:表示创建第几个图片对应的imageView
     */
    private void createImageView(int i) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(mImageIds[i]);
        if (i == mImageIds.length - 1) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesTool.getInstance().putBoolean(SharedPreferencesTool.ISFIRSTENTER, false);
                    startActivity(new Intent(LaunchActivity.this, LoginAndRegisterActivity.class));
                    finish();
                }
            });
        }
        imageViews.add(imageView);
    }


    /**
     * ViewPager的adapter
     **/
    private class Myadapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
//                if (position == imageViews.size()-1) {
//                    imageView.setClickable(true);
//                    imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            SharedPreferencesTool.saveBoolean(LaunchActivity.this, Constants.ISFIRSTENTER, false);
//                            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
//                            finish();
//                        }
//                    });
//                }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 开始体验按钮的点击事件
     * <p>
     * 2016年12月10日   下午5:10:11
     */
    public void start(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
