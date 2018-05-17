package com.hbyundu.shop.rest.api.user;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class UserBaseAPI extends BaseAPI {


    protected UserService service;


    protected UserBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(UserService.class);
    }
}
