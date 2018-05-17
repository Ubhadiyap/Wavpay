package com.wavpayment.wavpay.ui.main.order;

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
import com.wavpayment.wavpay.ui.main.balance.BalanceDelegate;
import com.wavpayment.wavpay.utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Administrator on 2017/12/27.
 */

public class TranAccDelegate extends SpongeDelegate {

    private String orderId = null;
    private boolean isRead =false;
    @Override
    public Object setLayout() {
        return R.layout.delegate_tran_acc;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        $(R.id.btn_balance).setOnClickListener(view -> getSupportDelegate().startWithPop(new BalanceDelegate()));
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
        Map<String, String> map = new HashMap<>();
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
                            final String payAccount = bill.getString("payAccount");
                            final int id = bill.getIntValue("id");
                            final String tradeTime = DateUtils.getDateToString(trade);
                            ((TextView) $(R.id.tv_time)).setText(tradeTime);
                            ((TextView) $(R.id.tv_amount)).setText("RM "+String.valueOf(collectAmount));
                            ((TextView) $(R.id.tv_name)).setText(String.valueOf(orderId));//订单号
                            ((TextView) $(R.id.tv_type)).setText(payAccount);
                            if (isRead){
                                netMsg(id);
                            }
                        }
                    }
                });

    }

    public void setRead(boolean read) {
        isRead = read;
    }

    private void netMsg(int id){
        Map<String, String> map = new HashMap<>();
        map.put("billId", String.valueOf(id));
        Network.getInstance().post(NetUrl.UPDATEBILLREADSTATUS,map);
    }
}
