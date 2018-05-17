package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.DealRateModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RepayDealRateAPI extends RepayBaseAPI {

    private RepayDealRateAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RepayDealRateAPI INSTANCE = new RepayDealRateAPI();
    }

    public static RepayDealRateAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void dealRate(final SubscriberOnListener<List<DealRateModel.DealRateItemModel>> listener) {
        cancel();

        subscription = service.getDealRate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<DealRateModel>())
                .subscribe(new Subscriber<DealRateModel>() {
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
                    public void onNext(DealRateModel dealRateModel) {
                        if (listener != null) {
                            listener.onSucceed(dealRateModel.lilv);
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
