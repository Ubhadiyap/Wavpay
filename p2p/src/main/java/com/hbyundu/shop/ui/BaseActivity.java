package com.hbyundu.shop.ui;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hbyundu.shop.R;
import com.hbyundu.shop.vendor.util.ActivityStackUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by apple on 2017/11/29.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();

        ActivityStackUtils.getInstance().pushActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        ActivityStackUtils.getInstance().popActivity(this);
    }

    public void finishToActivity(Class<?> activityClass) {
        finish();
        ActivityStackUtils.getInstance().popToActivity(activityClass, 0);
    }

    public void finishBeforeActivity(Class<?> activityClass) {
        finish();
        ActivityStackUtils.getInstance().popToActivity(activityClass, 1);
    }

    public boolean hasActivityInHistory(Class<?> cls) {
        return ActivityStackUtils.getInstance().hasActivity(cls);
    }

    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorTitleBar);
    }

    public void addFragment(Fragment fragment, int containerId) {
        getSupportFragmentManager().beginTransaction().add(containerId, fragment).commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
    }
}
