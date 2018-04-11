package com.orientdata.lookforcustomers.view.clip;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.orientdata.lookforcustomers.base.WangrunBaseActivity;
import com.orientdata.lookforcustomers.util.ImageTools;
import com.orientdata.lookforcustomers.util.SystemUtils;
import com.orientdata.lookforcustomers.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import vr.md.com.mdlibrary.myView.alertview.AlertView;
import vr.md.com.mdlibrary.myView.alertview.OnItemClickListener;


/**
 * Created by hlj on 3/7/17.
 * 图片选择的Activity
 */
public abstract class ImageActivity extends WangrunBaseActivity {
    public static final int PHOTOZOOM = 0; // 相册/拍照
    public static final int PHOTOTAKE = 1; // 相册/拍照
    public static final int IMAGE_COMPLETE = 2; // 结果
    public static final int CROPREQCODE = 3; // 截取
    protected String temppath;
    protected String photoSavePath;//保存路径
    protected String photoSaveName;//图pian名
    //<资质名称,资质路径>
    protected ArrayList<String> filePaths;
    protected File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(Environment.getExternalStorageDirectory(), "ClipPhoto/cache/");
        if (!file.exists())
            file.mkdirs();
        photoSavePath = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";
        if (savedInstanceState != null) {
            photoSaveName = savedInstanceState.getString("photo");
        } else {
            photoSaveName = System.currentTimeMillis() + ".png";
        }
        filePaths = new ArrayList<String>();
    }


    //在企业认证的Activity中点击上传图片按钮,弹框选择图片的上传方式:拍照,从相册选择和取消按钮的处理
    protected void tanKuang() {
        new AlertView(null, null, "取消", null, new String[]{"从相册选择"}, this, AlertView.Style.ActionSheet,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
//                            case 0:                                   //拍照
//                                photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";     //图片名
//                                Uri imageUri = null;
//                                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //创建打开相机的意图对象
//                                file = new File(photoSavePath, photoSaveName);       //将图片路径和图片名传到文件对象中
//                                imageUri = Uri.fromFile(file);                       //将文件转换为图片的uri
//                                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                                startActivityForResult(openCameraIntent, PHOTOTAKE);  //开启拍照意图,携带数据回调
//                                break;
                            case 0:                                 //从相册选择
                                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);   //创建打开相册的意图对象,选择照片
                                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//传入数据(图片uri,图片类型)
                                startActivityForResult(openAlbumIntent, PHOTOZOOM);   //开启
                                break;
                            case -1:                               //取消
                                break;
                        }
                    }
                }).setCancelable(true).show();
    }

    /**
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ToastUtils.showShort("该图片不存在或者为空文件");
            return null;
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM://相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String path;
                if (SystemUtils.isMIUI()) {
                    path = getPhotoForMiuiSystem(data);
                } else {
                    path = getPhotoForNormalSystem(data);
                }
                Intent intent3 = new Intent(this, ClipActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;
            case PHOTOTAKE://拍照
                if (resultCode != RESULT_OK) {
                    return;
                }
                path = photoSavePath + photoSaveName;
                Log.i("YCS", "pathstart:" + path);
                Intent intent2 = new Intent(this, ClipActivity.class);
                intent2.putExtra("path", path);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE://裁剪完
                if (resultCode != RESULT_OK) {
                    return;
                }
                temppath = data.getStringExtra("path");
                // TODO: 2017/3/26  file not found
                *//*
                 Log. temppath
                  temppath  -- >   File

                 *//*

                Bitmap bitmap = getLoacalBitmap(temppath);
                setImageBitmap(bitmap, temppath);
                filePaths.add(temppath);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM://相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String path;
                if (SystemUtils.isMIUI()) {
                    path = getPhotoForMiuiSystem(data);
                } else {
                    path = getPhotoForNormalSystem(data);
                }
//                Intent intent3 = new Intent(this, ClipActivity.class);
//                intent3.putExtra("path", path);
//                startActivityForResult(intent3, IMAGE_COMPLETE);
                Bitmap bitmap1 = getLoacalBitmap(path);
                if (bitmap1 == null) {
                    return;
                }
                setImageBitmap(bitmap1, path);
                long size = getBitmapsize(bitmap1);
                if (size > 5 * 1024 * 1024) {
//                filePaths.add(path);
                    showDefaultLoading();
                    //压缩保存图片
                    final Bitmap tempBitmap = bitmap1;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //TODO 压缩图片
                                String dirPath = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/";
                                File dir = new File(dirPath);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                String savePath1 = dirPath + System.currentTimeMillis() + ".png";
                                //ImageTools.savePhotoToSDCard(ImageTools.compassBitmap(tempBitmap, 4 * 1024), savePath1);
                                ImageTools.compassBitmap(tempBitmap, 5 * 1024, savePath1);
                                filePaths.add(savePath1);
                            } finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideDefaultLoading();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    filePaths.add(path);
                }


                break;
            case PHOTOTAKE://拍照
                if (resultCode != RESULT_OK) {
                    return;
                }
                path = photoSavePath + photoSaveName;
                Log.i("YCS", "pathstart:" + path);
//                Intent intent2 = new Intent(this, ClipActivity.class);
//                intent2.putExtra("path", path);
//                startActivityForResult(intent2, IMAGE_COMPLETE);
                Bitmap bitmap2 = getLoacalBitmap(path);
                if (bitmap2 == null) {
                    return;
                }
                setImageBitmap(bitmap2, path);
//                filePaths.add(path);
                //压缩保存图片
                long size1 = getBitmapsize(bitmap2);
                if (size1 > 5 * 1024 * 1024) {
                    showDefaultLoading();
                    final Bitmap tempBitmap2 = bitmap2;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //TODO 压缩图片
                                String savePath1 = Environment.getExternalStorageDirectory() + "/ClipPhoto/cache/" + System.currentTimeMillis() + ".png";
                                //ImageTools.savePhotoToSDCard(ImageTools.compassBitmap(tempBitmap2, 5 * 1024), savePath1);
                                ImageTools.compassBitmap(tempBitmap2, 5 * 1024, savePath1);
                                filePaths.add(savePath1);
                            } finally {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideDefaultLoading();
                                    }
                                });
                            }
                        }
                    }).start();
                }
                break;
            case IMAGE_COMPLETE://裁剪完
                /*if (resultCode != RESULT_OK) {
                    return;
                }
                temppath = data.getStringExtra("path");

                Bitmap bitmap = getLoacalBitmap(temppath);
                setImageBitmap(bitmap, temppath);
                filePaths.add(temppath);*/
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public long getBitmapsize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0L;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public abstract void setImageBitmap(Bitmap bitmap, String path);

    private String getPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            c.close();
        } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
            imagePath = localUri.getPath();
        }
        return imagePath;
    }


    /**
     * 其他系统的相册选择
     *
     * @param data
     */
    private String getPhotoForNormalSystem(Intent data) {
        String filePath = getRealPathFromURI(data.getData());
        return filePath;
    }

    /**
     * 解析Intent.getdata()得到的uri为String型的filePath
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("photo", photoSaveName);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        photoSaveName = savedInstanceState.getString("photo");
    }
}
