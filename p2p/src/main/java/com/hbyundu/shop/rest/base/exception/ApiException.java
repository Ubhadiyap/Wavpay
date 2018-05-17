package com.hbyundu.shop.rest.base.exception;

/**
 * Created by apple on 16/12/7.
 */
public class ApiException extends Exception {

    private String errorMessage;

    public ApiException(Throwable throwable, String errorMessage) {
        super(throwable);
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
