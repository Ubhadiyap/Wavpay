package com.wavpayment.wavpay.ui.main.balance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现
 * Created by Administrator on 2017/12/30.
 */

public class WithdrawPayDelegate extends SpongeDelegate {
    private String orderId = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_b_withdraw;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        $(R.id.btn_balance).setOnClickListener(view -> {
            getSupportDelegate().pop();
            getSupportDelegate().start(new BalanceDelegate());
        });
        IGlobalCallback<Integer> callbackInt = CallbackManager.getInstance().getCallback(CallbackType.INFO);
        callbackInt.executeCallback(200);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        netOrderId();
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private void netOrderId() {
        if (orderId != null) {
            Map<String,String> map = new HashMap<>();
            map.put("orderId", orderId);
            Network.getInstance()
                    .get(NetUrl.QUERYBILLSBYID,map)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            final JSONObject data = JSON.parseObject(response.body());
                            final int code = data.getIntValue("statusCode");
                            if (code == 200) {
                                final JSONObject bill = data.getJSONObject("bill");
                                final double collectAmount = bill.getDoubleValue("collectAmount");
                                final long trade = bill.getLongValue("tradeTime");
                                final long create = bill.getLongValue("createTime");
                                final String tradeTime = DateUtils.getDateToString(trade);
                                final String createTime = DateUtils.getDateToString(create);
                                ((TextView) $(R.id.tv_time)).setText(tradeTime);
                                ((TextView) $(R.id.tv_coll_time)).setText(createTime);
                                ((TextView) $(R.id.tv_amount)).setText("RM "+String.valueOf(collectAmount));
                            }
                        }
                    });
        }

    }
}
