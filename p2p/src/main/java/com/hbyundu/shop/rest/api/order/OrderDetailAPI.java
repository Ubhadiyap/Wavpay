package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderItemModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrderDetailAPI extends OrderBaseAPI {

    private static final String CMD_ID = "00014";

    private OrderDetailAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final OrderDetailAPI INSTANCE = new OrderDetailAPI();
    }

    public static OrderDetailAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void orderDetail(long orderId, final SubscriberOnListener<OrderItemModel> listener) {
        cancel();

        subscription = service.orderDetail(CMD_ID, orderId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<OrderItemModel>())
                .subscribe(new Subscriber<OrderItemModel>() {
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
                    public void onNext(OrderItemModel orderItemModel) {
                        if (listener != null) {
                            listener.onSucceed(orderItemModel);
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
