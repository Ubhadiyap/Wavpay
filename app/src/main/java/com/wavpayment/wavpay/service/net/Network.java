package com.wavpayment.wavpay.service.net;

import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.util.Map;

/**
 * 网络请求
 * Created by Administrator on 2018/2/10.
 */

public class Network {

    private String baseUrl = null;

    private Network() {
        baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
    }

    private static class Holder {
        private static final Network INSTANCE = new Network();
    }

    public static Network getInstance() {
        return Network.Holder.INSTANCE;
    }

    public Request<String, GetRequest<String>> get(String url, Map<String, String> params) {
        return OkGo.<String>get(baseUrl + url)
                .tag(this)
                .params(params);
    }

    public Request<String, GetRequest<String>> get(String url) {
        return OkGo.<String>get(baseUrl + url)
                .tag(this);
    }

    public Request<String, PostRequest<String>> post(String url, Map<String, String> params) {
        return OkGo.<String>post(baseUrl + url)
                .tag(this)
                .params(params);
    }
}