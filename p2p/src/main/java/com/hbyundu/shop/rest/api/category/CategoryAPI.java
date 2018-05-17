package com.hbyundu.shop.rest.api.category;

import android.text.TextUtils;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.category.CategoryModel;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;
import com.hbyundu.shop.vendor.encrypt.MD5;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class CategoryAPI extends CategoryBaseAPI {

    private static final String CMD_ID = "00008";

    private CategoryAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final CategoryAPI INSTANCE = new CategoryAPI();
    }

    public static CategoryAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void category(final SubscriberOnListener<CategoryModel> listener) {
        cancel();


        subscription = service.category(CMD_ID)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<CategoryModel>())
                .subscribe(new Subscriber<CategoryModel>() {
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
                    public void onNext(CategoryModel categoryModel) {

                        if (listener != null) {
                            listener.onSucceed(categoryModel);

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
