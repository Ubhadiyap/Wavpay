package com.hbyundu.shop.rest.api.recommend;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class RecommendBaseAPI extends BaseAPI {


    protected RecommendService service;


    protected RecommendBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(RecommendService.class);
    }
}
