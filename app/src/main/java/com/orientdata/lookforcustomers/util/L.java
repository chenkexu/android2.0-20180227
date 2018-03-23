package com.orientdata.lookforcustomers.util;

import android.util.Log;

/**
 * @author caibing.zhang
 * @createdate 2012-9-17 下午4:01:04
 * @Description: 日志
 */
public class L {
	private static final String KEY = "huangrui";

	public static void i(Object message) {
		if (CoreConstants.IS_DEBUG) {
			Log.i(KEY, message.toString());
		}
	}

	public static void e(Object message) {
		if (CoreConstants.IS_DEBUG) {
			Log.e(KEY, message.toString());
		}
	}

	public static void d(Object message) {
		if (CoreConstants.IS_DEBUG) {
			Log.d(KEY, message.toString());
		}
	}

	public static void w(Object message) {
		if (CoreConstants.IS_DEBUG) {
			Log.w(KEY, message.toString());
		}
	}

	public static void w(Object message, Throwable tr) {
		if (CoreConstants.IS_DEBUG) {
			Log.w(KEY, message.toString(), tr);
		}
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, Object msg) {
		if (CoreConstants.IS_DEBUG)
			Log.i(tag, msg.toString());
	}

	public static void d(String tag, Object msg) {
		if (CoreConstants.IS_DEBUG)
			Log.i(tag, msg.toString());
	}

	public static void e(String tag, Object msg) {
		if (CoreConstants.IS_DEBUG)
			Log.i(tag, msg.toString());
	}

	public static void v(String tag, Object msg) {
		if (CoreConstants.IS_DEBUG)
			Log.i(tag, msg.toString());
	}
}
