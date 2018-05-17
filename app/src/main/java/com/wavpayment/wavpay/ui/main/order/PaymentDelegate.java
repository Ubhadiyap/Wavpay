package com.wavpayment.wavpay.ui.main.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.log.SpongeLogger;
import com.lan.sponge.widget.PwdEditText;
import com.vondear.rxtools.RxKeyboardTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.service.main.order.OrderHandler;
import com.wavpayment.wavpay.ui.left.security.SPaymentDelegate;
import com.wavpayment.wavpay.ui.main.balance.TopAccDelegate;
import com.wavpayment.wavpay.ui.main.balance.WithdrawPayDelegate;
import com.wavpayment.wavpay.utils.RSAUtils;

import java.util.Map;

/**
 * 密码界面
 * Created by Administrator on 2017/12/27.
 */


public class PaymentDelegate extends SpongeDelegate {
    private String key = null;
    private PwdEditText pwd;
    private Map<String, String> params;
    private String phone;
    private int type;//判断转账，充值


    @Override
    public Object setLayout() {
        return R.layout.delegate_payment;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        CommonHandler.getInstance().key(phone);
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
            RxKeyboardTool.hideSoftInput(_mActivity);
            if(key!=null) {
                final String pass = RSAUtils.encryptByte(key, password);
                SpongeLoader.showLoading(_mActivity);
                OrderHandler.getInstance().vPayPass(pass);
            }else {
                CommonHandler.getInstance().key(phone);
            }
        });
        callback();

    }

    @Override
    public void onResume() {
        super.onResume();
        RxKeyboardTool.showSoftInput(_mActivity, pwd);
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void callback() {
        OrderHandler.getInstance().setListener(code->{
            SpongeLoader.stopLoading();
            switch (code) {
                case 200:
                    Sponge.getHandler().postDelayed(()->OrderHandler.getInstance().pay(params),3000);
                    break;
                case 115:
                    RxToast.showToast("The payment password is empty");
                    break;
                case 116:
                    RxToast.showToast(Sponge.getAppContext().getString(R.string.p_err_116));
                    break;
                case 424:
                    break;
                case 423:
                    break;
            }
        });
        CallbackManager.getInstance()
                .addCallback(CallbackType.KEY, args -> key = (String) args)
                .addCallback(CallbackType.PAY, args -> {
                    if (type == 1 || type == 5 || type == 4) {
                        TranPayDelegate tran = new TranPayDelegate();
                        tran.setOrderId(args.toString());
                        getSupportDelegate().pop();
                        getSupportDelegate().start(tran);
                    } else if (type == 2) {
                        final TopAccDelegate top = new TopAccDelegate();
                        top.setOrderId(args.toString());
                        getSupportDelegate().pop();
                        getSupportDelegate().start(top);
                    } else if (type == 3) {
                        WithdrawPayDelegate wpd = new WithdrawPayDelegate();
                        wpd.setOrderId(args.toString());
                        getSupportDelegate().pop();
                        getSupportDelegate().start(wpd);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        RxKeyboardTool.hideSoftInput(_mActivity);
    }
}
