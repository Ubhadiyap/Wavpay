package com.hbyundu.shop.rest.base.observable;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by apple on 16/12/7.
 */
public class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
