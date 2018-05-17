package com.hbyundu.shop.rest.api.goods;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsListDataModel;
import com.hbyundu.shop.rest.model.goods.GoodsListModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class GoodsSearchAPI extends GoodsBaseAPI {

    private int currentPage;

    private static final String CMD_ID = "00023";

    private GoodsSearchAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final GoodsSearchAPI INSTANCE = new GoodsSearchAPI();
    }

    public static GoodsSearchAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refresh(String keyword, final SubscriberOnListener<List<GoodsListModel>> listener) {
        currentPage = 0;
        searchGoods(keyword, currentPage, listener);
    }

    public void loadMore(String keyword, final SubscriberOnListener<List<GoodsListModel>> listener) {
        searchGoods(keyword, currentPage, listener);
    }

    private void searchGoods(String keyword, int page, final SubscriberOnListener<List<GoodsListModel>> listener) {
        cancel();

        subscription = service.search(CMD_ID, keyword, page + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<GoodsListDataModel>())
                .subscribe(new Subscriber<GoodsListDataModel>() {
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
                    public void onNext(GoodsListDataModel listDataModel) {
                        currentPage++;
                        if (listener != null) {
                            listener.onSucceed(listDataModel.data.get(0).data);
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
