package com.orientdata.lookforcustomers.manager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class BaiduMapUtil {
	public static final double K = 156545.7031525d; // 常量K （40075700 / 256）

	public static void installBaiduMap(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("您尚未安装百度地图APP或地图版本过低，点击确认完成安装。");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getLatestBaiduMapApp(context);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
	public static void getLatestBaiduMapApp(Context context)
    {
        if(context == null)
        {
            return;
        } else
        {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse("http://mo.baidu.com/map/");
            intent.setData(uri);
            context.startActivity(intent);
            return;
        }
    }

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	@SuppressLint("SdCardPath")
	public static boolean isInstallByread() {
		return new File("/data/data/" + "com.baidu.BaiduMap").exists();
	}

	public static float getZoomlevelByPileDistance(double distance,
			int screenWidth) {
		return (float) (Math.log((K / (distance / screenWidth))) / Math.log(2));
	}
}
