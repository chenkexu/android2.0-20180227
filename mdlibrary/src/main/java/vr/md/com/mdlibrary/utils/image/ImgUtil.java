package vr.md.com.mdlibrary.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by fakecoder on 2015/7/12.
 */
public class ImgUtil {


    /**
     * @param fileName
     * @return 功能：读取图片文件
     */
    public static Bitmap readBitmap(String fileName) {

        File file = new File(fileName);

        if (file.exists()) {
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
                return bitmap;
            } catch (Throwable t) {
                Log.e("FileUtil", "Exception readBitmap", t);
                return null;
            }

        } else {
            return null;
        }
    }


//    /**
//     * 为网络图片在本地生成一个路径
//     *
//     * @param url
//     * @return
//     */
//    public static String createAnImageLocation(String url) {
//
//        String imageLocation = null;
//
//        String[] tempStrings = url.split("\\/");
//        tempStrings = tempStrings[tempStrings.length - 1].split("\\.");
//        String tempString = "";
//        tempString = tempStrings[0];
//        String picNameString = tempString;
//        // // 设置sd卡缓存路径
//        imageLocation = FileUtil.getAppSdcardDateFolder("cache/img") + picNameString;
//
//        return imageLocation;
//
//    }

    /**
     * @param bitmap
     * @param fileName 功能：创建图片文件
     */
    public static void saveBitmap(Bitmap bitmap, String fileName,
                                  Bitmap.CompressFormat format) {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        FileOutputStream out;

        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(format, 80, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * bitmap转换成drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        @SuppressWarnings("deprecation")
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }


    /**
     * drawable转换成bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }


    public static Bitmap getBitmapFromLocalFolderPic(String path) {

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public static Bitmap getBitmapFromByteArray(byte[] bytes) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;


    }

    public static Bitmap getBitmapFromInputStream(InputStream inputStream) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }


    public static OutputStream Bitmap2OutputStream(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos;

    }


    public static InputStream Bitmap2InputStrem(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        return inputStream;
    }


    public static byte[] Bitmap2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }


    public static Bitmap compressBitmapByQuality(Bitmap bitmap, int requireSizeKb) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length > requireSizeKb * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;

        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(inputStream, null, null);


        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }


    public static Bitmap compressBitmap(Bitmap imgae, int sizeKb_memory) {
        //获取inputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        imgae.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();


        InputStream inputStream = new ByteArrayInputStream(data);
        Log.e("data", data.toString());
        Log.e("inputStream", inputStream.toString());


        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置成true,不返回bitmap
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);


        int width = options.outWidth;
        int height = options.outHeight;

        int originalPixel = width * height;
        if ((originalPixel / 1024) > sizeKb_memory) {
            options.inSampleSize = (int) Math.sqrt(originalPixel / (1024 * sizeKb_memory));


        } else {
            options.inSampleSize = 1;
        }


        //设置成false,返回压缩后的bitmap
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;


        inputStream = new ByteArrayInputStream(data);

        Log.e("data", data.toString());
        Log.e("inputStream", inputStream.toString());

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        try {
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;

    }


    /**
     * @param data
     * @param requiredSize 需要的大小多少kb
     * @return
     */
    public static Bitmap compressBitmapByByte(byte[] data, int requiredSize) {

        Bitmap bitmap = null;

        // Decode image size
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = requiredSize;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;


        if (requiredSize * 1024 < width_tmp * height_tmp) {
            scale = (width_tmp * height_tmp) / (requiredSize * 1024);
            scale = (int) Math.sqrt(scale);

        }

        if (scale <= 0)
            scale = 1;

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        o2.inJustDecodeBounds = false;
        o2.inPreferredConfig = Bitmap.Config.RGB_565;
        Log.e("inSampleSize", scale + "");


        //inputStream 需要重新初始化，否则bitmap返回null

        inputStream = new ByteArrayInputStream(data);


        bitmap = BitmapFactory.decodeStream(inputStream, null, o2);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }


}
