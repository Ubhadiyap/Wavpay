package com.hbyundu.shop.rest.api.recommend;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.recommend.RecommendGoodsDataModel;
import com.hbyundu.shop.rest.model.recommend.RecommendGoodsModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RecommendGoodsAPI extends RecommendBaseAPI {

    private static final String CMD_ID = "00004";

    private RecommendGoodsAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RecommendGoodsAPI INSTANCE = new RecommendGoodsAPI();
    }

    public static RecommendGoodsAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void goods(int categoryId, final SubscriberOnListener<List<RecommendGoodsModel>> listener) {
        cancel();

        subscription = service.goods(CMD_ID, categoryId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RecommendGoodsDataModel>())
                .subscribe(new Subscriber<RecommendGoodsDataModel>() {
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
                    public void onNext(RecommendGoodsDataModel goodsDataModel) {
                        if (listener != null) {
                            listener.onSucceed(goodsDataModel.data);
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
