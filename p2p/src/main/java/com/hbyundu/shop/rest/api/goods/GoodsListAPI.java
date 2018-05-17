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

public class GoodsListAPI extends GoodsBaseAPI {

    private int currentPage;

    private static final String CMD_ID = "00005";

    private GoodsListAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final GoodsListAPI INSTANCE = new GoodsListAPI();
    }

    public static GoodsListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refresh(long categoryId, final SubscriberOnListener<List<GoodsListModel>> listener) {
        currentPage = 0;
        categoryGoods(categoryId, currentPage, listener);
    }

    public void loadMore(long categoryId, final SubscriberOnListener<List<GoodsListModel>> listener) {
        categoryGoods(categoryId, currentPage, listener);
    }

    private void categoryGoods(long categoryId, int page, final SubscriberOnListener<List<GoodsListModel>> listener) {
        cancel();

        subscription = service.categoryGoods(CMD_ID, categoryId, page + 1)
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
