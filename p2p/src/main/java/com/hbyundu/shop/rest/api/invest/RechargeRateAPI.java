package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.RechargeRateDataModel;
import com.hbyundu.shop.rest.model.invest.RechargeRateModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RechargeRateAPI extends InvestBaseAPI {

    private static final String CMD_ID = "00027";

    private RechargeRateAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RechargeRateAPI INSTANCE = new RechargeRateAPI();
    }

    public static RechargeRateAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void rate(final SubscriberOnListener<List<RechargeRateModel>> listener) {
        cancel();

        subscription = service.getRate(CMD_ID)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RechargeRateDataModel>())
                .subscribe(new Subscriber<RechargeRateDataModel>() {
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
                    public void onNext(RechargeRateDataModel rateDataModel) {
                        if (listener != null) {
                            listener.onSucceed(rateDataModel.data);

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
