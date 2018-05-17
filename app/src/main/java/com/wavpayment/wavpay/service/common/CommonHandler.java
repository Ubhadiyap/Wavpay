package com.wavpayment.wavpay.service.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.activity.ProxyActivity;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lan.sponge.util.log.SpongeLogger;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxBarCode;
import com.vondear.rxtools.view.RxQRCode;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.downline.DownlineWithLinkePasswordDelegate;
import com.wavpayment.wavpay.ui.login.AccountManager;
import com.wavpayment.wavpay.ui.login.LoginDelegate;
import com.wavpayment.wavpay.ui.main.MainDelegate;
import com.wavpayment.wavpay.ui.main.StartDelegate;
import com.wavpayment.wavpay.ui.register.GetCodeDelegate;
import com.wavpayment.wavpay.ui.register.LoginListener;
import com.wavpayment.wavpay.ui.register.RegisterDelegate;
import com.wavpayment.wavpay.ui.register.SuccessDelegate;
import com.wavpayment.wavpay.utils.RSAUtils;
import com.wavpayment.wavpay.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 公共接口
 * Created by Administrator on 2017/11/28.
 */
public class CommonHandler {
    public final static String PERSONAL = "personal";
    private IGlobalCallback<String> callbackStr = null;
    private IGlobalCallback<Integer> callbackInt = null;
    private String DEVICE = null;
    private String baseUrl = null;

    private CommonHandler() {
        DEVICE = RxDeviceTool.getDeviceIdIMEI(Sponge.getAppContext());
        baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
    }

    private static class Holder {
        private static final CommonHandler INSTANCE = new CommonHandler();
    }

    public static CommonHandler getInstance() {
        return CommonHandler.Holder.INSTANCE;
    }

