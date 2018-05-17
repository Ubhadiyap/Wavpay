package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.RechargeResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RechargeAPI extends InvestBaseAPI {

    private static final String CMD_ID = "00026";

    private RechargeAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RechargeAPI INSTANCE = new RechargeAPI();
    }

    public static RechargeAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void rate(long userId, long rateId, double money, final SubscriberOnListener<String> listener) {
        cancel();

        subscription = service.recharge(CMD_ID, userId, rateId, money)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RechargeResultModel>())
                .subscribe(new Subscriber<RechargeResultModel>() {
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
                    public void onNext(RechargeResultModel rechargeResultModel) {
                        if (rechargeResultModel.state == 0) {
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
