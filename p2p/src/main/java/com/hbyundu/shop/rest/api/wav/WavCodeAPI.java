package com.hbyundu.shop.rest.api.wav;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.wav.WavResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2018/3/6.
 */

public class WavCodeAPI extends WavBaseAPI {

    private WavCodeAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final WavCodeAPI INSTANCE = new WavCodeAPI();
    }

    public static WavCodeAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getCode(String account, final SubscriberOnListener<String> listener) {
        cancel();

        subscription = service.getCode(account)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<WavResultModel>())
                .subscribe(new Subscriber<WavResultModel>() {
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
                    public void onNext(WavResultModel resultModel) {
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
