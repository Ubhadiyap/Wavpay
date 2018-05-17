package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderConfirmResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrderConfirmAPI extends OrderBaseAPI {

    private static final String CMD_ID = "00025";

    private OrderConfirmAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final OrderConfirmAPI INSTANCE = new OrderConfirmAPI();
    }

    public static OrderConfirmAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void orderConfirm(long orderId, final SubscriberOnListener<OrderConfirmResultModel> listener) {
        cancel();

        subscription = service.orderConfirm(CMD_ID, orderId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<OrderConfirmResultModel>())
                .subscribe(new Subscriber<OrderConfirmResultModel>() {
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
                    public void onNext(OrderConfirmResultModel orderConfirmResultModel) {
                        if (listener != null) {
                            listener.onSucceed(orderConfirmResultModel);
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
