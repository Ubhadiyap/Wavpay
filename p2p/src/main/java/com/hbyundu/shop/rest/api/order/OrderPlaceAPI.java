package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrderPlaceAPI extends OrderBaseAPI {

    private static final String CMD_ID = "00011";

    private long userId;
    private long goodsId;
    private String goodsAttr;
    private int fenqi;
    private int count;
    private String deliveryAddr;
    private String deliveryTel;
    private String deliveryName;
    private String memo;
    private int addressType;
    private long addressId;
    private long dealId;

    private OrderPlaceAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final OrderPlaceAPI INSTANCE = new OrderPlaceAPI();
    }

    public static OrderPlaceAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }



    public void placeOrder(final SubscriberOnListener<OrderResultModel> listener) {
        cancel();

        subscription = service.placeOrder(CMD_ID, userId, goodsId, goodsAttr, fenqi, count, deliveryAddr, deliveryTel, deliveryName, memo, addressType, addressId, dealId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<OrderResultModel>())
                .subscribe(new Subscriber<OrderResultModel>() {
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
                    public void onNext(OrderResultModel orderResultModel) {
                        if (listener != null) {
                            listener.onSucceed(orderResultModel);
                        }
                    }
                });
    }

    public void cancel() {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public OrderPlaceAPI setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public OrderPlaceAPI setGoodsId(long goodsId) {
        this.goodsId = goodsId;
        return this;
    }

    public OrderPlaceAPI setGoodsAttr(String goodsAttr) {
        this.goodsAttr = goodsAttr;
        return this;
    }

    public OrderPlaceAPI setFenqi(int fenqi) {
        this.fenqi = fenqi;
        return this;
    }

    public OrderPlaceAPI setCount(int count) {
        this.count = count;
        return this;
    }

    public OrderPlaceAPI setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
        return this;
    }

    public OrderPlaceAPI setDeliveryTel(String deliveryTel) {
        this.deliveryTel = deliveryTel;
        return this;
    }

    public OrderPlaceAPI setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
        return this;
    }

    public OrderPlaceAPI setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public OrderPlaceAPI setAddressType(int addressType) {
        this.addressType = addressType;
        return this;
    }

    public OrderPlaceAPI setAddressId(long addressId) {
        this.addressId = addressId;
        return this;
    }

    public OrderPlaceAPI setDealId(long dealId) {
        this.dealId = dealId;
        return this;
    }
}
