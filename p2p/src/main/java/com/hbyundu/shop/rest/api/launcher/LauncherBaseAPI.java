package com.hbyundu.shop.rest.api.launcher;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class LauncherBaseAPI extends BaseAPI {


    protected LauncherService service;


    protected LauncherBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(LauncherService.class);
    }
}
