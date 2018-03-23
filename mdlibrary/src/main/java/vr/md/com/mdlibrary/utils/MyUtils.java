/**
 * 工具包，提供一些常用的方法
 * 1:判断字符串是否为空
 * 2:判断一个字符串是否为中文
 * 3:判断两个字符串是否相等
 * 4:list 合并
 * 5:数组翻转
 * 6：随机生成字符串有数字，字母组成
 * 7:根据dip返回当前设备上的px值
 * 8:用序列化与反序列化实现深克隆
 * 9:日期比较
 * 10:设置粗体
 * 11:软键盘开关，当软键盘开启的时候调用这个方法关闭，当软键盘没有弹出的时候调用这个方法弹出
 * 12:显示软件盘
 * 13:隐藏软件盘
 * 14:判断网络
 * 15：返回当前网络类型
 * 16:判断字符串是不是数字
 * 17:合集,并集
 * 18:交集
 * 19:方法一：循环元素删除
 * 20:方法二：通过HashSet剔除
 * 21:缩放图片
 * 22:bitmap转换成drawable
 * 23:获得Device Id
 * 24:触发移动事件的最短距离，如果小于这个距离就不触发移动控件
 * 25:判断sd卡是否插入
 * 26:返回一定格式的当前时间
 * 27:调用发短信界面
 * 28:调用打电话界面
 * 29:是否插入sim卡
 */
package vr.md.com.mdlibrary.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import vr.md.com.mdlibrary.AppConfig;
import vr.md.com.mdlibrary.BaseActivity;

