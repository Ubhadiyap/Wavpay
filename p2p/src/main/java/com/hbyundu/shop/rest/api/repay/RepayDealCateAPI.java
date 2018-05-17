package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.DealCateDataModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RepayDealCateAPI extends RepayBaseAPI {

    private RepayDealCateAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RepayDealCateAPI INSTANCE = new RepayDealCateAPI();
    }

    public static RepayDealCateAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void dealCate(final SubscriberOnListener<DealCateDataModel> listener) {
        cancel();

        subscription = service.dealCate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<DealCateDataModel>())
                .subscribe(new Subscriber<DealCateDataModel>() {
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
                    public void onNext(DealCateDataModel dealCateDataModel) {
                        if (listener != null) {
                            listener.onSucceed(dealCateDataModel);
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
