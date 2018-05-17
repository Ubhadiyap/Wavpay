package com.wavpayment.wavpay.ui.main.bank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加银行卡
 * Created by Administrator on 2017/12/28.
 */

public class AddCardDelegate extends SpongeDelegate {

    private String type;
    private String realName;
    private String cardNumber;

    @Override
    public Object setLayout() {
        return R.layout.delegate_add_card;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText("Name on Card");
        $(R.id.btn_next).setOnClickListener(view -> {
            realName = ((TextView) $(R.id.et_one)).getText().toString();
            cardNumber = ((TextView) $(R.id.et_two)).getText().toString();
            if (!realName.isEmpty()&&!cardNumber.isEmpty()) {
                network();
            }else {
                RxToast.showToast("Please fill in the complete information");
            }
        });
    }


    private void network() {
        SpongeLoader.showLoading(_mActivity);
        Map<String, String> map = new HashMap<>();
        map.put("bankCode", type);
        map.put("realName", realName);
        map.put("cardNumber",cardNumber);
        Network.getInstance()
                .post(NetUrl.ADDCARD,map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        int code = JSON.parseObject(response.body()).getInteger("statusCode");
                        switch (code){
                            case 200:
                                IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.CARD);
                                callback.executeCallback("ok");
                                getSupportDelegate().pop();
                                break;
                            case 427:
                                RxToast.showToast("The bank has been bound");
                                break;
                        }
                    }
                });
    }

    public void setType(String type) {
        this.type = type;
    }

}
