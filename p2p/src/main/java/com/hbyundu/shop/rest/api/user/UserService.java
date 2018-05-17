package com.hbyundu.shop.rest.api.user;

import com.hbyundu.shop.rest.model.user.ChangePwdResultModel;
import com.hbyundu.shop.rest.model.user.UserInfoModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface UserService {

    @GET("storeclient/store/store.do")
    Observable<ChangePwdResultModel> changePwd(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("original") String original, @Query("newPasswd") String newPasswd);

    @GET("storeclient/store/store.do")
    Observable<UserInfoModel> userInfo(@Query("CmdId") String cmdId, @Query("userid") long userId);
}
