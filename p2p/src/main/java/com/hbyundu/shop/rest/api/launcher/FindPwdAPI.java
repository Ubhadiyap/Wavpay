package com.hbyundu.shop.rest.api.launcher;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.FindPwdResultModel;
import com.hbyundu.shop.vendor.encrypt.MD5;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class FindPwdAPI extends LauncherBaseAPI {

    private static final String CMD_ID = "00028";

    private FindPwdAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final FindPwdAPI INSTANCE = new FindPwdAPI();
    }

    public static FindPwdAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void findPwd(String username, String password, String mobile, final SubscriberOnListener<String> listener) {
        cancel();

        password = MD5.getMD5String(password);
        subscription = service.findPwd(CMD_ID, username, mobile, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<FindPwdResultModel>())
                .subscribe(new Subscriber<FindPwdResultModel>() {
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
                    public void onNext(FindPwdResultModel resultModel) {
                        if (resultModel.state == 0) {
                            if (listener != null) {
                                listener.onSucceed(null);
                            }
                        } else {
                            if (listener != null) {
                                listener.onError(null);
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
