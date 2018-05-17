package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class InvestBaseAPI extends BaseAPI {


    protected InvestService service;


    protected InvestBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(InvestService.class);
    }
}
