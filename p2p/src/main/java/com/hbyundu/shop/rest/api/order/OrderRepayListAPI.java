package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderRepayDataModel;
import com.hbyundu.shop.rest.model.order.OrderRepayItemModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrderRepayListAPI extends OrderBaseAPI {

    private static final String CMD_ID = "00015";

    private OrderRepayListAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final OrderRepayListAPI INSTANCE = new OrderRepayListAPI();
    }

    public static OrderRepayListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void orderRepayList(long orderId, final SubscriberOnListener<List<OrderRepayItemModel>> listener) {
        cancel();

        subscription = service.orderRepayList(CMD_ID, orderId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<OrderRepayDataModel>())
                .subscribe(new Subscriber<OrderRepayDataModel>() {
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
                    public void onNext(OrderRepayDataModel orderRepayDataModel) {
                        if (listener != null) {
                            listener.onSucceed(orderRepayDataModel.list);
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
