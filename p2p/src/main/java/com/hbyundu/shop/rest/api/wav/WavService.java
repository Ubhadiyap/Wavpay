package com.hbyundu.shop.rest.api.wav;

import com.hbyundu.shop.rest.model.wav.WavBelowModel;
import com.hbyundu.shop.rest.model.wav.WavKeyModel;
import com.hbyundu.shop.rest.model.wav.WavResultModel;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by apple on 2018/3/6.
 */

public interface WavService {

    @POST("appUserLogin/key.html")
    Observable<WavKeyModel> key(@Query("account") String account, @Query("deviceCode") String deviceCode);

    @GET("appUserLogin/accounts.html")
    Observable<WavResultModel> checkAccount(@Query("account") String account);

    @POST("appUserLogin/register.html")
    Observable<WavResultModel> register(@Query("account") String account, @Query("password") String password,
                                        @Query("payPassword") String payPassword, @Query("deviceCode") String deviceCode);

    @POST("appUserHome/password.html")
    Observable<WavResultModel> resetPassword(@Query("account") String account, @Query("password") String password,
                                             @Query("code") String code, @Query("deviceCode") String deviceCode, @Query("type") int type);

    @POST("codes/code.html")
    Observable<WavResultModel> getCode(@Query("account") String account);

    @POST("appUserLogin/verifyCodeForRegister.html")
    Observable<WavResultModel> verifyCodeForRegister(@Query("account") String account, @Query("code") String code);

    @POST("debitCredit/below.html")
    Observable<WavBelowModel> below(@Query("orderId") String orderId, @Query("amount") String amount);
}
