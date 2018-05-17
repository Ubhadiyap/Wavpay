package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.WithdrawResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class WithdrawAPI extends RepayBaseAPI {

    private static final String CMD_ID = "00031";

    private WithdrawAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final WithdrawAPI INSTANCE = new WithdrawAPI();
    }

    public static WithdrawAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void withdraw(long userId, String orderId, String money, final SubscriberOnListener<WithdrawResultModel> listener) {
        cancel();

        subscription = service.withdraw(CMD_ID, userId, orderId, money)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<WithdrawResultModel>())
                .subscribe(new Subscriber<WithdrawResultModel>() {
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
                    public void onNext(WithdrawResultModel resultModel) {
                        if (resultModel.state == 0) {
                            if (listener != null) {
                                listener.onSucceed(resultModel);
                            }
                        } else {
                            if (listener != null) {
                                listener.onError("");
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
