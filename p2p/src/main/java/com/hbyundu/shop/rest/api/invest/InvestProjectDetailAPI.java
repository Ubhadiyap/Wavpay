package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestDetailModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class InvestProjectDetailAPI extends InvestBaseAPI {

    private static final String CMD_ID = "00010";

    private InvestProjectDetailAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final InvestProjectDetailAPI INSTANCE = new InvestProjectDetailAPI();
    }

    public static InvestProjectDetailAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void investDetail(long userId, long detailId, final SubscriberOnListener<InvestDetailModel> listener) {
        cancel();

        subscription = service.investDetail(CMD_ID, detailId, userId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<InvestDetailModel>())
                .subscribe(new Subscriber<InvestDetailModel>() {
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
                    public void onNext(InvestDetailModel investDetailModel) {
                        if (listener != null) {
                            listener.onSucceed(investDetailModel);

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
