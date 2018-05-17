package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.RepayResultModel;
import com.hbyundu.shop.vendor.encrypt.MD5;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RepayAPI extends RepayBaseAPI {

    private static final String CMD_ID = "00021";

    private RepayAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RepayAPI INSTANCE = new RepayAPI();
    }

    public static RepayAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void repay(long repayId, String payPassword, final SubscriberOnListener<RepayResultModel> listener) {
        cancel();

        payPassword = MD5.getMD5String(payPassword);
        subscription = service.repay(CMD_ID, repayId, payPassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RepayResultModel>())
                .subscribe(new Subscriber<RepayResultModel>() {
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
                    public void onNext(RepayResultModel repayResultModel) {
                        if (listener != null) {
                            listener.onSucceed(repayResultModel);
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
