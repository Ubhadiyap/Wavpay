package com.hbyundu.shop.rest.api.launcher;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.SignUpResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class SignUpAPI extends LauncherBaseAPI {

    private static final String CMD_ID = "00007";

    private SignUpAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final SignUpAPI INSTANCE = new SignUpAPI();
    }

    public static SignUpAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void signUp(String username, String password, String mobile, final SubscriberOnListener<SignUpResultModel> listener) {
        cancel();

        subscription = service.signUp(CMD_ID, username, password, mobile)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<SignUpResultModel>())
                .subscribe(new Subscriber<SignUpResultModel>() {
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
                    public void onNext(SignUpResultModel signUpResultModel) {
                        if (signUpResultModel.code == 0) {
                            if (listener != null) {
                                listener.onError(null);
                            }
                        } else {
                            if (listener != null) {
                                listener.onSucceed(signUpResultModel);
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
