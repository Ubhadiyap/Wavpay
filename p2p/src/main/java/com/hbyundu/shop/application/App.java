package com.hbyundu.shop.application;

import android.app.Application;

/**
 * Created by apple on 2017/12/20.
 */

public class App {

    private static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }

    public static Application getApplication() {
        return mApplication;
    }

    public static String getStringById(int resId) {
        return getApplication().getString(resId);
    }
}
