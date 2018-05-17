package com.wavpayment.wavpay.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.login.LoginDelegate;

/**
 *
 * Created by Administrator on 2018/1/18.
 */

public class MobileDelegate extends SpongeDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_mobile;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView)$(R.id.tv_title)).setText("Mobile number");
        $(R.id.iv_back).setOnClickListener(v -> {
            getSupportDelegate().pop();
            getSupportDelegate().start(new LoginDelegate());
        });
        $(R.id.next).setOnClickListener(v -> {
            final String mobile = ((EditText)$(R.id.et_mobile)).getText().toString();
            if (!mobile.isEmpty()){
                $(R.id.next).setEnabled(false);
                CommonHandler.getInstance().accounts(this,mobile);
            }else {
                RxToast.showToast(getResources().getString(R.string.phone));
            }
        });


        CallbackManager.getInstance()
                .addCallback(CallbackType.CHECK,args -> $(R.id.next).setEnabled(true));

    }



    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        getSupportDelegate().start(new LoginDelegate());
        return true;
    }
}
