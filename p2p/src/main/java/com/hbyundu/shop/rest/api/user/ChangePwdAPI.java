package com.hbyundu.shop.rest.api.user;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.user.ChangePwdResultModel;
import com.hbyundu.shop.vendor.encrypt.MD5;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class ChangePwdAPI extends UserBaseAPI {

    private static final String CMD_ID = "00017";

    private ChangePwdAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final ChangePwdAPI INSTANCE = new ChangePwdAPI();
    }

    public static ChangePwdAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void changePwd(long userId, String oldPassword, String newPassword, final SubscriberOnListener<ChangePwdResultModel> listener) {
        cancel();

        oldPassword = MD5.getMD5String(oldPassword);
        newPassword = MD5.getMD5String(newPassword);
        subscription = service.changePwd(CMD_ID, userId, oldPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<ChangePwdResultModel>())
                .subscribe(new Subscriber<ChangePwdResultModel>() {
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
                    public void onNext(ChangePwdResultModel changePwdResultModel) {
                        if (changePwdResultModel.state == 0) {
                            if (listener != null) {
                                listener.onSucceed(changePwdResultModel);
                            }
                        } else {
                            if (listener != null) {
                                listener.onError(changePwdResultModel.msg);
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
