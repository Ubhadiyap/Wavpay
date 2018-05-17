package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class OrderBaseAPI extends BaseAPI {


    protected OrderService service;


    protected OrderBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(OrderService.class);
    }
}
