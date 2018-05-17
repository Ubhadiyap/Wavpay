package com.hbyundu.shop.rest.api.launcher;

import android.text.TextUtils;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;
import com.hbyundu.shop.vendor.encrypt.MD5;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class AutoLoginAPI extends LauncherBaseAPI {

    private static final String CMD_ID = "00029";

    private AutoLoginAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final AutoLoginAPI INSTANCE = new AutoLoginAPI();
    }

    public static AutoLoginAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void login(String mobile, final SubscriberOnListener<LoginResultModel> listener) {
        cancel();

        subscription = service.autoLogin(CMD_ID, mobile)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<LoginResultModel>())
                .subscribe(new Subscriber<LoginResultModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(LoginResultModel loginResultModel) {
                        if (!TextUtils.isEmpty(loginResultModel.error)) {
                            if (listener != null) {
                                listener.onError(loginResultModel.error);
                            }
                        } else {
                            if (listener != null) {
                                listener.onSucceed(loginResultModel);
                            }
                        }
                    }
                });
    }

    public void cancel() {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
