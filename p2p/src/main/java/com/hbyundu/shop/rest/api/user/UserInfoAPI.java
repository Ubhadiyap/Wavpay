package com.hbyundu.shop.rest.api.user;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.user.UserInfoModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class UserInfoAPI extends UserBaseAPI {

    private static final String CMD_ID = "00018";

    private UserInfoAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final UserInfoAPI INSTANCE = new UserInfoAPI();
    }

    public static UserInfoAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void userInfo(long userId, final SubscriberOnListener<UserInfoModel> listener) {
        cancel();

        subscription = service.userInfo(CMD_ID, userId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<UserInfoModel>())
                .subscribe(new Subscriber<UserInfoModel>() {
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
                    public void onNext(UserInfoModel userInfoModel) {
                        if (listener != null) {
                            listener.onSucceed(userInfoModel);
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
