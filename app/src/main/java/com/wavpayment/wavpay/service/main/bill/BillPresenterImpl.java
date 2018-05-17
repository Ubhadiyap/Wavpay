package com.wavpayment.wavpay.service.main.bill;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;

import java.util.HashMap;
import java.util.Map;

/**
 * 账单列表
 * Created by Administrator on 2017/12/29.
 */

public class BillPresenterImpl implements IBillContract.Presenter {
    private final IBillContract.View VIEW;

    public BillPresenterImpl(IBillContract.View view) {
        VIEW = view;
    }

    @Override
    public void all(String... args) {
        Map<String,String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(args[0]));
        map.put("limit", String.valueOf(args[1]));
        map.put("tradeType", String.valueOf(args[2]));
        Network.getInstance()
                .post(NetUrl.QUERYBILLS,map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                        switch (code) {
                            case 200:
                                VIEW.success(response.body());
                                break;
                        }
                    }
                });
    }
}
