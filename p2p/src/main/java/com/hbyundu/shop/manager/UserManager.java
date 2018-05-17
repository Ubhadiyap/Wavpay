package com.hbyundu.shop.manager;

import android.content.Context;

/**
 * Created by sunhaigang on 2017/9/8.
 */

public class UserManager {

    private static UserManager sInstance;

    private UserPreference mPreference;

    private Context mContext;

    public static synchronized UserManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UserManager(context);
        }

        return sInstance;
    }

    private UserManager(Context context) {
        mPreference = new UserPreference(context);
        mContext = context;
    }

    public void setUid(long uid) {
        mPreference.put(UserPreference.UID, uid);
    }

    public void setUsername(String username) {
        mPreference.put(UserPreference.USERNAME, username);
    }

    public Long getUid() {
        return mPreference.getLong(UserPreference.UID, 0);
    }

    public String getUsername() {
        return mPreference.getString(UserPreference.USERNAME, "");
    }

    public void setMobile(String mobile) {
        mPreference.put(UserPreference.MOBILE, mobile);
    }

    public String getMobile() {
        return mPreference.getString(UserPreference.MOBILE, "");
    }

    public boolean isAuth() {
        return (getUid() > 0);
    }

    public void clear() {
        mPreference.clear();
    }
}