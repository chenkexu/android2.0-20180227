package vr.md.com.mdlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关的工具
 * Created by Phoenix on 2016/10/12.
 */

public class StringUtil {
    public static final String PHONE_PATTERN = "1[3-9][0-9]{9}";
    private static final String ZIP_CODE = "^[1-9]\\d{5}$";//邮编正则表达式

    /**
     * 检测输入的内容是否是手机号码
     *
     * @param phoneNum 输入的手机号
     * @return true:是手机号码，false：不是手机号
     */
    public static boolean isPhoneNum(String phoneNum) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    /**
     * 验证是否是邮编
     */
    public static boolean isZipCode(String zipCode) {
        Pattern pattern = Pattern.compile(ZIP_CODE);
        Matcher matcher = pattern.matcher(zipCode);
        return matcher.matches();
    }
}
