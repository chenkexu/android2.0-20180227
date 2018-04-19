package com.orientdata.lookforcustomers.view.clip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.util.ImageTools;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClipActivity extends Activity {
    private ClipImageLayout mClipImageLayout;
    private String path;
    private ProgressDialog loadingDialog;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipimage);
        //这步必须要加
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        title = (TextView) findViewById(R.id.title123);
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("请稍候...");
        path = getIntent().getStringExtra("path");
        Log.i("YCS", "path:" + path);
//		if(TextUtils.isEmpty(path)||!(new File(path).exists())){
//			Toast.makeText(this, "图片加载失败1",Toast.LENGTH_SHORT).show();
//			return;
//		}
        Bitmap bitmap = ImageTools.convertToBitmap(path, 600, 600);
        if (readPictureDegree(path) == 90) {
            File file = toturn(bitmap);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        if (bitmap == null) {
            Toast.makeText(this, "图片加载失败2", Toast.LENGTH_SHORT).show();
            return;
        }
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setBitmap(bitmap);
        ((Button) findViewById(R.id.id_action_clip)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//				loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();
                        String path = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";//+System.currentTimeMillis()+ ".png";
                        File dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        path = path + System.currentTimeMillis() + ".png";
                        ImageTools.savePhotoToSDCard(bitmap, path);
                        //上传到图片库
                        ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
                        imgClipResultEvent.path = path;
                        EventBus.getDefault().post(imgClipResultEvent);


//						loadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("path", path);
                        Log.i("YCS", "pathend:" + path);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).start();
            }
        });

    }

//	@Override
//	protected ImgPresent<IImgView> createPresent() {
//		return new ImgPresent<>(this);
//	}

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param img
     * @return
     */
    public File toturn(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+90); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

/*    *//**
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

//	@Override
//	public void showLoading() {
//		showDefaultLoading();
//	}
//
//	@Override
//	public void hideLoading() {
//		hideDefaultLoading();
//	}

}
