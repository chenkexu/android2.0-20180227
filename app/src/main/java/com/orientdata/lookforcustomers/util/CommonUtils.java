package com.orientdata.lookforcustomers.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.blankj.utilcode.util.ResourceUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Phoenix on 2016/8/13.
 */
public class CommonUtils {

    private String ageF = "不限"; //最后的起始年龄
    private String ageB = "不限";  //最后的终止年龄
    private String sex = "不限";   //性别
    private String industryStr = "不限"; //行业
    private int labelNum = 0;


    public static String getPersonNum(String currentCircleRadius, String cityName, String ageF, String ageB, String sex, String industryStr, int labelNum) {
        String ageValue = "";

        double sexValue = 1.0;
        double intersValue = 1.0;

        Logger.d("选择的参数为: " + currentCircleRadius + "," + cityName + "," + ageF + "," + ageB + "," + sex + "," + industryStr + "," + labelNum);
        int personNumStr = CommonUtils.getRandom2(currentCircleRadius, cityName);

        if (sex.equals("不限")) {
            sexValue = 1.0;
//            personNumStr = (int) (personNumStr * 1.0);
        } else {
            sexValue = 0.5;
//            personNumStr = (int) (personNumStr * 0.5);
        }


        // TODO: 2018/6/12 年龄选择
        String ageJson = ResourceUtils.readAssets2String("dataAge.json");
        HashMap<String, Map<String, String>> ageJsonMap = GsonUtils.parseJsonToMap(ageJson);
        for (Map.Entry<String, Map<String, String>> entry : ageJsonMap.entrySet()) {
            if (ageF.equals(entry.getKey())) {
                Map<String, String> value = entry.getValue();
                for (Map.Entry<String, String> entry2 : value.entrySet()) {
                    if (ageB.equals(entry2.getKey())) {
                        ageValue = entry2.getValue();
                        Logger.d("ageValue: " + ageValue);
                    }
                }
            }
        }

//        personNumStr = (int) (personNumStr * (Double.parseDouble(ageValue)+sexValue) * 0.5);

        if (industryStr.equals("不限")) {
            intersValue = intersValue * 1.0;
//            personNumStr = (int) (personNumStr * 1.0);
        } else if (industryStr.equals("自定义")) {
            if (labelNum == 6) {
                intersValue = labelNum * 1.0;
//                personNumStr = (int)(personNumStr * labelNum * 1.0);
            } else {
                intersValue = labelNum * 0.1666;
//                personNumStr = (int)(personNumStr * labelNum * 0.1666);
            }
        } else {
            double randomDouble = getRandomDouble(0.72, 0.82);
            intersValue = randomDouble * 0.5;
//            personNumStr = (int) (personNumStr * randomDouble * 0.5);
        }

        personNumStr = (int) (personNumStr * ((sexValue + Double.parseDouble(ageValue)) * 0.5 + intersValue * 0.5));

        return "当前范围符合您标签的约有" + personNumStr + "人";
    }


    public static double getRandomDouble(double min, double max) {
        double d = min + Math.random() * max % (max - min + 1);
        return d;
    }


    public static int getRandom(int min, int max) {

        Random random = new Random();
        int s = (random.nextInt(max) % (max - min + 1) + min);
        return s;
    }


    public static int getRandom2(String radius, String cityName) {
        Logger.d("当前的城市名:" + cityName);
        int radiusRam = 0;
        switch (radius) {
            case "500":
                radiusRam = CommonUtils.getRandom(8000, 12000);
                break;
            case "1000":
                radiusRam = CommonUtils.getRandom(28000, 32000);
                break;
            case "2000":
                radiusRam = CommonUtils.getRandom(90000, 110000);
                break;
            case "3000":
                radiusRam = CommonUtils.getRandom(290000, 310000);
                break;
            case "5000":
                radiusRam = CommonUtils.getRandom(900000, 1100000);
                break;
            case "10000":
                radiusRam = CommonUtils.getRandom(1800000, 2200000);
                break;
        }
        Double cityMapValue = CommonUtils.getCityMapValue(cityName);
        int personNum = (int) (radiusRam * cityMapValue);
        return personNum;
    }


