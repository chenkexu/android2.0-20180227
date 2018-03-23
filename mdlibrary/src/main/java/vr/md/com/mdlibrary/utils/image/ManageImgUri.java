package vr.md.com.mdlibrary.utils.image;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import vr.md.com.mdlibrary.utils.MyUtils;

/**
 * Created by fakecoder on 2015/7/2.
 */
public class ManageImgUri {


    public static Bitmap getBitmapFromByteArray(byte[] bitmapBytes, Context context, boolean isSavedToLocal, String picPath) {

        Bitmap bitmap = null;


        // 压缩bitmap
        bitmap = ImgUtil.compressBitmapByByte(bitmapBytes, 500);


        if (isSavedToLocal) {
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            // 将压缩后的图片保存在本地
            if (picPath == null) {
                Calendar calendar = Calendar.getInstance();
                String s = calendar.getTimeInMillis() + "";
                String folderPath = MyUtils.getAppPath();
                String picFileAbsPath = folderPath + "/" + s;


                //保存至本地
                if (MyUtils.checkSDCard()) {
                    ImgUtil.saveBitmap(bitmap, picFileAbsPath, format);
                }
            } else {
//保存至本地
                if (MyUtils.checkSDCard()) {
                    ImgUtil.saveBitmap(bitmap, picPath, format);
                }
            }


        }


        return bitmap;

    }

    /**
     * 根据图片Uri获取图片的bitmap，对其进行压缩。最后将其保存在手机sd中，并将bitmap返回
     *
     * @param uri     图片的uri
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static Bitmap getBitmapFromUri(Uri uri, Context context, boolean isSavedToLocal, String picPath) {
        Bitmap bitmap = null;

        // 根据uri获取图片bitmap
        InputStream inputStream = null;
        try {
//            1.
//            inputStream = context.getContentResolver().openInputStream(uri);
//
//            byte[] bitmapBytes = new byte[0];
//            bitmapBytes = inputStream2Byte(inputStream);
//
//            inputStream.close();
//
//            Log.e("bitmaplength before", bitmapBytes.length + "");
//
//            // 压缩bitmap
//            bitmap = ImageUtil.compressBitmap(bitmapBytes, 500);
//
//            // 将压缩后的图片保存在本地
//            Calendar calendar = Calendar.getInstance();
//            String s = calendar.getTimeInMillis() + "";
//            String folderPath = MyUtils.getAppPath();
//            String picFileAbsPath = folderPath + "/" + s;
//            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;

//            2.
            inputStream = context.getContentResolver().openInputStream(uri);

            byte[] bitmapBytes = new byte[0];
            bitmapBytes = inputStream2Byte(inputStream);
            inputStream.close();

            // 压缩bitmap
            bitmap = ImgUtil.compressBitmapByByte(bitmapBytes, 500);


//            3.
//            inputStream = context.getContentResolver().openInputStream(uri);
//
//            bitmap = BitmapFactory.decodeStream(inputStream);
//
//            // 压缩bitmap
//            bitmap = ImgUtil.compressBitmap(bitmap, 100);

            if (isSavedToLocal) {
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                // 将压缩后的图片保存在本地
                if (picPath == null) {
                    Calendar calendar = Calendar.getInstance();
                    String s = calendar.getTimeInMillis() + "";
                    String folderPath = MyUtils.getAppPath();
                    String picFileAbsPath = folderPath + "/" + s;


                    //保存至本地
                    if (MyUtils.checkSDCard()) {
                        ImgUtil.saveBitmap(bitmap, picFileAbsPath, format);
                    }
                } else {
//保存至本地
                    if (MyUtils.checkSDCard()) {
                        ImgUtil.saveBitmap(bitmap, picPath, format);
                    }
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }


    /**
     * 默认将图片保存至本地sd卡
     *
     * @param uri
     * @param context
     * @param picPath
     * @return
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context context, String picPath) {
        Bitmap bitmap = null;

        // 根据uri获取图片bitmap
        InputStream inputStream = null;
        try {

            inputStream = context.getContentResolver().openInputStream(uri);

            byte[] bitmapBytes = new byte[0];
            bitmapBytes = inputStream2Byte(inputStream);
            inputStream.close();

            // 压缩bitmap
            bitmap = ImgUtil.compressBitmapByByte(bitmapBytes, 500);


            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            // 将压缩后的图片保存在本地

//保存至本地
            if (MyUtils.checkSDCard()) {
                ImgUtil.saveBitmap(bitmap, picPath, format);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }


    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] inputStream2Byte(InputStream in) throws IOException {


        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1)
            outStream.write(data, 0, count);

        data = null;
        outStream.flush();
        outStream.close();
        return outStream.toByteArray();
    }
}
