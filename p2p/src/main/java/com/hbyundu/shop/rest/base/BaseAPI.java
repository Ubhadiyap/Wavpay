package com.hbyundu.shop.rest.base;

import rx.Subscription;

/**
 * Created by apple on 16/3/9.
 */
public class BaseAPI {

    protected Subscription subscription;

    public void cancel() {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
