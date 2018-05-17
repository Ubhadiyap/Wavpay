package com.wavpayment.wavpay.ui.main.balance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.bill.BillDelegate;
import com.wavpayment.wavpay.ui.main.order.WebPayDelegate;
import com.wavpayment.wavpay.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;

/**
 * 余额
 * Created by Administrator on 2017/11/25.
 */

public class BalanceDelegate extends SpongeDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_balance;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_right)).setText(getString(R.string.b_tran));
        ((TextView) $(R.id.tv_title)).setText(getString(R.string.b_balance));
        $(R.id.tv_right).setOnClickListener(view -> getSupportDelegate().start(new BillDelegate()));
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        if (response!=null) {
            final JSONObject data = JSON.parseObject(response).getJSONObject("user");
            final String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
            final String photo = baseUrl.substring(0, baseUrl.length() - 1) + data.getString("headImg");
            final String balance = data.getString("balance");
            ((TextView) $(R.id.tv_balance)).setText(getString(R.string.b_w) + balance);
            if (!photo.isEmpty()) {
                Glide.with(_mActivity)
                        .load(photo)
                        .apply(RECYCLER_OPTIONS)
                        .into((CircleImageView) $(R.id.cv_photo));
            }
        }
        $(R.id.btn_top).setOnClickListener(view -> {
            $(R.id.btn_top).setEnabled(false);
            $(R.id.btn_accept).setEnabled(false);
            networkTop();
        });
        $(R.id.btn_accept).setOnClickListener(view -> {
            $(R.id.btn_top).setEnabled(false);
            $(R.id.btn_accept).setEnabled(false);
            network();
        });

    }

    private void networkTop() {
        Network.getInstance()
                .get("fpxTrade/topUp.html")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getIntValue("statusCode");
                            if (code == 200) {
                                String url = JSON.parseObject(response.body()).getString("url");
                                WebPayDelegate web = new WebPayDelegate();
                                web.setUrl(url);
                                web.setTitle("Top-Up");
                                getSupportDelegate().pop();
                                getSupportDelegate().start(web);
                            }else {
                                $(R.id.btn_top).setEnabled(true);
                                $(R.id.btn_accept).setEnabled(true);
                            }
                        }
                    }
                });
    }

    private void network(){
        Map<String,String> map = new HashMap<>();
        Network.getInstance()
                .post("withdraw/toMyself.html",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getIntValue("statusCode");
                            if (code == 200) {
                                String url = JSON.parseObject(response.body()).getString("url");
                                WebPayDelegate web = new WebPayDelegate();
                                web.setUrl(url);
                                web.setTitle(getString(R.string.b_wit));
                                getSupportDelegate().pop();
                                getSupportDelegate().start(web);
                            }else {
                                $(R.id.btn_top).setEnabled(true);
                                $(R.id.btn_accept).setEnabled(true);
                            }
                        }
                    }
                });
    }
}
