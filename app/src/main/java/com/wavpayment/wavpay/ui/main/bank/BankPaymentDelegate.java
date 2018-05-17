package com.wavpayment.wavpay.ui.main.bank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lan.sponge.widget.PwdEditText;
import com.vondear.rxtools.RxKeyboardTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.service.main.order.OrderHandler;
import com.wavpayment.wavpay.ui.left.security.SPaymentDelegate;
import com.wavpayment.wavpay.utils.RSAUtils;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;

/**
 * 添加删除银行卡密码验证
 * Created by Administrator on 2017/12/27.
 */

public class BankPaymentDelegate extends SpongeDelegate {
    private String key = null;
    private PwdEditText pwd;
    private String phone;

    private boolean isDel = true;
    private String bankId = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_payment;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.iv_back).setOnClickListener(view -> {
            HideKeyboard(pwd);
            getSupportDelegate().pop();
        });
        $(R.id.tv_forget).setOnClickListener(v -> {
            HideKeyboard(pwd);
            getSupportDelegate().start(new SPaymentDelegate());
        });
        pwd = $(R.id.pe_payment);
        pwd.setOnInputFinishListener(password -> {
            HideKeyboard(pwd);
            final String pass = RSAUtils.encryptByte(key, password);
            SpongeLoader.showLoading(_mActivity);
            OrderHandler.getInstance().vPayPass(pass);
        });
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        phone = data.getString("account");//设置当前账号
        callback();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        CommonHandler.getInstance().key(phone);
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }

    private void callback() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.KEY, args -> key = (String) args)
                .addCallback(CallbackType.CARD_DELETE, args -> {
                    SpongeLoader.stopLoading();
                    IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.CARD);
                    callback.executeCallback("ok");
                    getSupportDelegate().pop();
                });

        OrderHandler.getInstance().setListener(code->{
            if (!isDel && code == 200) {
                //删除银行卡
                CommonHandler.getInstance().delCard(bankId);
            } else {
                SpongeLoader.stopLoading();
                switch (code) {
                    case 200:
                        //下个界面
                        getSupportDelegate().startWithPop(new CardDelegate());
                        break;
                    case 115:
                        getSupportDelegate().start(new SPaymentDelegate());
                        break;
                    case 116:
                        RxToast.showToast(getString(R.string.p_err_116));
                        break;
                    case 424:
                        break;
                    case 423:
                        break;
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        RxKeyboardTool.showSoftInput(_mActivity, pwd);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxKeyboardTool.hideSoftInput(_mActivity);
    }

}
