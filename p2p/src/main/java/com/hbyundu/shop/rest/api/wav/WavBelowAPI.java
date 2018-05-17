package com.hbyundu.shop.rest.api.wav;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.wav.WavBelowModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2018/3/6.
 */

public class WavBelowAPI extends WavBaseAPI {

    private WavBelowAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final WavBelowAPI INSTANCE = new WavBelowAPI();
    }

    public static WavBelowAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void below(String orderId, String amount, final SubscriberOnListener<String> listener) {
        cancel();

        subscription = service.below(orderId, amount)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<WavBelowModel>())
                .subscribe(new Subscriber<WavBelowModel>() {
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
                    public void onNext(WavBelowModel resultModel) {
                        if (listener != null) {
                            if (resultModel.statusCode == 200) {
                                listener.onSucceed(resultModel.msg);
                            } else {
                                listener.onError(resultModel.msg);
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
