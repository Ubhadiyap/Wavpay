package com.hbyundu.shop.rest.api.launcher;

import com.hbyundu.shop.rest.model.launcher.FindPwdResultModel;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;
import com.hbyundu.shop.rest.model.launcher.SignUpResultModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface LauncherService {

    @GET("storeclient/store/store.do")
    Observable<LoginResultModel> login(@Query("CmdId") String cmdId, @Query("username") String username, @Query("password") String password);

    @GET("storeclient/store/store.do")
    Observable<SignUpResultModel> signUp(@Query("CmdId") String cmdId, @Query("username") String username, @Query("userpwd") String password, @Query("mobile") String mobile);

    @GET("storeclient/store/store.do")
    Observable<FindPwdResultModel> findPwd(@Query("CmdId") String cmdId, @Query("username") String username, @Query("mobile") String mobile, @Query("newPwd") String newPwd);

    @GET("storeclient/store/store.do")
    Observable<LoginResultModel> autoLogin(@Query("CmdId") String cmdId, @Query("mobile") String mobile);
}
