package com.hbyundu.shop.vendor.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;

/**
 * 包文件中静态信息的读取
 */
public class PackageUtils {

    /**
     * 获取meta-data.
     *
     * @param context
     * @param name         名字
     * @param defaultValue 默认值
     * @return
     */
    public static String getMetaDataValue(Context context, @NonNull String name, String defaultValue) {
        String value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name).toString();
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException(
                    "Could not read the name in the manifest file.", e);
        }

        if (value == null) {
            throw new RuntimeException("The name '" + name
                    + "' is not defined in the manifest file's meta data.");
        }

        return (value == null) ? defaultValue : value;
    }

    /**
     * 获取程序版本名称
     *
     * @param context
     * @param packageName  包名
     * @param defaultValue 默认值
     * @return
     */
    public static String getVersionName(Context context, @NonNull String packageName, String defaultValue) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return (versionName == null) ? defaultValue : versionName;
    }

    /**
     * 获取程序版本号
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static int getVersionCode(Context context, @NonNull String packageName) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
