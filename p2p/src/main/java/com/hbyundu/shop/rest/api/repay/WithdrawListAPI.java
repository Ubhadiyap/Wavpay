package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.RepayDataModel;
import com.hbyundu.shop.rest.model.repay.RepayItemModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class WithdrawListAPI extends RepayBaseAPI {

    private static final String CMD_ID = "00030";

    private int currentPage;

    private WithdrawListAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final WithdrawListAPI INSTANCE = new WithdrawListAPI();
    }

    public static WithdrawListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refresh(long userId, int state, final SubscriberOnListener<List<RepayItemModel>> listener) {
        currentPage = 0;
        repayList(userId, state, currentPage, listener);
    }

    public void loadMore(long userId, int state, final SubscriberOnListener<List<RepayItemModel>> listener) {
        repayList(userId, state, currentPage, listener);
    }

    public void repayList(long userId, int state, int page, final SubscriberOnListener<List<RepayItemModel>> listener) {
        cancel();

        subscription = service.withdrawList(CMD_ID, userId, state, page + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RepayDataModel>())
                .subscribe(new Subscriber<RepayDataModel>() {
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
                    public void onNext(RepayDataModel repayDataModel) {
                        currentPage++;
                        if (listener != null) {
                            listener.onSucceed(repayDataModel.list);
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
