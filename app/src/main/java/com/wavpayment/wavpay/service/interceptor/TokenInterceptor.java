package com.wavpayment.wavpay.service.interceptor;

import com.lan.sponge.config.Sponge;
import com.vondear.rxtools.RxSPTool;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor extends BaseInterceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        final String token = RxSPTool.getString(Sponge.getAppContext(),"token");
        if (token==null||token.length()==0){
            return chain.proceed(originalRequest);
        }
        Request authorised = originalRequest.newBuilder()
                .header("token",token)
                .build();
        return chain.proceed(authorised);
    }
}
