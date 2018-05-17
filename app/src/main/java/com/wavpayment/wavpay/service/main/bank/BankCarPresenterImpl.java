package com.wavpayment.wavpay.service.main.bank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;

/**
 * 银行卡
 * Created by Administrator on 2017/12/27.
 */

public class BankCarPresenterImpl implements IBankContract.Presenter {

    private final IBankContract.View VIEW;

    public BankCarPresenterImpl(IBankContract.View view) {
        VIEW = view;
    }

    @Override
    public void cardList() {
        Network.getInstance()
                .get(NetUrl.CARDS)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final int code = object.getInteger("statusCode");
                        if (code == 200) {
                            VIEW.onCardList(response.body());
                        } else {
                            VIEW.error(code);
                        }
                    }
                });
    }

}
