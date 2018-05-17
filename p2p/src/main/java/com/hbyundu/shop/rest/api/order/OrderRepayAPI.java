package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderRepayResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrderRepayAPI extends OrderBaseAPI {

    private static final String CMD_ID = "00016";

    private OrderRepayAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final OrderRepayAPI INSTANCE = new OrderRepayAPI();
    }

    public static OrderRepayAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void orderRepay(long fenQiId, final SubscriberOnListener<OrderRepayResultModel> listener) {
        cancel();

        subscription = service.orderRepay(CMD_ID, fenQiId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<OrderRepayResultModel>())
                .subscribe(new Subscriber<OrderRepayResultModel>() {
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
                    public void onNext(OrderRepayResultModel orderRepayResultModel) {
                        if (listener != null) {
                            listener.onSucceed(orderRepayResultModel);
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
