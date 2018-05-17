package com.hbyundu.shop.rest.api.category;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.RetrofitClient;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public class CategoryBaseAPI extends BaseAPI {


    protected CategoryService service;


    protected CategoryBaseAPI() {
        super();

        service = RetrofitClient.getRetrofit().create(CategoryService.class);
    }
}
