package com.hbyundu.shop.rest.api.recommend;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.recommend.RecommendCategoryDataModel;
import com.hbyundu.shop.rest.model.recommend.RecommendCategoryModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RecommendCategoryAPI extends RecommendBaseAPI {

    private static final String CMD_ID = "00003";

    private RecommendCategoryAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RecommendCategoryAPI INSTANCE = new RecommendCategoryAPI();
    }

    public static RecommendCategoryAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void category(final SubscriberOnListener<List<RecommendCategoryModel>> listener) {
        cancel();

        subscription = service.category(CMD_ID)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<RecommendCategoryDataModel>())
                .subscribe(new Subscriber<RecommendCategoryDataModel>() {
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
                    public void onNext(RecommendCategoryDataModel categoryModel) {
                        if (listener != null) {
                            listener.onSucceed(categoryModel.data);
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
