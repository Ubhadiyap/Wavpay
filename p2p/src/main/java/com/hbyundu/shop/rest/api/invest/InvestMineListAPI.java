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

public class InvestMineListAPI extends InvestBaseAPI {

    private int currentPage;

    private static final String CMD_ID = "00012";

    private InvestMineListAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final InvestMineListAPI INSTANCE = new InvestMineListAPI();
    }

    public static InvestMineListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void refresh(long userId, final SubscriberOnListener<List<InvestListModel>> listener) {
        currentPage = 0;
        myInvestList(currentPage, userId, listener);
    }

    public void loadMore(long userId, final SubscriberOnListener<List<InvestListModel>> listener) {
        myInvestList(currentPage, userId, listener);
    }

    public void myInvestList(int page, long userId, final SubscriberOnListener<List<InvestListModel>> listener) {
        cancel();

        subscription = service.investMineList(CMD_ID, page + 1, userId)
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
