package com.wavpayment.wavpay.ui.main.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.log.SpongeLogger;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;

/**
 * webView
 * Created by Administrator on 2018/1/20.
 */

public class OrderWebDelegate extends SpongeDelegate {
    private BridgeWebView mWebView;
    private String loadUrl;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_web;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        mWebView = $(R.id.webView);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new OrderWebViewClient(mWebView));
        mWebView.setDefaultHandler(new HandlerCallBack());
        mWebView.loadUrl(loadUrl);
        mWebView.registerHandler("isWavPay", (data, function) -> function.onCallBack("ok"));
        mWebView.registerHandler("getAuthCode", (data, function) ->
                Network.getInstance()
                        .get("authCode/userId.html")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                int code = JSON.parseObject(response.body()).getInteger("statusCode");
                                if (code == 200) {
                                    final String userId = JSON.parseObject(response.body()).getString("userId");
                                    function.onCallBack(userId);
                                } else {
                                    function.onCallBack("error");
                                    RxToast.showToast("error");
                                }
                            }
                        }));

        CallbackManager.getInstance()
                .addCallback(CallbackType.A_YARD_PASS, args -> {
                    SpongeLogger.e("", args.toString());
                    MerchantsDelegate merchants = new MerchantsDelegate();
                    merchants.setData(args.toString());
                    getSupportDelegate().pop();
                    getSupportDelegate().start(merchants);
                });
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    /**
     * 自定义的WebViewClient
     */
    private class OrderWebViewClient extends BridgeWebViewClient {
        OrderWebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }

    /**
     * js回调
     * 自定义回调
     */
   private class HandlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {
            if (function != null) {
                Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
