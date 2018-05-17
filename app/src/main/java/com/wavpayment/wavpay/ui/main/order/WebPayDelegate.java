package com.wavpayment.wavpay.ui.main.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;


/**
 *
 * Created by Administrator on 2017/12/30.
 */

public class WebPayDelegate extends SpongeDelegate {

    private BridgeWebView mWebView;

    private String url = null;
    private String title = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_web_pay;
    }



    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText(title);
        mWebView = $(R.id.webView);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new WebViewClient(mWebView));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (url!=null) {
            String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
            baseUrl += url.substring(1, url.length());
            mWebView.loadUrl(baseUrl);
        }
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 自定义的WebViewClient
     */
    private class WebViewClient extends BridgeWebViewClient {
        WebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }
}
