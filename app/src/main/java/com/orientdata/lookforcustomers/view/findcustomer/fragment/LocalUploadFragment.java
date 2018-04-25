package com.orientdata.lookforcustomers.view.findcustomer.fragment;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.base.BaseFragment;
import com.orientdata.lookforcustomers.bean.UploadPicBean;
import com.orientdata.lookforcustomers.event.ImgClipResultEvent;
import com.orientdata.lookforcustomers.presenter.ImgPresent;
import com.orientdata.lookforcustomers.runtimepermissions.PermissionsManager;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.util.ImageTools;
import com.orientdata.lookforcustomers.util.ToastUtils;
import com.orientdata.lookforcustomers.view.findcustomer.IImgView;
import com.orientdata.lookforcustomers.view.findcustomer.impl.AddAdvertiseImgActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wy on 2017/11/16.
 * 本地上传
 */

public class LocalUploadFragment extends BaseFragment implements IImgView, View.OnClickListener {
    private ImgPresent mImgPresent;

    private ImageView addImg;
    protected String temppath;
    //    private static final String IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory()+"/ClipPhoto/tempAd.jpg";//剪切完的图片所存地址
    public static final String IMAGE_FILE_LOCATION = "file:///sdcard/tempAd.jpg";//剪切完的图片所存地址
    public static final String IMAGE_FILE_LOCATION_Path = Environment.getExternalStorageDirectory() + "/tempAd.jpg";//剪切完的图片所存地址
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_upload, container, false);
        initView(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && (getActivity()) != null) {
            ((AddAdvertiseImgActivity) getActivity()).setCancelVisible(View.GONE);
        }
    }

    private void initView(View v) {
        mImgPresent = ((AddAdvertiseImgActivity) getActivity()).getPresent();
        addImg = v.findViewById(R.id.addImg);
        addImg.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addImg:
                choosePhoto();
                break;
        }
    }

    public static final int PHOTOZOOM = 0; // 相册/拍照

    //选择照片
    private void choosePhoto() {
        if (CommonUtils.haveSDCard()) {
            if (hasPermisson()) {
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);   //创建打开相册的意图对象,选择照片
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//传入数据(图片uri,图片类型)
                startActivityForResult(openAlbumIntent, PHOTOZOOM);   //开启
            } else {
                requestPermission();
            }
        } else {
            Toast.makeText(getActivity(), "没有SD卡!", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int CROP_SMALL_PICTURE = 103;
    Bitmap bitmap1 = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM://相册
                if (resultCode != RESULT_OK) {
                    return;
                }
                if (data == null) {
                    return;
                }
                cropImageUri(data.getData(), 720, 422, CROP_SMALL_PICTURE);
                break;

            case CROP_SMALL_PICTURE:
                //这里是调用图片库裁剪后的返回
                //可以参照裁剪方法，里面已经指定了uri，所以在这里，直接可以从里面取uri，然后获取bitmap，并且设置到imageview
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
////                    addImg.setImageBitmap(bitmap1);
                    compressImg(bitmap1);
                    //上传图片
//                    if (bitmap1 != null) {
//                        File file = new File(IMAGE_FILE_LOCATION_Path);
//                        if (file.exists()) {
//                            mImgPresent.uploadImg(3, IMAGE_FILE_LOCATION_Path, false);
//                        }
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void compressImg(Bitmap bitmap1) {
//        Bitmap bitmap1 = getLoacalBitmap(IMAGE_FILE_LOCATION);
//        long size = getBitmapsize(bitmap1);
//        if (size > 5 * 1024 * 1024) {
//            showDefaultLoading();
//            //压缩保存图片
//            final Bitmap tempBitmap = bitmap1;
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //TODO 压缩图片
//                    ImageTools.savePhotoToSDCard(ImageTools.compassBitmap(tempBitmap, 5 * 1024), IMAGE_FILE_LOCATION);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideDefaultLoading();
//                        }
//                    });
//                }
//            }).start();
//        }

        Bitmap bm = getLoacalBitmap(IMAGE_FILE_LOCATION_Path);
        if (bm == null){
            return;
        }
        //setImageBitmap(bitmap1, path);
        long size = getBitmapsize(bm);
        if (size > 100 * 1024) {
//                filePaths.add(path);
//            showDefaultLoading();
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
                        ImageTools.compassBitmap(tempBitmap, 100, IMAGE_FILE_LOCATION_Path);
                    } finally {
                    }
                }
            }).start();
        }

        ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
        imgClipResultEvent.path = IMAGE_FILE_LOCATION_Path;
        imgClipResultEvent.bitmap = bitmap1;
        EventBus.getDefault().post(imgClipResultEvent);
        getActivity().finish();
        //((AddAdvertiseImgActivity) getActivity()).setCurrentItem(2);
    }

    public long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
//        File file = new File(IMAGE_FILE_LOCATION);
//        if(file.exists()){
//            file.delete();
//        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        intent.putExtra("aspectX", 72);
        intent.putExtra("aspectY", 42);
        //设置输出的宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //是否缩放
        intent.putExtra("scale", false);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, requestCode);
    }

    /**
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ToastUtils.showShort("该照片不存在，或为空文件");
            return null;
        }
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
        Cursor cursor = ((Activity) getContext()).managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String getPhotoForMiuiSystem(Intent data) {
        Uri localUri = data.getData();
        String scheme = localUri.getScheme();
        String imagePath = "";
        if ("content".equals(scheme)) {
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContext().getContentResolver().query(localUri, filePathColumns, null, null, null);
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
     * 是否有拍照的权限
     */
    @TargetApi(23)
    public boolean hasPermisson() {
        boolean b1 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b2 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean b3 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission.CAMERA);
        boolean b4 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission_group.CONTACTS);
        boolean b5 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission_group.MICROPHONE);
        boolean b6 = PermissionsManager.getInstance().hasPermission(getActivity(), Manifest.permission_group.SMS);
        return b1 && b2 && b3 && b4 && b5 && b6;
    }

    /**
     * 请求拍照的权限
     */
    @TargetApi(23)
    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission_group.CONTACTS,
                        Manifest.permission_group.MICROPHONE,
                        Manifest.permission_group.SMS},
                0);
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
        if (bitmap1 != null && uploadPicBean.getCode() == 0) {
            ToastUtils.showShort("上传成功！");
            //TODO
            ImgClipResultEvent imgClipResultEvent = new ImgClipResultEvent();
            imgClipResultEvent.path = IMAGE_FILE_LOCATION;
            imgClipResultEvent.bitmap = bitmap1;
            imgClipResultEvent.adImgid = uploadPicBean.getResult() + "";
            EventBus.getDefault().post(imgClipResultEvent);
            getActivity().finish();
            //上传完成后显示列表文件。
            //((AddAdvertiseImgActivity) getActivity()).setCurrentItem(2);
        }
    }
}
