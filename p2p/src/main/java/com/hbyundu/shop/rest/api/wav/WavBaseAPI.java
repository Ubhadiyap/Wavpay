package com.hbyundu.shop.rest.api.wav;

import com.hbyundu.shop.rest.base.BaseAPI;
import com.hbyundu.shop.rest.base.WavClient;

/**
 * Created by apple on 2018/3/6.
 */

public class WavBaseAPI extends BaseAPI {

    protected WavService service;

    protected WavBaseAPI() {
        super();

        service = WavClient.getRetrofit().create(WavService.class);
    }
}
