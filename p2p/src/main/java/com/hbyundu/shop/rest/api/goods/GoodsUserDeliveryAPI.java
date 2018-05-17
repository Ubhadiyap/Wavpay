package com.hbyundu.shop.rest.api.goods;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsDeliveryDataModel;
import com.hbyundu.shop.rest.model.goods.GoodsDeliveryModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class GoodsUserDeliveryAPI extends GoodsBaseAPI {

    private static final String CMD_ID = "00024";

    private GoodsUserDeliveryAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final GoodsUserDeliveryAPI INSTANCE = new GoodsUserDeliveryAPI();
    }

    public static GoodsUserDeliveryAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void userDelivery(long userId, final SubscriberOnListener<List<GoodsDeliveryModel>> listener) {
        cancel();

        subscription = service.userDelivery(CMD_ID, userId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<GoodsDeliveryDataModel>())
                .subscribe(new Subscriber<GoodsDeliveryDataModel>() {
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
                    public void onNext(GoodsDeliveryDataModel goodsDeliveryDataModel) {
                        if (listener != null) {
                            listener.onSucceed(goodsDeliveryDataModel.list);
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
