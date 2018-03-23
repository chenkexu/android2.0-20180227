package com.orientdata.lookforcustomers.network.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class AppConfig {

	private static AppConfig appConfig = null;
	private final static String LOCAL_PRODUCT_PATH = "/okhttp/";
	private final static String LOCAL_PRODUCT_DOWNLOAD_VIDEOPATH = "/DCIM/UP_ZX/Video/";
	public final static String LOCAL_PRODUCT_DOWNLOAD_PHOTOPATH = "/DCIM/UP_ZX/Photo/";
	private static String path;
	//	private final static String LOCAL_PRODUCT_DOWNLOAD_PATH = "/okhttp/download";

//	private AndroidSystemInfo mSystemInfo;

	public static AppConfig sharedInstance() {
		if (appConfig == null) {
			appConfig = new AppConfig();
		}
		return appConfig;
	}

	public int versionCode = 0;
	public String versionName = "";
	public String systemVersion = "" + Build.VERSION.SDK_INT;

	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	private float mDensity = 0;

	public AppConfig() {
		MyApp delegate = MyApp.getApp();

		try {
			PackageInfo info = delegate.getPackageManager().getPackageInfo(
					delegate.getPackageName(), 0);
			versionName = info.versionName;
			versionCode = info.versionCode;

//			mSystemInfo = new AndroidSystemInfo();
//			mSystemInfo.setmSystemVersion(getAndroidVersion());
//			mSystemInfo.setmPlatform(getPlatform());
//			mSystemInfo.setmManufacturer(getManufacturer());
//			mSystemInfo.setmModel(getPhoneModel());
//			mSystemInfo.setmUuid(getUUID());

			WindowManager wm = (WindowManager)delegate.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics();
			display.getMetrics(outMetrics);

			mDensity = outMetrics.density;
			mScreenHeight = outMetrics.heightPixels;
			mScreenWidth  = outMetrics.widthPixels;
		} catch (Exception e) {
			Log.e("AppConfig", "get package info error");
		}
	}

	public String getDeviceId() {
		MyApp delegate = MyApp.getApp();

		final TelephonyManager tm = (TelephonyManager) delegate
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						delegate.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}

	public static String getDisplayString() {
		String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = MyApp.getApp().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		str += screenWidth + "x" + screenHeight;
		float density = dm.density;
		str += "-" + density * 160;
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;
		str += String.format("-%.0f*%.0f", xdpi, ydpi);
		return str;
	}

	public String getDeviceIdString() {
		MyApp delegate = MyApp.getApp();

		final TelephonyManager tm = (TelephonyManager) delegate
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
				delegate.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId;
	}
	/**
	 * 获取设备UUID
	 *
	 * @return
	 */
	public static String getUUID() {
		final TelephonyManager tm = (TelephonyManager) MyApp.getApp()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
				MyApp.getApp().getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();

		return uniqueId;
	}
	public static int[] getDisplayMetricsWH(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = MyApp.getApp().getResources().getDisplayMetrics();
		int width = dm.widthPixels;//宽度
		int height = dm.heightPixels ;//高度
		return new int[]{width,height};
	}


	public int getViewMeasuredHeight(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
		return view.getMeasuredHeight();
	}


	/**
	 * 获得当前应用的目录
	 * @return SDCard存在则返回当前应用的路径，否则返回null
	 */
	public static String getLocalProductPath() {
		Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (!isSDPresent) {
			Log.e("getLocalProductPath", "SDCard disappered!");
			return null;
		}

		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + LOCAL_PRODUCT_PATH;
		File dir = new File(path);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		// 屏蔽MediaScanning展示
		File file = new File(path + ".nomedia");
		if (!file.exists()) {
			try {
				FileWriter f = new FileWriter(file.getAbsolutePath());
				f.close();
			} catch (IOException e) {
				Log.e("getLocalProductPath", "IO Exception in file: " + path + ".nomedia");
			}
		}

		return path;
	}
	public static String getLocalProductDownloadPath(int type) {//下载文件
		Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (!isSDPresent) {
			Log.e("getLocalProductPath", "SDCard disappered!");
			return null;
		}
    if(type==3){
		  path = Environment.getExternalStorageDirectory().getAbsolutePath() + LOCAL_PRODUCT_DOWNLOAD_PHOTOPATH;
	}else if(type==4){
		 path = Environment.getExternalStorageDirectory().getAbsolutePath() + LOCAL_PRODUCT_DOWNLOAD_VIDEOPATH;
	}

		File dir = new File(path);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		// 屏蔽MediaScanning展示
		File file = new File(path + ".nomedia");
		if (!file.exists()) {
			try {
				FileWriter f = new FileWriter(file.getAbsolutePath());
				f.close();
			} catch (IOException e) {
				Log.e("getLocalProductPath", "IO Exception in file: " + path + ".nomedia");
			}
		}

		return path;
	}
	/**
	 * 获得屏幕高度
	 * @return
	 */
	public static int getScreenWidth() {
		return appConfig.mScreenWidth;
	}

	/**
	 * 获得屏幕宽度
	 * @return
	 */
	public static int getScreenHeight() {
		return appConfig.mScreenHeight;
	}

	/**
	 * 获得状态栏的高度
	 * @return
	 */
	public static int getStatusHeight() {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = MyApp.getApp().getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 *
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth();
		int height = getScreenHeight();
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 *
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth();
		int height = getScreenHeight();
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;

	}

	public static float getScreenDensity() {
		return appConfig.mDensity;
	}

	/**
	 * 获取系统版本号
	 */

	public static String getAndroidVersion(){
		return Build.VERSION.RELEASE;
	}
	/**
	 * 获取手机厂商
	 */
	public static String getPhoneModel(){
		return Build.MODEL;
	}
	/**
	 * 获取手机型号
	 */
	public static String getManufacturer(){
		return Build.MANUFACTURER;//BRAND
	}
	/**
	 * 获取系统名称
	 */
	public static String getPlatform(){

		return "Android";
	}

//	public AndroidSystemInfo getmSystemInfo() {
//		return mSystemInfo;
//	}

//	public class AndroidSystemInfo {
//		@SerializedName("platform")
//		private String mPlatform;
//		@SerializedName("version")
//		private String mSystemVersion;
//		@SerializedName("uuid")
//		private String mUuid;
//		@SerializedName("cordova")
//		private String mCordova;
//		@SerializedName("model")
//		private String mModel;
//		@SerializedName("manufacturer")
//		private String mManufacturer;
//
//		public String getmPlatform() {
//			return mPlatform;
//		}
//
//		public void setmPlatform(String mPlatform) {
//			this.mPlatform = mPlatform;
//		}
//
//		public String getmSystemVersion() {
//			return mSystemVersion;
//		}
//
//		public void setmSystemVersion(String mSystemVersion) {
//			this.mSystemVersion = mSystemVersion;
//		}
//
//		public String getmUuid() {
//			return mUuid;
//		}
//
//		public void setmUuid(String mUuid) {
//			this.mUuid = mUuid;
//		}
//
//		public String getmCordova() {
//			return mCordova;
//		}
//
//		public void setmCordova(String mCordova) {
//			this.mCordova = mCordova;
//		}
//
//		public String getmModel() {
//			return mModel;
//		}
//
//		public void setmModel(String mModel) {
//			this.mModel = mModel;
//		}
//
//		public String getmManufacturer() {
//			return mManufacturer;
//		}
//
//		public void setmManufacturer(String mManufacturer) {
//			this.mManufacturer = mManufacturer;
//		}
//	}
}
