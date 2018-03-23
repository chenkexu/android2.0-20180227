package com.orientdata.lookforcustomers.view.findcustomer;

import android.Manifest;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseActivity;
import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.bean.PicListBean;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.presenter.ImgPresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.ImageTools;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.impl.AddAdvertiseImgActivity;
import com.orientdata.lookforcustomers.widget.ColorControlView;
import com.orientdata.lookforcustomers.widget.ImageMakingView;
import com.orientdata.lookforcustomers.widget.MyTitle;
import com.orientdata.lookforcustomers.widget.toggleButton.zcw.togglebutton.ToggleButton;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by wy on 2017/11/25.
 * 图片制作
 */

public class ImageMakingActivity extends BaseActivity<IImgView, ImgPresent<IImgView>> implements IImgView, View.OnClickListener {
    private String url = "";
    private ImageMakingView imgMake;
    private MyTitle titleImg;
    private TextView tvColor1;
    private TextView tvColor2;
    private TextView tvColor3;
    private TextView tvColor4;
    private ColorControlView mColorControlView;
    private RelativeLayout word1, word2, word3;
    private EditText etWord1, etWord2, etWord3;
    private TextView word1Len, word2Len, word3Len;
    private TextView word1TextLen, word2TextLen, word3TextLen;
    private TextView tvWord1InImg, tvWord2InImg, tvWord3InImg;//图片中的
    private ScrollView scrollView;
    private int countW1 = 0;
    private int countW2 = 0;
    private int countW3 = 0;
    //tvInImage的x方向和y方向移动量
    private float mW1Dx, mW1Dy;
    private float mW2Dx, mW2Dy;
    private float mW3Dx, mW3Dy;
    private RelativeLayout rlContainer;
    //父组件的尺寸
    private float imageWidth, imageHeight, imagePositionX, imagePositionY;
    private ToggleButton toggleBold, toggleTilt;
    private TextView tvChange, tvImgAdd;
    private TextView tv1, tv2, tv3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_making);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
        initTitle();
    }

    @Override
    protected ImgPresent<IImgView> createPresent() {
        return new ImgPresent<>(this);
    }

    private void initTitle() {
        titleImg.setTitleName(getString(R.string.img_making));
        titleImg.setImageBack(this);
    }

    private void initView() {
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }
        imgMake = (ImageMakingView) findViewById(R.id.imgMake);
        Glide.with(this).load(url).into(imgMake);
        titleImg = (MyTitle) findViewById(R.id.titleImg);
        tvColor1 = (TextView) findViewById(R.id.tvColor1);
        tvColor2 = (TextView) findViewById(R.id.tvColor2);
        tvColor3 = (TextView) findViewById(R.id.tvColor3);
        tvColor4 = (TextView) findViewById(R.id.tvColor4);
        word1 = (RelativeLayout) findViewById(R.id.word1);
        word2 = (RelativeLayout) findViewById(R.id.word2);
        word3 = (RelativeLayout) findViewById(R.id.word3);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        etWord1 = word1.findViewById(R.id.etContent);
        etWord2 = word2.findViewById(R.id.etContent);
        etWord3 = word3.findViewById(R.id.etContent);
        etWord1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etWord2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        etWord3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        word1Len = word1.findViewById(R.id.tvContentLen);
        word2Len = word2.findViewById(R.id.tvContentLen);
        word3Len = word3.findViewById(R.id.tvContentLen);
        word1TextLen = word1.findViewById(R.id.textView7);
        word2TextLen = word2.findViewById(R.id.textView7);
        word3TextLen = word3.findViewById(R.id.textView7);
        word1TextLen.setText("/15");
        word2TextLen.setText("/4");
        word3TextLen.setText("/7");
        mColorControlView = (ColorControlView) findViewById(R.id.colorSeclec);
        tvWord1InImg = (TextView) findViewById(R.id.tvWord1InImg);
        tvWord2InImg = (TextView) findViewById(R.id.tvWord2InImg);
        tvWord3InImg = (TextView) findViewById(R.id.tvWord3InImg);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        toggleBold = (ToggleButton) findViewById(R.id.toggleBold);
        toggleTilt = (ToggleButton) findViewById(R.id.toggleTilt);
        tvChange = (TextView) findViewById(R.id.tvChange);
        tvImgAdd = (TextView) findViewById(R.id.tvImgAdd);
        rlContainer = (RelativeLayout) findViewById(R.id.rlContainer);
        setEtWatcher();
        addWord1TouchListener();
        addWord2TouchListener();
        addWord3TouchListener();
        setColorListener();
        toggleBold.setOnToggleChanged(onBoldToggleChanged);
        toggleTilt.setOnToggleChanged(onTiltToggleChanged);
        tvImgAdd.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tvColor1.setOnClickListener(this);
        tvColor2.setOnClickListener(this);
        tvColor3.setOnClickListener(this);
        etWord1.requestFocus();


        imgMake.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imgMake.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                imagePositionX = imgMake.getX();
                imagePositionY = imgMake.getY();
                imageWidth = imgMake.getWidth();
                imageHeight = imgMake.getHeight();
            }
        });
    }

    private void setColorListener() {
        mColorControlView.setSingeTouchListener(new ColorControlView.SingleTouchListener() {
            @Override
            public void onChangeColor(int color) {
                setTextSolidColor(tvColor4, color);
                setTextColor(color);
            }
        });
    }

    private void setTextSolidColor(TextView tv, int color) {
//        GradientDrawable background = (GradientDrawable) tv.getBackground();
//        background.setColor(getResources().getColor(color));
        tv.setBackgroundColor(color);
    }

    /**
     * 设置字体颜色 及 上面框的颜色
     *
     * @param color
     */
    private void setTextColor(int color) {
        if (etWord1.hasFocus()) {
            tvColor1.setBackgroundColor(color);
            tvWord1InImg.setTextColor(color);
        } else if (etWord2.hasFocus()) {
            tvColor2.setBackgroundColor(color);
            tvWord2InImg.setTextColor(color);
        } else if (etWord3.hasFocus()) {
            tvColor3.setBackgroundColor(color);
            tvWord3InImg.setTextColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChange:
                finish();
                break;
            case R.id.tvImgAdd:
                if (hasPermisson()) {
                    confirm(rlContainer);//确认添加
                } else {
                    requestPermission();
                }
                break;
            case R.id.tvColor1:
                etWord1.requestFocus();
                break;
            case R.id.tvColor2:
                etWord2.requestFocus();
                break;
            case R.id.tvColor3:
                etWord3.requestFocus();
                break;


        }
    }

    /**
     * text1及text1右边框选中状态
     */
    public void setText1Focus() {
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        Drawable drawable = tvColor1.getBackground();
        ColorDrawable dra = (ColorDrawable) drawable;
//        tvWord1InImg.setTextColor(dra.getColor());
        tvColor4.setBackgroundColor(dra.getColor());
    }

    /**
     * text2及text2右边框选中状态
     */
    public void setText2Focus() {
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.VISIBLE);
        tv3.setVisibility(View.GONE);
        Drawable drawable = tvColor2.getBackground();
        ColorDrawable dra = (ColorDrawable) drawable;
