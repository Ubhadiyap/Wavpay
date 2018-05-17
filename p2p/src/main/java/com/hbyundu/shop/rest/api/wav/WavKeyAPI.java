package com.hbyundu.shop.rest.api.wav;

import android.text.TextUtils;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.wav.WavKeyModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2018/3/6.
 */

public class WavKeyAPI extends WavBaseAPI {

    private WavKeyAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final WavKeyAPI INSTANCE = new WavKeyAPI();
    }

    public static WavKeyAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getKey(String account, String deviceCode, final SubscriberOnListener<String> listener) {
        cancel();

        subscription = service.key(account, deviceCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<WavKeyModel>())
                .subscribe(new Subscriber<WavKeyModel>() {
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
                    public void onNext(WavKeyModel keyModel) {
                        if (listener != null) {
                            if (keyModel.statusCode == 200 && !TextUtils.isEmpty(keyModel.key)) {
                                listener.onSucceed(keyModel.key);
                            } else {
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