    public static Double getCityMapValue(String cityName) {
        HashMap<String, Double> cityMap = new HashMap<String, Double>() {
            private static final long serialVersionUID = 1L;

            {
                put("北京市", 1.0);
                put("上海市", 1.0);
                put("广州市", 1.0);
                put("深圳市", 1.0);
                put("成都市", 0.98);
                put("杭州市", 0.98);
                put("武汉市", 0.98);
                put("重庆市", 0.98);
                put("南京市", 0.98);
                put("天津市", 0.98);
                put("苏州市", 0.98);
                put("西安市", 0.98);
                put("长沙市", 0.98);
                put("沈阳市", 0.98);
                put("青岛市", 0.98);
                put("郑州市", 0.98);
                put("大连市", 0.98);
                put("东莞市", 0.98);
                put("宁波市", 0.98);
            }
        };

        for (Map.Entry<String, Double> entry : cityMap.entrySet()) {
            if (cityName.equals(entry.getKey())) {
                return entry.getValue();
            }
        }

        return 0.95;
    }


    /**
     * 返回一定格式的时间
     *
     * @param pattern "yyyy-MM-dd HH:mm:ss E"
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
     * 将时间转换成相应的pattern
     *
     * @param date
     * @param pattern "yyyy-MM-dd HH:m:s"
     * @return
     */
    public static String getTimeInterval(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date dateTmp = getDate(date);
        return format.format(dateTmp);
    }

    /**
     * 将时间由patternDate转换成相应的pattern
     *
     * @param date
     * @param patternDate
     * @param pattern
     * @return
     */
    public static String getTimeInterval(String date, String patternDate, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date dateTmp = getDate(date, patternDate);
        return format.format(dateTmp);
    }

    public static String getTimeInterval1(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date dateTmp = getDate1(date);
        return format.format(dateTmp);
    }

    /**
     * 字符串转Data
     */
    public static Date getDate(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        try {
            return format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String data, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate1(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转字符串
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getDateStr(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 输入的内容是否是手机号码
     */
    public static boolean isPhoneNum(String phoneNum) {
        Pattern pattern = Pattern.compile("1[3-9][0-9]{9}");
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    /**
     * 输入的内容是否是密码
     */
    public static boolean isPassword(String password) {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{6,18}");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 判断密码是否符合规则
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        //校验整数
        boolean hasNum = password.matches(".*\\d+.*");
        //检查字母
        boolean hasLetter = password.matches(".*[a-zA-Z]+.*");
        //检查长度
        boolean hasLength = password.length() >= 6 && password.length() <= 18;
        return hasNum && hasLetter && hasLength;
    }

    /**
     * 判断输入的内容是不是身份证号
     */
    public static boolean isCertificate(String id) {
        Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }

    /**
     * 验证邮箱格式是否正确
     */
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9#_~!$&‘()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String twoDecimal(double decimal) {
        DecimalFormat df = new DecimalFormat(".##");
        String st = df.format(decimal);
        return st;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     * 三星手机前置为8，旋转270度。后置为6旋转90度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            System.out.println("图片的旋转角度是==" + orientation);
            switch (orientation) {
                case 6:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case 8:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
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
     * sd卡是否可用
     *
     * @return true 如果sd卡可读可写
     */
    public static boolean haveSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     *
     * @param value
     * @return Sting
     */
    public static String formatFloatNumber(double value) {
        if (value != 0.00) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("##########.########");
            return df.format(value);
        } else {
            return "0.00";
        }

    }

    public static String formatFloatNumber(Double value) {
        if (value != null) {
            if (value.doubleValue() != 0.00) {
                java.text.DecimalFormat df = new java.text.DecimalFormat("##########.########");
                return df.format(value.doubleValue());
            } else {
                return "0.00";
            }
        }
        return "";
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();

        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }

        return res;
    }


    /**
     * 判定输入汉字是否是中文
     *
     * @param c
     */
    public static boolean isChinese(char c) {
        for (char param : chineseParam) {
            if (param == c) {
                return false;
            }
        }

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }

        return false;
    }

    public static final int INPUT_LIMIT_LEN = 20;
    private static char[] chineseParam = new char[]{'」', '，', '。', '？', '…', '：', '～', '【', '＃', '、', '％', '＊', '＆', '＄', '（', '‘', '’',
            '“', '”', '『', '〔', '｛', '【', '￥', '￡', '‖', '〖', '《', '「', '》', '〗', '】', '｝', '〕', '』', '”', '）', '！', '；', '—'};

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public Uri setQQUri(String key) {
        Uri data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key);
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return data;
    }

}
