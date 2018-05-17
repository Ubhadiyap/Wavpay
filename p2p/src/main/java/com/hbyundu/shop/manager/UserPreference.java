package com.hbyundu.shop.manager;

import android.content.Context;

import net.grandcentrix.tray.TrayModulePreferences;

/**
 * Created by sunhaigang on 2017/9/8.
 */

public class UserPreference extends TrayModulePreferences {

    public static final String UID = "userId";

    public static final String USERNAME = "username";

    public static final String MOBILE = "mobile";

    public UserPreference(final Context context) {
        super(context, "shop", 1);
    }

    @Override
    protected void onCreate(int initialVersion) {
    }

    @Override
    protected void onUpgrade(int oldVersion, int newVersion) {

    }
}

