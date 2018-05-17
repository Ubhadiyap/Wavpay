package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.LoanDataModel;
import com.hbyundu.shop.rest.model.repay.LoanItemModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class LoanListAPI extends RepayBaseAPI {

    private static final String CMD_ID = "00032";

    private int currentPage;

    private LoanListAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final LoanListAPI INSTANCE = new LoanListAPI();
    }

    public static LoanListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refresh(long userId, final SubscriberOnListener<List<LoanItemModel>> listener) {
        currentPage = 0;
        loanList(userId, currentPage, listener);
    }

    public void loadMore(long userId, final SubscriberOnListener<List<LoanItemModel>> listener) {
        loanList(userId, currentPage, listener);
    }

    public void loanList(long userId, int page, final SubscriberOnListener<List<LoanItemModel>> listener) {
        cancel();

        subscription = service.loanList(CMD_ID, userId, page + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<LoanDataModel>())
                .subscribe(new Subscriber<LoanDataModel>() {
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
                    public void onNext(LoanDataModel loanDataModel) {
                        currentPage++;
                        if (listener != null) {
                            listener.onSucceed(loanDataModel.list);
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