    /**
     * 获取公密
     *
     * @param acc
     */
    public void key(String acc) {
        Map<String, String> map = new HashMap<>();
        map.put("account", acc);
        map.put("deviceCode", DEVICE);
        Network.getInstance()
                .post(NetUrl.KEY, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            final JSONObject object = JSON.parseObject(response.body());
                            final int code = object.getInteger("statusCode");
                            if (code == 200) {
                                final String key = object.getString("key");
                                callbackStr = CallbackManager.getInstance().getCallback(CallbackType.KEY);
                                callbackStr.executeCallback(key);
                            }
                        }
                    }
                });
    }

    /**
     * 上传推送设配ID
     *
     * @param devId
     */
    public void pushDevId(String devId) {
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", devId);
        map.put("clientType", "1");
        map.put("deviceName",Utils.getPhoneModel());
        Network.getInstance().post(NetUrl.SENDDEVICEID, map).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                SpongeLoader.stopLoading();
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    public void code(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("account", phone);
        Network.getInstance().post(NetUrl.CODE, map).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (Utils.isJSONValid(response.body())) {
                    int code = JSON.parseObject(response.body()).getInteger("statusCode");
                    String msg = null;
                    switch (code) {
                        case 101:
                            msg = Sponge.getAppContext().getString(R.string.code_101);
                            break;
                        case 102:
                            msg = Sponge.getAppContext().getString(R.string.code_102);
                            break;
                        case 200:
                            msg = Sponge.getAppContext().getString(R.string.code_200);
                            break;
                    }
                    if (msg != null)
                        RxToast.showToast(msg);
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
     * 找回,修改密码
     *
     * @param args
     */
    public void linked(String... args) {
        Map<String, String> map = new HashMap<>();
        map.put("account", args[0]);
        map.put("password", args[1]);
        map.put("code", args[2]);
        map.put("deviceCode", DEVICE);
        map.put("type", args[3]);
        Network.getInstance()
                .post(NetUrl.PASSWORD, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            switch (code) {
                                case 200:
                                    callbackStr = CallbackManager.getInstance().getCallback(CallbackType.LINKED);
                                    callbackStr.executeCallback(String.valueOf(code));
                                    break;
                                case 406:
                                    break;
                                case 404:
                                    RxToast.showToast("The user doesn't exist");
                                    break;
                                case 410:
                                    RxToast.showToast("Verification code is invalid or incorrect.");
                                    break;
                                case 400:
                                    break;
                                case 422:
                                    RxToast.showToast("The login password and the payment password are the same.");
                                    break;
                                case 423:
                                    break;
                                case 424:
                                    break;
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
     * 删除银行卡
     *
     * @param bankId
     */
    public void delCard(String bankId) {
        Map<String, String> map = new HashMap<>();
        map.put("bankId", bankId);
        Network.getInstance()
                .post(NetUrl.DELCARD, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getIntValue("statusCode");
                            if (code == 200) {
                                RxToast.showToast(Sponge.getAppContext().getString(R.string.del_s));
                                callbackInt = CallbackManager.getInstance().getCallback(CallbackType.CARD_DELETE);
                                callbackInt.executeCallback(200);
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
     * 获取用户信息
     */
    public void info() {
        Network.getInstance()
                .get(NetUrl.INFO)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            if (code == 200) {
                                RxSPTool.putJSONCache(Sponge.getAppContext(), PERSONAL, response.body());
                                callbackStr = CallbackManager.getInstance().getCallback(CallbackType.INFO_CALL);
                                callbackStr.executeCallback(response.body());
                            }
                        }
                    }
                });
    }

    /**
     * 更新用户头像
     *
     * @param path
     */
    public void update(String path) {
        final File file = new File(path);
        final Bitmap bm = RxImageTool.GetLocalOrNetBitmap(path);
        File crop = null;
        try {
            crop = Utils.saveFile(bm, file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpongeLoader.showLoading(Sponge.getConfiguration(ConfigKeys.ACTIVITY));
        OkGo.<String>post(baseUrl + NetUrl.PHOTO)
                .tag(this)
                .params("photo", crop)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        SpongeLogger.e("","body:"+response.body()+",code:"+response.code()+",message:"+response.message());
                        if (response.code() == 413) {
                            RxToast.error("The picture is too large to support the 2M size picture");
                        } else {
                            if (Utils.isJSONValid(response.body())) {
                                SpongeLoader.stopLoading();
                                final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                                final String msg = JSON.parseObject(response.body()).getString("msg");
                                if (code == 200) {
                                    RxToast.success("success");
                                    callbackInt = CallbackManager.getInstance().getCallback(CallbackType.INFO);
                                    callbackInt.executeCallback(200);
                                } else {
                                    RxToast.error(msg);
                                }
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
     * 更新用户信息
     *
     * @param key
     * @param value
     */
    public void update(String key, String value) {
        SpongeLoader.showLoading(Sponge.getConfiguration(ConfigKeys.ACTIVITY));
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        Network.getInstance()
                .post(NetUrl.UPDATE, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            if (code == 200) {
                                RxToast.success("success");
                                callbackInt = CallbackManager.getInstance().getCallback(CallbackType.INFO);
                                callbackInt.executeCallback(200);
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
     * 退出登录
     *
     * @param delegate
     */
    public void loginOut(MainDelegate delegate) {
        RxSPTool.putString(Sponge.getAppContext(), "token", "");
        AccountManager.setSignState(false);
        delegate.getSupportDelegate().pop();
        delegate.getSupportDelegate().start(new StartDelegate());
        Network.getInstance().get(NetUrl.LOGINOUT).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Intent intent = new Intent("com.hbyundu.shop.wav.CLEAR_USER");
                intent.setComponent(new ComponentName(delegate.getActivity().getPackageName(),
                        "com.hbyundu.shop.receiver.WavReceiver"));
                delegate.getActivity().sendBroadcast(intent);
            }
        });
    }




    /** 强制下线窗口弹出时就离线现有账户
     * @param proxyActivity
     */
    public void downLineWithPop(ProxyActivity proxyActivity){
        RxSPTool.putString(Sponge.getAppContext(), "token", "");
        AccountManager.setSignState(false);
        Network.getInstance().get(NetUrl.LOGINOUT).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Intent intent = new Intent("com.hbyundu.shop.wav.CLEAR_USER");
                intent.setComponent(new ComponentName(proxyActivity.getPackageName(),
                        "com.hbyundu.shop.receiver.WavReceiver"));
                proxyActivity.sendBroadcast(intent);
            }
        });
    }


    /** 强制下线
     * @param proxyActivity
     */
    public void downLine(ProxyActivity proxyActivity){
        proxyActivity.getSupportDelegate().pop();
        proxyActivity.getSupportDelegate().start(new LoginDelegate());
    }
    /** 强制下线并且更改密码
     * @param proxyActivity
     */
    public void downLineWithRestePassword(ProxyActivity proxyActivity){
        proxyActivity.getSupportDelegate().pop();
        proxyActivity.getSupportDelegate().start(new DownlineWithLinkePasswordDelegate());
    }




    /**
     * 登录
     *
     * @param listener
     * @param args
     */
    public void login(LoginListener listener, String... args) {
        final List<String> keys = RSAUtils.getkeys();
        Map<String, String> map = new HashMap<>();
        map.put("account", args[0]);
        map.put("password", args[1]);
        map.put("deviceCode", DEVICE);
        map.put("publicKey", keys.get(0));
        Network.getInstance()
                .post(NetUrl.LOGIN, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            final JSONObject da = JSON.parseObject(response.body());
                            final int code = da.getInteger("statusCode");
                            switch (code) {
                                case 200:
                                    try {
                                        //解密token,并存储
                                        final String sToken = da.getString("token");
                                        final String token = RSAUtils.encryptContent(keys.get(1), sToken);
                                        RxSPTool.putString(Sponge.getAppContext(), "token", token);
                                        //保持登陆状态
                                        AccountManager.setSignState(true);
                                        listener.onLogin();
                                        Intent intent = new Intent("com.hbyundu.shop.wav.SET_USER");
                                        intent.setComponent(new ComponentName(Sponge.getAppContext().getPackageName(),
                                                "com.hbyundu.shop.receiver.WavReceiver"));
                                        intent.putExtra("mobile", args[0]);
                                        Sponge.getAppContext().sendBroadcast(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 403:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.l_err_403));
                                    break;
                                case 400:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.l_err_400));
                                    break;
                                case 404:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.l_err_404));
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 验证号码是否被注册
     *
     * @param userName
     */
    public void accounts(SpongeDelegate delegate, String userName) {
        Map<String, String> map = new HashMap<>();
        map.put("account", userName);
        Network.getInstance()
                .get(NetUrl.ACCOUNTS, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            switch (code) {
                                case 200:
                                    code(userName);
                                    final GetCodeDelegate getCode = new GetCodeDelegate();
                                    getCode.setMobile(userName);
                                    delegate.getSupportDelegate().pop();
                                    delegate.getSupportDelegate().start(getCode);
                                    break;
                                case 403:
                                    callbackStr = CallbackManager.getInstance().getCallback(CallbackType.CHECK);
                                    callbackStr.executeCallback("200");
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_403));
                                    break;
                            }
                        }
                    }
                });
    }


    /**
     * 生成收款码
     *
     * @param ivQRCode
     * @param amount
     */
    public void accCode(ImageView ivQRCode, final String amount) {
        String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
        baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        String finalBaseUrl = baseUrl;
        GetRequest<String> request = OkGo.<String>get(baseUrl + "/bills/collectionQrCode.html").tag(this);
        if (!amount.isEmpty()) {
            request.params("amount", amount);
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                SpongeLoader.stopLoading();
                if (Utils.isJSONValid(response.body())) {
                    final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                    if (code == 200) {
                        final String url = finalBaseUrl + JSON.parseObject(response.body()).getString("url");
                        RxQRCode.builder(url).
                                backColor(Sponge.getAppContext().getResources().getColor(com.vondear.rxtools.R.color.white)).
                                codeColor(Sponge.getAppContext().getResources().getColor(com.vondear.rxtools.R.color.black)).
                                codeSide(700).
                                into(ivQRCode);
                    }
                }
            }
        });
    }

    /**
     * 生成付款码
     *
     * @param ivQRCode
     * @param cardId
     */
    public void payCode(ImageView ivQRCode, ImageView ivLinecode, String cardId) {
        String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
        baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        String finalBaseUrl = baseUrl;
        Map<String, String> map = new HashMap<>();
        map.put("payWays", cardId);
        Network.getInstance()
                .get(NetUrl.PAYMENTQRCODE, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            if (code == 200) {
                                final String url = finalBaseUrl + JSON.parseObject(response.body()).getString("url");
                                //条形码生成方式一  推荐此方法
                                RxBarCode.builder(url).
                                        backColor(Sponge.getAppContext().getResources().getColor(com.vondear.rxtools.R.color.transparent)).
                                        codeColor(Sponge.getAppContext().getResources().getColor(com.vondear.rxtools.R.color.black)).
                                        codeWidth(1000).
                                        codeHeight(300).
                                        into(ivLinecode);
                                RxQRCode.builder(url).
                                        backColor(Sponge.getAppContext().getResources().getColor(com.vondear.rxtools.R.color.white)).
                                        codeColor(Sponge.getAppContext().getResources().getColor(com.vondear.rxtools.R.color.black)).
                                        codeSide(800).
                                        into(ivQRCode);
                            }
                        }
                    }
                });
    }

    /**
     * 注册
     *
     * @param delegate
     * @param args
     */

    public void register(RegisterDelegate delegate, String... args) {

        Map<String, String> map = new HashMap<>();
        map.put("account", args[0]);
        map.put("password", args[1]);
        map.put("payPassword", args[2]);
        map.put("deviceCode", DEVICE);
        Network.getInstance()
                .post(NetUrl.REGISTER, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            switch (code) {
                                case 200:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.r_success));
                                    final SuccessDelegate success = new SuccessDelegate();
                                    success.setMobile(args[0]);
                                    success.setPassword(args[1]);
                                    delegate.getSupportDelegate().pop();
                                    delegate.getSupportDelegate().start(success);
                                    break;
                                case 500:
                                    break;
                                case 403:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_403));
                                    break;
                                case 410:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_410));
                                    break;
                                case 404:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_404));
                                    break;
                                case 423:
                                    break;
                                case 424:
                                    break;
                                case 426:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_426));
                                    break;
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
     * 校验银行卡和姓名是否匹配
     *
     * @param args
     */
    public void verifyName(String... args) {
        Map<String, String> map = new HashMap<>();
        map.put("bankCode", args[0]);
        map.put("realName", args[1]);
        map.put("cardNumber", args[2]);
        Network.getInstance()
                .post(NetUrl.VERIFYCARDANDNAME, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final int code = object.getInteger("statusCode");
                        if (code == 200) {
                            IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.V_CARD);
                            callback.executeCallback(response.body());
                        } else if (code == 118) {
                            RxToast.showToast("Bank card number error");
                        } else if (code == 120) {
                            RxToast.showToast("The cardholder's name is wrong");
                        }
                    }
                });
    }

    /**
     * 注册验证码验证
     *
     * @param mobile
     * @param code
     */
    public void verifyCode(GetCodeDelegate delegate, String mobile, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("account", mobile);
        map.put("code", code);
        Network.getInstance()
                .post(NetUrl.VERIFYCODEFORREGISTER, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpongeLoader.stopLoading();
                        if (Utils.isJSONValid(response.body())) {
                            int c = JSON.parseObject(response.body()).getInteger("statusCode");
                            if (c == 200) {
                                final RegisterDelegate security = new RegisterDelegate();
                                security.setMobile(mobile);
                                delegate.getSupportDelegate().pop();
                                delegate.getSupportDelegate().start(security);
                            } else if (c == 410) {
                                RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_410));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        SpongeLogger.e("", response.code() + "," + response.message() + "," + response.body());
                    }
                });
    }

    /**
     * 更新app版本
     */
    public void upDateApp(Context context) {
        String version = RxAppTool.getAppVersionName(Sponge.getAppContext());
        Map<String, String> map = new HashMap<>();
        map.put("account", version);
        map.put("clientType", "1");
        Network.getInstance()
                .post(NetUrl.APPVERSION, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            switch (code) {
                                case 200:
                                    final String downloadUrl = JSON.parseObject(response.body()).getString("downloadUrl");
                                    final String ver = context.getString(R.string.update_found) + JSON.parseObject(response.body()).getString("version") + context.getString(R.string.update_judge) + "?";
                                    if (!downloadUrl.isEmpty()) {
                                        final String url = downloadUrl.substring(1, downloadUrl.length());
                                        ShowDialog(context, ver, url);
                                    }
                                    break;
                                case 500:
                                    RxToast.showToast(Sponge.getAppContext().getString(R.string.p_err_500));
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 显示更新提示弹窗
     * @param context
     * @param strAppVersionName
     * @param apk_down_url
     */
    private void ShowDialog(Context context, String strAppVersionName, String apk_down_url) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(context);//提示弹窗
        rxDialogSureCancel.getContentView().setText(strAppVersionName);
        rxDialogSureCancel.setTitle(context.getString(R.string.update_title));
        rxDialogSureCancel.getSureView().setOnClickListener(v -> {
            RxToast.showToast(context.getString(R.string.update_loading));
            OkGo.<File>get(baseUrl + apk_down_url)
                    .tag(this)
                    .execute(new FileCallback("wavpay.apk") {
                        @Override
                        public void onSuccess(Response<File> response) {
                            RxToast.showToast(context.getString(R.string.update_success));
                            RxAppTool.InstallAPK(context, response.body().getPath());
                        }
                    });
            rxDialogSureCancel.cancel();
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(v -> {
            RxToast.showToast(context.getString(R.string.update_cancel));
            rxDialogSureCancel.cancel();
        });
        rxDialogSureCancel.show();
    }
}
