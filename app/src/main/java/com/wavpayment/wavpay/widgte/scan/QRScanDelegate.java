package com.wavpayment.wavpay.widgte.scan;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.log.SpongeLogger;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxAnimationTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.order.MerchantsDelegate;
import com.wavpayment.wavpay.ui.main.order.NewOrderDelegate;
import com.wavpayment.wavpay.ui.main.order.OrderWebDelegate;
import com.wavpayment.wavpay.utils.Utils;

import static com.wavpayment.wavpay.ui.main.order.BaseOrderDelegate.COLLECTION;
import static com.wavpayment.wavpay.ui.main.order.BaseOrderDelegate.PAYMENT;
import static com.wavpayment.wavpay.ui.main.order.MerchantsDelegate.MER_PAY;

/**
 * 扫一扫
 * Created by Administrator on 2018/1/20.
 */

public class QRScanDelegate extends SpongeDelegate implements QRCodeReaderView.OnQRCodeReadListener {
    private QRCodeReaderView qrCodeReaderView;
    private boolean isLamp = false;
    private boolean isScan = true;

    @Override
    public Object setLayout() {
        return R.layout.delegate_scan;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.top_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.lamp).setOnClickListener(v -> {
            if (isLamp) {
                isLamp = false;
            } else {
                isLamp = true;
            }
            qrCodeReaderView.setTorchEnabled(isLamp);
        });
        qrCodeReaderView = $(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.setFrontCamera();
        qrCodeReaderView.setBackCamera();
        initScanAnimation();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (text.contains("/bills/QRCODECOLL/")) {
            network(text, COLLECTION);
        } else if (text.contains("/bills/QRCODEPAY/")) {
            network(text, PAYMENT);
        } else if (text.contains("/merchant/MERCHANTQRCODECOLL/")) {
            network(text, MER_PAY);
        } else if (text.contains("https://oneqr.wavpay.net/")) {//一码通
            OrderWebDelegate web = new OrderWebDelegate();
            web.setLoadUrl(text);
            getSupportDelegate().pop();
            getSupportDelegate().start(web);
        } else {
            RxToast.showToast(text);
        }
    }

    private void network(String url, int type) {
        if (isScan) {
            isScan = false;
            SpongeLoader.showLoading(_mActivity);
            OkGo.<String>get(url)
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            SpongeLoader.stopLoading();

                            if (Utils.isJSONValid(response.body())) {
                                final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                                if (code == 200) {
                                    if (type == MER_PAY) {
                                        MerchantsDelegate merchants = new MerchantsDelegate();
                                        merchants.setData(response.body());
                                        getSupportDelegate().pop();
                                        getSupportDelegate().start(merchants);
                                    } else {
                                        NewOrderDelegate order = new NewOrderDelegate();
                                        order.setData(type, response.body());
                                        getSupportDelegate().pop();
                                        getSupportDelegate().start(order);
                                    }
                                } else if (code == 113) {
                                    isScan = true;
                                    RxToast.showToast("The qr code expires");
                                } else if (code == 121) {
                                    isScan = true;
                                    RxToast.showToast(getString(R.string.p_err_114));
                                }
                            }
                        }
                    });
        }
    }

    private void initScanAnimation() {
        ImageView mQrLineView = $(com.vondear.rxtools.R.id.capture_scan_line);
        RxAnimationTool.ScaleUpDowm(mQrLineView);
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
