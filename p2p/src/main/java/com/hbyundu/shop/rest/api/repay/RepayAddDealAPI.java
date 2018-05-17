package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.observable.HttpResponseFunc;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.DealAddResultModel;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RepayAddDealAPI extends RepayBaseAPI {

    public int repayTime;

    public String dealyongtu;

    public int choubiaoqixian;

    private List<File> files;

    private RepayAddDealAPI() {
        super();
    }

    private static class SingletonHolder {
        private static final RepayAddDealAPI INSTANCE = new RepayAddDealAPI();
    }

    public static RepayAddDealAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addPhotos(List<File> files) {
        this.files = files;

        if (files != null) {

        }
    }

    public void addDeal(long userId, String title, double total, double rate, String description, final SubscriberOnListener<Long> listener) {
        cancel();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (files != null && files.size() > 0) {
            for (File file : files) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                builder.addFormDataPart("file", file.getName(), requestBody);
            }
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), "");
            builder.addFormDataPart("empty", "empty", requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        subscription = service.addDeal(userId, title, total, rate, repayTime, description, dealyongtu, choubiaoqixian, multipartBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<DealAddResultModel>())
                .subscribe(new Subscriber<DealAddResultModel>() {
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
                    public void onNext(DealAddResultModel dealAddResultModel) {
                        if (dealAddResultModel.result == 1) {
                            if (listener != null) {
                                listener.onSucceed(dealAddResultModel.dealid);
                            }
                        } else {
                            if (listener != null) {
                                listener.onError(null);
                            }
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
