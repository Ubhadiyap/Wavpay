package com.wavpayment.wavpay.push;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lan.sponge.util.log.SpongeLogger;
import com.wavpayment.wavpay.app.WavPayActivity;

import java.util.HashMap;
import java.util.Map;


public class PushReceiver extends MessageReceiver {
    IGlobalCallback<String> callback;
    IGlobalCallback<HashMap<String,String>> downLineCallback;
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        //推送通知处理
        IGlobalCallback<Integer> callbackInt = CallbackManager.getInstance().getCallback(CallbackType.INFO);
        callbackInt.executeCallback(200);

        //强制下线推送
        final String event_type = extraMap.get("event_type");
        if (event_type != null) {
            if (event_type.equals("OFF_LINE_REMIND")) {
                Intent intent = new Intent(context,WavPayActivity.class);
                HashMap<String,String> dataMap = new HashMap<>();
                dataMap.put("account",extraMap.get("account"));
                dataMap.put("current_date",extraMap.get("current_date"));
                context.startActivity(intent);
                downLineCallback = CallbackManager.getInstance().getCallback(CallbackType.DOWN_LINE);
                downLineCallback.executeCallback(dataMap);
            }
        }
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        SpongeLogger.e("PushReceiver",cPushMessage.getMessageId()+","+cPushMessage.getContent());
        //推送消息
        final int tradeType = JSON.parseObject(cPushMessage.getContent()).getIntValue("tradeType");
        if (tradeType==1){
            callback = CallbackManager.getInstance().getCallback(CallbackType.ACCEPT_PUSH);
            callback.executeCallback(cPushMessage.getContent());
        }else if (tradeType==4){
            callback = CallbackManager.getInstance().getCallback(CallbackType.PAY_PUSH);
            callback.executeCallback(cPushMessage.getContent());
        }if (tradeType==-1){
            final int count=JSON.parseObject(cPushMessage.getContent()).getIntValue("count");
            callback =  CallbackManager.getInstance().getCallback(CallbackType.MESSAGE_PUSH);
            callback.executeCallback(String.valueOf(count));
        }if (tradeType==-2){
            callback = CallbackManager.getInstance().getCallback(CallbackType.A_YARD_PASS);
            callback.executeCallback(cPushMessage.getContent());
        }if (tradeType==-3){
            callback = CallbackManager.getInstance().getCallback(CallbackType.PAY_PAY);
            callback.executeCallback(cPushMessage.getContent());
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        //点击跳转
        final JSONObject data = JSON.parseObject(extraMap);
        final String orderId = data.getString("orderId");
        final int payOrReceive = data.getIntValue("payOrReceive");
        if (payOrReceive==1){//付款
            callback = CallbackManager.getInstance().getCallback(CallbackType.INFORM_PAY);
            callback.executeCallback(orderId);
        }else if (payOrReceive==2){//收款
            callback = CallbackManager.getInstance().getCallback(CallbackType.INFORM_ACCEPT);
            callback.executeCallback(orderId);
        }

        //强制下线推送
        final String event_type = data.getString("event_type");
        if (event_type != null) {
            if (event_type.equals("OFF_LINE_REMIND")) {
                Intent intent = new Intent(context,WavPayActivity.class);
                HashMap<String,String> dataMap = new HashMap<>();
                dataMap.put("account",data.getString("account"));
                dataMap.put("current_date",data.getString("current_date"));
                context.startActivity(intent);
                downLineCallback = CallbackManager.getInstance().getCallback(CallbackType.DOWN_LINE);
                downLineCallback.executeCallback(dataMap);
            }
        }
    }

}
