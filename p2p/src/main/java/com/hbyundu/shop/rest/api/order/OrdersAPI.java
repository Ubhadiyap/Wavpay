package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderDataModel;
import com.hbyundu.shop.rest.model.order.OrderItemModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrdersAPI extends OrderBaseAPI {

    private int currentPage;

    private static final String CMD_ID = "00013";

    private OrdersAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final OrdersAPI INSTANCE = new OrdersAPI();
    }

    public static OrdersAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refresh(long userId, final SubscriberOnListener<List<OrderItemModel>> listener) {
        currentPage = 0;
        orders(userId, currentPage, listener);
    }

    public void loadMore(long userId, final SubscriberOnListener<List<OrderItemModel>> listener) {
        orders(userId, currentPage, listener);
    }

    private void orders(long userId, int page, final SubscriberOnListener<List<OrderItemModel>> listener) {
        cancel();

        subscription = service.orders(CMD_ID, userId, page + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<OrderDataModel>())
                .subscribe(new Subscriber<OrderDataModel>() {
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
                    public void onNext(OrderDataModel orderDataModel) {
                        currentPage++;
                        if (listener != null) {
                            listener.onSucceed(orderDataModel.list);
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
