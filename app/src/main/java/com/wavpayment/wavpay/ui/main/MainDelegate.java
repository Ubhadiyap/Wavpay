package com.wavpayment.wavpay.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.app.WavPayApp;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.test.Test;
import com.wavpayment.wavpay.ui.main.order.TranAccDelegate;
import com.wavpayment.wavpay.ui.main.order.TranPayDelegate;
import com.wavpayment.wavpay.widgte.scan.QRScanDelegate;

import java.util.HashMap;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


public class MainDelegate extends BaseBottomDelegate {

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        if (response != null&&response.contains("user")) {
            final JSONObject data = JSON.parseObject(response).getJSONObject("user");
            final String photo = data.getString("headImg");
            final String nick = data.getString("nickName");
            final String phone = data.getString("account");
            setTitle(nick);
            setNavName(nick);
            setNavImg(photo);
            setNavPhone(phone);
            CommonHandler.getInstance().pushDevId(WavPayApp.getPushService().getDeviceId());
        }
        //全局回调
        CallbackManager.getInstance()
                .addCallback(CallbackType.INFO, args -> CommonHandler.getInstance().info())
                .addCallback(CallbackType.INFO_CALL, args -> {
                    final JSONObject user = JSON.parseObject(args.toString()).getJSONObject("user");
                    final String nickname = user.getString("nickName");
                    final String photo = user.getString("headImg");
                    final String phone = user.getString("account");
                    final String balance = user.getString("balance");
                    setTitle(nickname);
                    setNavName(nickname);
                    setNavPhone(phone);
                    setNavImg(photo);
                    IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.BALANCE);
                    callback.executeCallback(balance);
                    CommonHandler.getInstance().upDateApp(_mActivity);
                })
                .addCallback(CallbackType.INFORM_PAY, args -> {
                    final TranPayDelegate tp = new TranPayDelegate();
                    tp.setOrderId(args.toString());
                    tp.setRead(true);
                    getSupportDelegate().start(tp);
                })
                .addCallback(CallbackType.INFORM_ACCEPT, args -> {
                    final TranAccDelegate ta = new TranAccDelegate();
                    ta.setOrderId(args.toString());
                    ta.setRead(true);
                    getSupportDelegate().start(ta);
                }).addCallback(CallbackType.DOWN_LINE,args -> {
                    HashMap<String,String> dataMap = (HashMap<String, String>) args;

                    Intent intent = new Intent("com.wavpayment.wavpay.FORCE_OFFLINE");
                    intent.putExtra("account", dataMap.get("account"));
                    intent.putExtra("current_date",dataMap.get("current_date"));
                    getContext().sendBroadcast(intent);
                });
    }

    @Override
    protected int setIndexDelegate() {
        return 0;
    }


    @Override
    protected void onScan() {
        getSupportDelegate().start(new QRScanDelegate());
    }

    @Override
    protected void onLoginOut() {
        CommonHandler.getInstance().loginOut(this);
    }

}
