package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RepayBaseAPI extends BaseAPI {


    protected RepayService service;


    protected RepayBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(RepayService.class);
    }
}
