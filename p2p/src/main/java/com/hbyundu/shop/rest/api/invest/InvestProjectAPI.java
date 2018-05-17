package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestResultModel;
import com.hbyundu.shop.vendor.encrypt.MD5;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class InvestProjectAPI extends InvestBaseAPI {

    private static final String CMD_ID = "00022";

    private InvestProjectAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final InvestProjectAPI INSTANCE = new InvestProjectAPI();
    }

    public static InvestProjectAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void invest(long userId, long detailId, String payPassword, double money, final SubscriberOnListener<InvestResultModel> listener) {
        cancel();

        payPassword = MD5.getMD5String(payPassword);
        subscription = service.invest(CMD_ID, detailId, userId, payPassword, money)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<InvestResultModel>())
                .subscribe(new Subscriber<InvestResultModel>() {
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
                    public void onNext(InvestResultModel investResultModel) {
                        if (listener != null) {
                            listener.onSucceed(investResultModel);

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
