package com.orientdata.lookforcustomers.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.io.File;

/**
 * @description
 * @author Joke
 * @email 113979462@qq.com
 * @create 2015年6月1日
 * @version 1.0.0
 */

public class NavigationUtil {
	public static boolean isInstalledBaidu() {
		return new File("/data/data/" + "com.baidu.BaiduMap").exists();
	}

	public static boolean isInstalledGaode() {
		return new File("/data/data/" + "com.autonavi.minimap").exists();
	}

	public static void startGaode(Context context, double latitude,
                                  double longitude) {
		Intent intent = new Intent("android.intent.action.VIEW",
				android.net.Uri
						.parse("androidamap://navi?sourceApplication=优易充&lat="
								+ latitude + "&lon=" + longitude + "&dev=0"));
		intent.setPackage("com.autonavi.minimap");
		context.startActivity(intent);
	}

	public static void startBaidu(Context context, double latitude1,
                                  double longitude1, double latitude2, double longitude2) {
		if (BaiduMapUtil.isInstallByread()) {
			LatLng pt1 = new LatLng(latitude1, longitude1);
			LatLng pt2 = new LatLng(latitude2, longitude2);
			NaviParaOption para = new NaviParaOption();
			para.startPoint(pt1);
			para.startName("从这里开始");
			para.endPoint(pt2);
			para.endName("到这里结束");
			try {
				BaiduMapNavigation.openBaiduMapNavi(para, context);
			} catch (Exception e) {
				e.printStackTrace();
				BaiduMapUtil.installBaiduMap(context);// 提示安装百度地图客户端;
			}
		} else {
			Log.e("GasStation", "没有安装百度地图客户端");
			BaiduMapUtil.installBaiduMap(context);// 提示安装百度地图客户端;
		}

	}

	public static LatLng baidu2Gaode(LatLng ll) {
		final double x_pi = Math.PI * 3000.0 / 180.0;

		double x = ll.longitude - 0.0065;
		double y = ll.latitude - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double lon = z * Math.cos(theta);
		double lat = z * Math.sin(theta);

		return new LatLng(lat, lon);
	}
}
