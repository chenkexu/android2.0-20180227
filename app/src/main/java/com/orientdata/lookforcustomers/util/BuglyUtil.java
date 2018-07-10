package com.orientdata.lookforcustomers.util;

import android.content.Context;

import com.orientdata.lookforcustomers.view.home.MainHomeActivity;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

public class BuglyUtil {

    /**
     * 初始化SDK
     */
    public static void init(Context context) {
        // true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
        Beta.autoCheckUpgrade = false;
        // 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗
        Beta.canShowUpgradeActs.add(MainHomeActivity.class);
        //参数3：是否开启debug模式，true表示打开debug模式，false表示关闭调试模式
        Bugly.init(context, "0559356edf", false);
    }

    /**
     * 静默检查更新，并弹窗
     */
    public static void checkUpdate() {
        /**
         * @param isManual  用户手动点击检查，非用户点击操作请传false
         * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
         */
        Beta.checkUpgrade(false, false);
    }
}

