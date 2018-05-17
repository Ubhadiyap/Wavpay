package com.hbyundu.shop.rest.base.subscribers;

/**
 * Created by ywl on 2016/5/19.
 */
public interface SubscriberOnListener<T> {

    void onSucceed(T data);

    void onError(String msg);
}
