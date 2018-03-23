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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.SharedPreferencesTool;
import com.orientdata.lookforcustomers.view.login.imple.LoginAndRegisterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 三张图片,欢迎体验
 */
public class LaunchActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3, R.mipmap.guide_4};

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        initView();
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_vp_viewpager);
        mStart = (Button) findViewById(R.id.guide_btn_start);


        imageViews = new ArrayList<ImageView>();
        imageViews.clear();


        for (int i = 0; i < mImageIds.length; i++) {
            //创建相应的imageView
            createImageView(i);
        }
        mViewPager.setAdapter(new Myadapter());


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
}
