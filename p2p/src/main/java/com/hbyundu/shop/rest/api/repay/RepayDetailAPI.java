package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.RepaySubDataModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RepayDetailAPI extends RepayBaseAPI {

    private static final String CMD_ID = "00020";

    private RepayDetailAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RepayDetailAPI INSTANCE = new RepayDetailAPI();
    }

    public static RepayDetailAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void repayDetail(long dealId, final SubscriberOnListener<RepaySubDataModel> listener) {
        cancel();

        subscription = service.repayDetail(CMD_ID, dealId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RepaySubDataModel>())
                .subscribe(new Subscriber<RepaySubDataModel>() {
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
                    public void onNext(RepaySubDataModel repaySubDataModel) {
                        if (listener != null) {
                            listener.onSucceed(repaySubDataModel);
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
