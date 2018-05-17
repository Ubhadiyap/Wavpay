package com.wavpayment.wavpay.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.login.LoginDelegate;
import com.wavpayment.wavpay.ui.main.MainDelegate;

/**
 * Created by Administrator on 2018/1/18.
 */

public class SuccessDelegate extends SpongeDelegate implements LoginListener {

    private String mobile;
    private String password;

    @Override
    public Object setLayout() {
        return R.layout.delegate_success;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("Success");
        $(R.id.iv_back).setVisibility(View.GONE);
        $(R.id.next).setOnClickListener(v -> {
            getSupportDelegate().pop();
            getSupportDelegate().start(new MainDelegate());
        });
        $(R.id.add_bank).setOnClickListener(v -> RxToast.showToast("Is developing"));
        $(R.id.add_card).setOnClickListener(v -> RxToast.showToast("Is developing"));

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        CommonHandler.getInstance().login(this, mobile, password);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        getSupportDelegate().start(new LoginDelegate());
        return true;
    }

    @Override
    public void onLogin() {

    }
}
