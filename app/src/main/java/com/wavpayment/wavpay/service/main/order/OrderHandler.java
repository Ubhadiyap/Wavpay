package com.wavpayment.wavpay.service.main.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lan.sponge.util.log.SpongeLogger;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.utils.DateUtils;
import com.wavpayment.wavpay.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.wavpayment.wavpay.service.net.NetUrl.AUTHORIZATION;
import static com.wavpayment.wavpay.service.net.NetUrl.UNIFIEDORDER;

public class OrderHandler {
    private String DEVICE;
    private String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);

    private OrderHandler() {
        DEVICE = RxDeviceTool.getDeviceIdIMEI(Sponge.getAppContext());
    }

    private static class Holder {
        private static final OrderHandler INSTANCE = new OrderHandler();
    }

    public static OrderHandler getInstance() {
        return OrderHandler.Holder.INSTANCE;
    }

    /**
     * 验证支付密码
     *
     * @param pass
     */
    public void vPayPass(String pass) {
        Map<String, String> map = new HashMap<>();
        map.put("deviceCode", DEVICE);
        map.put("password", pass);
        Network.getInstance()
                .post(AUTHORIZATION,map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLogger.e("pay", response.body());
                        if (Utils.isJSONValid(response.body())) {
                            final JSONObject object = JSON.parseObject(response.body());
                            final int code = object.getInteger("statusCode");
                            listener.onSuccess(code);
                        }
                    }
                });
    }

    /**
     * 支付
     *
     * @param params
     */
    public void pay(Map<String, String> params) {
        Network.getInstance()
                .post(UNIFIEDORDER,params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            final JSONObject object = JSON.parseObject(response.body());
                            final int code = object.getInteger("statusCode");
                            String msg = null;
                            switch (code) {
                                case 200:
                                    final String orderId = object.getString("orderId");
                                    IGlobalCallback<String> callbackStr = CallbackManager.getInstance().getCallback(CallbackType.PAY);
                                    callbackStr.executeCallback(orderId);
                                    msg = "Pay for success";
                                    break;
                                case 110:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_110);
                                    break;
                                case 111:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_111);
                                    break;
                                case 112:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_112);
                                    break;
                                case 102:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_102);
                                    break;
                                case 118:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_118);
                                    break;
                                case 114:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_114);
                                    break;
                                case 109:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_109);
                                    break;
                                case 108:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_108);
                                    break;
                                case 103:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_103);
                                    break;
                                case 104:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_104);
                                    break;
                                case 119:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_119);
                                    break;
                                case 500:
                                    msg = Sponge.getAppContext().getString(R.string.p_err_500);
                                    break;
                            }
                            if (msg!=null) {
                                RxToast.showToast(msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        SpongeLoader.stopLoading();
                    }
                });
    }

    /**
     * 收款用户
     *
     * @param account
     */
    public OrderHandler vUser(String account) {
        Network.getInstance()
                .get("appUserHome/user/qrCode/" + account + ".html")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            orderListener.onSuccess(response.body());
                        }
                    }
                });
        return this;
    }

    private OrderHandlerListener listener;

    public void setListener(OrderHandlerListener listener) {
        this.listener = listener;
    }

    public interface OrderHandlerListener {
        void onSuccess(int code);
    }


    private OrderListener orderListener;

    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }

    public interface OrderListener {
        void onSuccess(String data);
    }


}
