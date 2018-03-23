package vr.md.com.mdlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Mr.Z on 15/12/11.
 */
public class DisplayUtil {
    /**
     * 屏幕宽度
     */
    private static int DisplayWidthPixels = 0;
    /**
     * 屏幕高度
     */
    private static int DisplayheightPixels = 0;

    private static int dpi;

    /**
     * 获取屏幕参数
     *
     * @param context
     */
    private static void getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 宽度
        DisplayWidthPixels = dm.widthPixels;
        // 高度
        DisplayheightPixels = dm.heightPixels;
        dpi = dm.densityDpi;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidthPixels(Context context) {
        if (context == null) {
            return -1;
        }
        if (DisplayWidthPixels == 0) {
            getDisplayMetrics(context);
        }
        return DisplayWidthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDisplayheightPixels(Context context) {
        if (context == null) {
            return -1;
        }
        if (DisplayheightPixels == 0) {
            getDisplayMetrics(context);
        }
        return DisplayheightPixels;
    }

    /**
     * 将px值转换为dip或dp值
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static double pt2px(Context context, double ptValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return ptValue * scale * 1.0f / 72;
    }

    /**
     * 获得屏幕尺寸
     *
     * @param context
     * @return
     */
    public static Point getScreenSize(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float getDensity(Context context) {
        float density = (float) Math.sqrt(Math.pow(getDisplayWidthPixels(context), 2) + Math.pow(getDisplayheightPixels(context), 2));
        //屏幕尺寸
        float length = 1.0f;
        return density * 1.0f / length;
    }

    /**
     * 获得屏幕物理尺寸
     *
     * @param context
     * @return
     */
    public static int getPhysicalDensity(Context context) {
        float density = (float) Math.sqrt(Math.pow(getDisplayWidthPixels(context), 2) + Math.pow(getDisplayheightPixels(context), 2));
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        int screenDensity = (int) (density * 1.0f / screenInches);
        return screenDensity;
    }

    /**
     * 瀑布流书的宽度
     *
     * @return
     */
    public static int getBookWidth(Context context) {
        return Math.min(getDisplayWidthPixels(context), getDisplayheightPixels(context)) / 2-dip2px(context,16);
    }

    /**
     * 获取竖屏情况下手机的屏幕高度
     *
     * @return
     */
    public static int getPortraitScreenHeight(Context context) {
        return Math.max(getDisplayWidthPixels(context), getDisplayheightPixels(context));
    }

    /**
     * 获取竖屏情况下手机的屏幕宽度
     *
     * @return
     */
    public static int getPortraitScreenWidth(Context context) {
        return Math.min(getDisplayWidthPixels(context), getDisplayheightPixels(context));
    }

}