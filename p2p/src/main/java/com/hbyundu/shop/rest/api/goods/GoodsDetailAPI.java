package com.hbyundu.shop.rest.api.goods;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsDetailModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class GoodsDetailAPI extends GoodsBaseAPI {

    private static final String CMD_ID = "00006";

    private GoodsDetailAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final GoodsDetailAPI INSTANCE = new GoodsDetailAPI();
    }

    public static GoodsDetailAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void goodsDetail(long goodsId, final SubscriberOnListener<GoodsDetailModel> listener) {
        cancel();

        subscription = service.goodsDetail(CMD_ID, goodsId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<GoodsDetailModel>())
                .subscribe(new Subscriber<GoodsDetailModel>() {
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
                    public void onNext(GoodsDetailModel goodsDetailModel) {
                        if (listener != null) {
                            listener.onSucceed(goodsDetailModel);
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
