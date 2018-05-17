package com.hbyundu.shop.vendor.util;

import android.app.Activity;

import java.util.Stack;

public final class ActivityStackUtils {
    
    public static final String TAG = ActivityStackUtils.class.getName();
    
    private static ActivityStackUtils mInstance;
    
    private static Stack<Activity> mActivityStack;
    
    private ActivityStackUtils() {
        
    }
    
    /**
     * 获取单利.
     * @return
     */
    public static synchronized ActivityStackUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ActivityStackUtils();
            mActivityStack = new Stack<Activity>();
        }
        
        return mInstance;
    }
    
    /**
     * 获取顶端activity
     * @return
     */
    public Activity getTopActivity() {
        if (mActivityStack == null || mActivityStack.size() == 0) {
            return null;
        }
        
        return mActivityStack.lastElement();
    }

    /**
     * 获取activity
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        if (mActivityStack == null || mActivityStack.size() == 0) {
            return null;
        }

        for (Activity activity : mActivityStack) {
            if (activity == null) {
                break;
            }

            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }

        return null;
    }
    
    /**
     * 移除activity
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activity = null;
        }
    }
    
    /**
     * 移除顶端activity
     */
    public void popActivity() {
        Activity activity = getTopActivity();
        popActivity(activity);
    }
    
    /**
     * 移除activity一直到cls
     * @param cls
     * @param flag 0:不包括cls 1:包括cls
     */
    public void popToActivity(Class<?> cls, int flag) {
        while (true) {
            Activity activity = getTopActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                if (flag == 1) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                    popActivity(activity);
                }
                break;
            }
            activity.finish();
            popActivity(activity);
        }
    }
    
    /**
     * 压入activity
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (mActivityStack != null) {
            mActivityStack.add(activity);
        }
    }
    
    /**
     * 清除所有activity
     */
    public void clearActivity() {
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) { 
                if (!activity.isFinishing()) { 
                    activity.finish(); 
                } 
            } 
        }
    }
    
    /**
     * 判断是否包含某个界面
     * @param cls
     * @return
     */
    public boolean hasActivity(Class<?> cls) {
        if (mActivityStack == null || mActivityStack.size() == 0) {
            return false;
        }
        
        for (Activity activity : mActivityStack) {
            if (activity == null) {
                break;
            }
            
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        
        return false;
    }
}

