package vr.md.com.mdlibrary.utils.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类
 *
 * @author fakecoder
 */
public class ImageUtil {

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * bitmap转换成drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
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

    /**
     * 从指定文件夹下获取图片
     *
     * @param picPathString
     * @return
     */
    public static Bitmap getFolderPic(String picPathString) {
        return BitmapFactory.decodeFile(picPathString);
    }

    /**
     * @param bitmap
     * @param requiredSize 需要的大小多少kb
     * @return
     */
    public static Bitmap compressBitmap(byte[] bitmap, int requiredSize) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new ByteArrayInputStream(bitmap), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = requiredSize;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        // while (true) {
        // if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
        // break;
        // width_tmp /= 2;
        // height_tmp /= 2;
        // scale *= 2;
        // }
//		if (width_tmp > height_tmp && width_tmp > REQUIRED_SIZE) {// 如果宽度大的话根据宽度固定大小缩放
//			scale = (int) (width_tmp / REQUIRED_SIZE);
//		} else if (width_tmp < height_tmp && height_tmp > REQUIRED_SIZE) {// 如果高度高的话根据宽度固定大小缩放
//			scale = (int) (height_tmp / REQUIRED_SIZE);
//		}
        if (requiredSize * 1024 < width_tmp * height_tmp) {
            scale = (width_tmp * height_tmp)/(requiredSize * 1024);
            scale = (int) Math.sqrt(scale);

        }

        if (scale <= 0)
            scale = 1;

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        o2.inPreferredConfig = Bitmap.Config.RGB_565;
        Log.e("inSampleSize", scale + "");
        return BitmapFactory.decodeStream(new ByteArrayInputStream(bitmap), null, o2);
    }

    /**
     * 质量压缩
     *
     * @param image
     * @param
     * @return
     */
    public static Bitmap compressBitmapByQuality(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 比例压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressBitmapByScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为800f
        float ww = 720f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return bitmap;
    }


    /**
     * @param image
     * @param size  多少kb
     * @return
     */
    public static byte[] compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        Logger.d("原来截图大小为：" + baos.toByteArray().length / 1024);
        while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        Logger.d("截图大小为：" + baos.toByteArray().length / 1024);
        return baos.toByteArray();
    }



    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }


    /**
     * @param bitmap
     * @param fileName 功能：创建图片文件
     */
    public static void saveBitmap(Bitmap bitmap, String fileName,
                                  CompressFormat format) {
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
}
