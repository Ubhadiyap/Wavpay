package com.hbyundu.shop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.launcher.AutoLoginAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;

/**
 * Created by apple on 2018/3/22.
 */

public class WavReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if ("com.hbyundu.shop.wav.SET_USER".equals(intent.getAction())) {
            final String mobile = intent.getStringExtra("mobile");
            AutoLoginAPI.getInstance().login(mobile, new SubscriberOnListener<LoginResultModel>() {
                @Override
                public void onSucceed(LoginResultModel data) {
                    UserManager.getInstance(context.getApplicationContext()).setUid(data.userId);
                    UserManager.getInstance(context.getApplicationContext()).setUsername(mobile);
                    UserManager.getInstance(context.getApplicationContext()).setMobile(data.mobile);
                }

                @Override
                public void onError(String msg) {
                }
            });
        } else if ("com.hbyundu.shop.wav.CLEAR_USER".equals(intent.getAction())) {
            UserManager.getInstance(context.getApplicationContext()).clear();
        }
    }
}