//        tvWord2InImg.setTextColor(dra.getColor());
        tvColor4.setBackgroundColor(dra.getColor());
    }

    /**
     * text3及text3右边框选中状态
     */
    public void setText3Focus() {
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.VISIBLE);
        Drawable drawable = tvColor3.getBackground();
        ColorDrawable dra = (ColorDrawable) drawable;
//        tvWord3InImg.setTextColor(dra.getColor());
        tvColor4.setBackgroundColor(dra.getColor());
    }

    private void setEtWatcher() {
        etWord1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setText1Focus();
                }
            }
        });
        etWord2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setText2Focus();
                }
            }
        });
        etWord3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setText3Focus();
                }
            }
        });
        etWord1.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {
            }

            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {

                temp = s;
                if (s.toString().equals("")) {
//                    tvWord1InImg.setVisibility(View.INVISIBLE);
                } else {
//                    tvWord1InImg.setVisibility(View.VISIBLE);
                    tvWord1InImg.setText(s);
                    Drawable drawable = tvColor1.getBackground();
                    ColorDrawable dra = (ColorDrawable) drawable;
                    tvWord1InImg.setTextColor(dra.getColor());
                }
            }

            @Override
            public void afterTextChanged(Editable s/*之后的文字内容*/) {
                word1Len.setText("" + temp.length());
            }
        });
        etWord2.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {

            }

            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {

                temp = s;
                if (s.toString().equals("")) {
//                    tvWord2InImg.setVisibility(View.INVISIBLE);
                } else {
//                    tvWord2InImg.setVisibility(View.VISIBLE);
                    tvWord2InImg.setText(s);
                    Drawable drawable = tvColor2.getBackground();
                    ColorDrawable dra = (ColorDrawable) drawable;
                    tvWord2InImg.setTextColor(dra.getColor());
                }
            }

            @Override
            public void afterTextChanged(Editable s/*之后的文字内容*/) {
                word2Len.setText("" + temp.length());
            }
        });
        etWord3.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s/*之前的文字内容*/, int start/*添加文字的位置(从0开始)*/, int count, int after/*添加的文字总数*/) {

            }

            @Override
            public void onTextChanged(CharSequence s/*之后的文字内容 */, int start/*添加文字的位置(从0开始)*/, int before/*之前的文字总数*/, int count) {

                temp = s;
                if (s.toString().equals("")) {
//                    tvWord3InImg.setVisibility(View.INVISIBLE);
                } else {
//                    tvWord3InImg.setVisibility(View.VISIBLE);
                    tvWord3InImg.setText(s);
                    Drawable drawable = tvColor3.getBackground();
                    ColorDrawable dra = (ColorDrawable) drawable;
                    tvWord3InImg.setTextColor(dra.getColor());
                }
            }

            @Override
            public void afterTextChanged(Editable s/*之后的文字内容*/) {
                word3Len.setText("" + temp.length());
            }
        });
    }

    @Override
    public void showLoading() {
        showDefaultLoading();
    }

    @Override
    public void hideLoading() {
        hideDefaultLoading();
    }

    @Override
    public void uploadPicSuc(UploadPicBean uploadPicBean) {
        if (uploadPicBean.getCode() == 0) {
            ToastUtils.showShort("上传至图库！");
            closeActivity(AddAdvertiseImgActivity.class, ImageMakingActivity.class);
            ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
            imgClipResultEvent.path = filePath;
            imgClipResultEvent.bitmap = bm;
            imgClipResultEvent.adImgid = uploadPicBean.getResult() + "";
            EventBus.getDefault().post(imgClipResultEvent);
            finish();
        }
    }


    //移动
    private class SimpleWord1GestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //向右移动时，distanceX为负；向左移动时，distanceX为正
            //向下移动时，distanceY为负；向上移动时，distanceY为正

            countW1++;
            mW1Dx -= distanceX;
            mW1Dy -= distanceY;

            //边界检查
            mW1Dx = calPosition(imagePositionX - tvWord1InImg.getX(), imagePositionX + imageWidth - (tvWord1InImg.getX() + tvWord1InImg.getWidth()), mW1Dx);
            mW1Dy = calPosition(imagePositionY - tvWord1InImg.getY(), imagePositionY + imageHeight - (tvWord1InImg.getY() + tvWord1InImg.getHeight()), mW1Dy);

            //控制刷新频率
            if (countW1 % 5 == 0) {
                tvWord1InImg.setX(tvWord1InImg.getX() + mW1Dx);
                tvWord1InImg.setY(tvWord1InImg.getY() + mW1Dy);
            }

            return true;
        }
    }

    //移动
    private class SimpleWord2GestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //向右移动时，distanceX为负；向左移动时，distanceX为正
            //向下移动时，distanceY为负；向上移动时，distanceY为正

            countW2++;
            mW2Dx -= distanceX;
            mW2Dy -= distanceY;

            //边界检查
            mW2Dx = calPosition(imagePositionX - tvWord2InImg.getX(), imagePositionX + imageWidth - (tvWord2InImg.getX() + tvWord2InImg.getWidth()), mW2Dx);
            mW2Dy = calPosition(imagePositionY - tvWord2InImg.getY(), imagePositionY + imageHeight - (tvWord2InImg.getY() + tvWord2InImg.getHeight()), mW2Dy);

            //控制刷新频率
            if (countW2 % 5 == 0) {
                tvWord2InImg.setX(tvWord2InImg.getX() + mW2Dx);
                tvWord2InImg.setY(tvWord2InImg.getY() + mW2Dy);
            }

            return true;
        }
    }

    //移动
    private class SimpleWord3GestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //向右移动时，distanceX为负；向左移动时，distanceX为正
            //向下移动时，distanceY为负；向上移动时，distanceY为正

            countW3++;
            mW3Dx -= distanceX;
            mW3Dy -= distanceY;

            //边界检查
            mW3Dx = calPosition(imagePositionX - tvWord3InImg.getX(), imagePositionX + imageWidth - (tvWord3InImg.getX() + tvWord3InImg.getWidth()), mW3Dx);
            mW3Dy = calPosition(imagePositionY - tvWord3InImg.getY(), imagePositionY + imageHeight - (tvWord3InImg.getY() + tvWord3InImg.getHeight()), mW3Dy);

            //控制刷新频率
            if (countW3 % 5 == 0) {
                tvWord3InImg.setX(tvWord3InImg.getX() + mW3Dx);
                tvWord3InImg.setY(tvWord3InImg.getY() + mW3Dy);
            }

            return true;
        }
    }

    //计算正确的显示位置（不能超出边界）
    private float calPosition(float min, float max, float current) {
        if (current < min) {
            return min;
        }

        if (current > max) {
            return max;
        }

        return current;
    }

    /**
     * text1 touchListener
     */
    private void addWord1TouchListener() {
        final GestureDetector gestureDetector1 = new GestureDetector(this, new SimpleWord1GestureListenerImpl());
        //移动
        tvWord1InImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                gestureDetector1.onTouchEvent(event);
                return true;
            }
        });
    }

    /**
     * text2 touchListener
     */
    private void addWord2TouchListener() {
        final GestureDetector gestureDetector2 = new GestureDetector(this, new SimpleWord2GestureListenerImpl());
        //移动
        tvWord2InImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                gestureDetector2.onTouchEvent(event);
                return true;
            }
        });
    }

    /**
     * text3 touchListener
     */
    private void addWord3TouchListener() {
        final GestureDetector gestureDetector3 = new GestureDetector(this, new SimpleWord3GestureListenerImpl());
        //移动
        tvWord3InImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                gestureDetector3.onTouchEvent(event);
                return true;
            }
        });
    }

    ToggleButton.OnToggleChanged onBoldToggleChanged = new ToggleButton.OnToggleChanged() {
        @Override
        public void onToggle(boolean on) {
            if (etWord1.hasFocus()) {
                setTextBold(tvWord1InImg, on);
            } else if (etWord2.hasFocus()) {
                setTextBold(tvWord2InImg, on);
            } else if (etWord3.hasFocus()) {
                setTextBold(tvWord3InImg, on);
            }

        }
    };

    private void setTextBold(TextView tv, boolean isBold) {
        setTextType(tv, isBold, toggleTilt.getToogle());
    }

    /**
     * 设置字体 粗体斜体
     *
     * @param tv
     * @param isBold
     * @param isItalic
     */
    private void setTextType(TextView tv, boolean isBold, boolean isItalic) {
        if (isBold && isItalic) {
            //粗斜体
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        } else if (!isBold && isItalic) {
            //斜体
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        } else if (isBold && !isItalic) {
            //粗体
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    private void setTextItalic(TextView tv, boolean isItalic) {
        setTextType(tv, toggleBold.getToogle(), isItalic);
    }

    ToggleButton.OnToggleChanged onTiltToggleChanged = new ToggleButton.OnToggleChanged() {
        @Override
        public void onToggle(boolean on) {
            if (etWord1.hasFocus()) {
                setTextItalic(tvWord1InImg, on);
            } else if (etWord2.hasFocus()) {
                setTextItalic(tvWord2InImg, on);
            } else if (etWord3.hasFocus()) {
                setTextItalic(tvWord3InImg, on);
            }
        }
    };
    String filePath = "";
    Bitmap bm = null;

    //确认，生成图片
    public void confirm(View view) {
        tvWord1InImg.setBackground(null);
        tvWord2InImg.setBackground(null);
        tvWord3InImg.setBackground(null);
        bm = loadBitmapFromView(view);
        filePath = Environment.getExternalStorageDirectory() + File.separator + "imageWithText.jpg";
       /*try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
//            Toast.makeText(this, "图片已保存至：SD卡根目录/imageWithText.jpg", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        //Bitmap bm = getLoacalBitmap(filePath);
        //setImageBitmap(bitmap1, path);
        long size = getBitmapsize(bm);
        if (size > 100 * 1024) {
//                filePaths.add(path);
            showDefaultLoading();
            //压缩保存图片
            final Bitmap tempBitmap = bm;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //TODO 压缩图片
//                        String dirPath = IMAGE_FILE_LOCATION;
//                        File dir = new File(dirPath);
//                        if (!dir.exists()) {
//                            dir.mkdirs();
//                        }
//                        String savePath1 = dirPath + System.currentTimeMillis() + ".png";
                        //ImageTools.savePhotoToSDCard(ImageTools.compassBitmap(tempBitmap, 4 * 1024), savePath1);
                        ImageTools.compassBitmap(tempBitmap, 100, filePath);
                    } finally {
                        hideDefaultLoading();
                    }
                }
            }).start();
        }


//        mPresent.uploadImg(3,filePath);
        closeActivity(AddAdvertiseImgActivity.class, ImageMakingActivity.class);
        ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
        imgClipResultEvent.path = filePath;
        imgClipResultEvent.bitmap = bm;
//        imgClipResultEvent.adImgid = uploadPicBean.getResult()+"";
        EventBus.getDefault().post(imgClipResultEvent);
        finish();

    }

 /*   *//**
     * @param url
     * @return
     *//*
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }*/

    public long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    //以图片形式获取View显示的内容（类似于截图）
    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission.CAMERA);
        boolean b4 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.CONTACTS);
        boolean b5 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.MICROPHONE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(this, Manifest.permission_group.SMS);
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission_group.CONTACTS,
                        Manifest.permission_group.MICROPHONE,
                        Manifest.permission_group.SMS},
                0);
    }

}