public class MyUtils {
	/**
	 * 1:判断字符串是否为空 方法描述 :判断一个字符串是否为空，空位true 不空false 创建者：pizhuang 创建时间：
	 * 2014年5月26日 下午4:10:16
	 * 
	 * @param str
	 * @return boolean
	 *
	 */
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.replace(" ", ""))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 2:判断一个字符串是否为中文 方法描述 : 创建者：pizhuang 创建时间： 2014年7月29日 下午5:12:44
	 * 
	 * @param str
	 * @return boolean
	 *
	 */
	public static boolean isChiness(String str) {
		if (str.matches("[\u4e00-\u9fa5]+")) {
			return true;
		}
		return false;
	}

	/**
	 * 3:判断两个字符串是否相等 方法描述 : 判断两个字符串是否等 创建者：pizhuang 创建时间： 2014年8月8日 上午10:25:40
	 * 
	 * @param str1
	 * @param str2
	 * @return boolean
	 *
	 */
	public static boolean strIsEqual(String str1, String str2) {
		if (isEmpty(str1) && isEmpty(str2)) {
			return true;
		}
		if (!isEmpty(str1) && isEmpty(str2)) {
			return false;
		}
		if (isEmpty(str1) && !isEmpty(str2)) {
			return false;
		}
		if (!isEmpty(str1) && !isEmpty(str2) && str1.equals(str2)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 方法描述 : 4:list 合并 创建者：lixin 创建时间： 2015年1月16日 下午2:00:07
	 * 
	 * @param arry
	 * @return List
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeSame(List arry) {

		Set set = new LinkedHashSet();
		for (Object tmp : arry) {
			set.add(tmp);
		}

		return Arrays.asList(set.toArray());
	}

	/**
	 * 
	 * 方法描述 : 5:数组翻转 创建者：lixin 创建时间： 2015年1月16日 下午2:02:37
	 * 
	 * @param ary
	 * @return String[]
	 *
	 */
	public static String[] reversalArray(String[] ary) {
		if (ary == null || ary.length == 0)
			return null;
		String[] newList = new String[ary.length];
		for (int i = ary.length - 1; i >= 0; i--)
			newList[ary.length - i - 1] = ary[i];
		return newList;
	}

	/**
	 * 6：随机生成字符串有数字，字母组成
	 * 
	 * @param len
	 *            生成的字符串的总长度
	 * @return 字符串
	 */
	public static String genRandomNum(int len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的字符串的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer sBuffer = new StringBuffer("");
		Random r = new Random();
		while (count < len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				sBuffer.append(str[i]);
				count++;
			}
		}

		return sBuffer.toString();
	}

	/**
	 * 7:根据dip返回当前设备上的px值
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipToPx(Context context, int dip) {
		int px = 0;
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		float density = dm.density;
		px = (int) (dip * density);
		// Print.println("pxToDip px = " + px);
		return px;
	}

	/**
	 * 
	 * 方法描述 8:用序列化与反序列化实现深克隆 创建者：lixin 创建时间： 2015年1月16日 下午2:06:13
	 * 
	 * @param src
	 * @return Object
	 *
	 */
	public static Object deepClone(Object src) {
		Object o = null;
		try {
			if (src != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(src);
				oos.close();
				ByteArrayInputStream bais = new ByteArrayInputStream(
						baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				o = ois.readObject();
				ois.close();
				baos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 
	 * 方法描述 : 9:日期比较 创建者：lixin 创建时间： 2015年1月16日 下午2:07:18
	 * 
	 * @param date1
	 * @param date2
	 * @return int
	 *
	 */
	public static int compareDate(String date1, String date2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 10:设置粗体
	 *
	 */
	public static void setTextViewBold(TextView textView) {
		Paint paint = textView.getPaint();
		paint.setFakeBoldText(true);
	}

	/**
	 * 11:软键盘开关，当软键盘开启的时候调用这个方法关闭，当软键盘没有弹出的时候调用这个方法弹出
	 * 
	 * @param context
	 */
	public static void softKeyboard(Context context) {
		InputMethodManager m = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/**
	 * 12:显示软件盘
	 * 
	 * @param context
	 * @param textView
	 *            编译控件的对象例如：TextView、EditText
	 */
	public static void showSoftKeyboard(Context context, TextView textView) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(textView, 0, null);
	}

	/**
	 * 13:隐藏软件盘
	 * 
	 * @param context
	 * @param textView
	 *            编译控件的对象例如：TextView、EditText
	 */
	public static void hideSoftKeyboard(Context context, TextView textView) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
	}

	/**
	 * 14:判断网络
	 * 
	 * @return -1没有网络
	 */
	public static int netWorkStatus(Context context) {

		boolean netSataus = false;
		int result = -1;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			netSataus = networkInfo.isAvailable();
		}

		return true == netSataus ? 0 : result;
	}

	/**
	 * 
	 * 方法描述 : 15：返回当前网络类型 创建者：lixin 创建时间： 2015年1月16日 下午2:13:17
	 * 
	 * @param context
	 * @return int
	 *
	 */
	public static int newWorkTpye(Context context) {

		int netType = -1;
		NetworkInfo networkInfo = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

			return ConnectivityManager.TYPE_MOBILE;

		} else if (networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

			return ConnectivityManager.TYPE_WIFI;

		}

		return netType;

	}

	/**
	 * 16:判断字符串是不是数字
	 * 
	 * @return true是数字 false不是数字
	 */
	public static boolean isDigit(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isDigit(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 17:合集,并集
	 * 
	 * @param oa1
	 * @param oa2
	 * @return
	 */
	public <E> Object[] getUnion(E[] oa1, E[] oa2) {
		Set<E> ars = new HashSet<E>();
		ars.addAll(Arrays.asList(oa1));
		ars.addAll(Arrays.asList(oa2));
		return ars.toArray();
	}

	/**
	 * 18:交集
	 * 
	 * @param oa1
	 * @param oa2
	 * @return
	 */
	public <E> Object[] getIntersection(E[] oa1, E[] oa2) {
		List<E> ls1 = Arrays.asList(oa1);
		ls1.retainAll(Arrays.asList(oa2));
		return ls1.toArray();

	}

	// 19:方法一：循环元素删除
	// 删除ArrayList中重复元素
	public static void removeDuplicate(List list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
	}

	// 20:方法二：通过HashSet剔除
	// 删除ArrayList中重复元素
	public static void removeDuplicate1(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
	}

	/**
	 * 21:缩放图片
	 * 
	 * @param context
	 * @param imageId
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImage(Context context, int imageId, int newWidth,
			int newHeight) {
		Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(),
				imageId);
		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, newWidth,
				height, matrix, true);

		return resizedBitmap;
	}

	/**
	 * 22:bitmap转换成drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * 23:获得Device Id
	 * 
	 * @param app
	 * @return
	 */
	public static String getDeviceId(Application app) {
		TelephonyManager tm = (TelephonyManager) app.getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						app.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		long deviceIdLong = (long) tmDevice.hashCode();
		UUID deviceUuid = new UUID(androidId.hashCode(), deviceIdLong << 32
				| tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}

	/**
	 * 24:触发移动事件的最短距离，如果小于这个距离就不触发移动控件
	 * 
	 * @param context
	 * @return
	 */
	public static int getTouchSlop(Context context) {
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		return configuration.getScaledTouchSlop();
	}

	/**
	 * 25:判断sd卡是否插入
	 * 
	 * @return 插入返回true， 否则false
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * 26:返回一定格式的当前时间
	 * 
	 * @param pattern
	 *            "yyyy-MM-dd HH:mm:ss E"
	 * @return
	 */
	public static String getCurrentDate(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		TimeZone timeZone = TimeZone.getTimeZone("Hongkong");
		sdf.setTimeZone(timeZone);
		String dateStr = sdf.format(new Date());
		return dateStr;

	}

	/**
	 * 27:调用发短信界面
	 * 
	 * @param context
	 * @param messageString
	 */
	public static void sendMessageActivity(Context context, String messageString) {
		if (isSIM(context)) {
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.putExtra("sms_body", messageString);
			it.setType("vnd.android-dir/mms-sms");
			context.startActivity(it);
		}
	}


	/**
	 * 29:是否插入sim卡
	 * 
	 * @param context
	 * @return true插入sim，false没有插入sim卡
	 */
	public static boolean isSIM(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMgr.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
			Toast.makeText(context, "没有插入sim卡", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 显示log便于统一管理
	 * @param c
	 * @param string
	 */
	public static void log(Class c,String string) {
		if (AppConfig.isShowLog) {
			String str = c.getName();
			String[] strs = str.split("\\.");
			Log.v(strs[strs.length - 1], string);
		}

	}

	/**
	 * Get yesterday system date.
	 * @param pattern
	 * @return
	 */
	public static String getYesterdayDate(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		return dateFormat.format(calendar.getTime());
	}


	/**
	 * 获取本应用在sd中的地址
	 * @return
	 */
	public static String getAppPath() {
		if (!checkSDCard()) {
			return null;
		}
		File file = new File(Environment.getExternalStorageDirectory() + "/"
				+ "mylawyer");
		if (!file.exists()) {
			file.mkdirs();
		}

		return file.getAbsolutePath();
	}

	/**
	 * 每次开启程序都要清除本地文件
	 */
	public static void clearMyLawyer () {
		if (!checkSDCard()) return;
		File file = new File(Environment.getExternalStorageDirectory() + "/"
				+ "mylawyer");
		deleteFile(file);
	}

	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
		}
	}

	/**
	 * 获得手机型号
	 * @return
	 */
	public static String getModel() {
		String model = android.os.Build.MODEL;
		try {
			model = URLEncoder.encode(model, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return model;
	}


	/**
	 * 默认位置
	 * @param context
	 * @return
	 */
	private static FrameLayout.LayoutParams getDefaultParams(Context context) {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							Gravity.RIGHT | Gravity.TOP);
		return layoutParams;
	}

	/*
  * 判断是否为整数
  * @param str 传入的字符串
  * @return 是整数返回true,否则返回false
*/


	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static String getVersion(BaseActivity baseActivity){
		try {
			PackageManager manager = baseActivity.getPackageManager();
			PackageInfo info = manager.getPackageInfo(baseActivity.getPackageName(),0);
			String version = info.versionName;
			return version;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
