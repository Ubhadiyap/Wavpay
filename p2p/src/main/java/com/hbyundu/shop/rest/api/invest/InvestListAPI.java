package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestListDataModel;
import com.hbyundu.shop.rest.model.invest.InvestListModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class InvestListAPI extends InvestBaseAPI {

    private int currentPage;

    private static final String CMD_ID = "00009";

    private InvestListAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final InvestListAPI INSTANCE = new InvestListAPI();
    }

    public static InvestListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void refresh(final SubscriberOnListener<List<InvestListModel>> listener) {
        currentPage = 0;
        investList(currentPage, listener);
    }

    public void loadMore(final SubscriberOnListener<List<InvestListModel>> listener) {
        investList(currentPage, listener);
    }

    public void investList(int page, final SubscriberOnListener<List<InvestListModel>> listener) {
        cancel();

        subscription = service.investList(CMD_ID, page + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<InvestListDataModel>())
                .subscribe(new Subscriber<InvestListDataModel>() {
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
                    public void onNext(InvestListDataModel investListDataModel) {
                        currentPage++;
                        if (listener != null) {
                            listener.onSucceed(investListDataModel.data);

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
