package com.hbyundu.shop.rest.api.goods;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class GoodsBaseAPI extends BaseAPI {


    protected GoodsService service;


    protected GoodsBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(GoodsService.class);
    }
}
