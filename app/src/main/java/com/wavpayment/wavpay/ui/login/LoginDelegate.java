package com.wavpayment.wavpay.ui.login;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.main.MainDelegate;
import com.wavpayment.wavpay.ui.register.LoginListener;
import com.wavpayment.wavpay.ui.register.MobileDelegate;
import com.wavpayment.wavpay.utils.RSAUtils;

public class LoginDelegate extends SpongeDelegate implements LoginListener {
    private EditText etUser;
    private EditText etPass;
    private String phone = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_login;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        etUser = $(R.id.et_user);
        etPass = $(R.id.et_pass);
        $(R.id.btn_login).setOnClickListener(v -> {
            final String username = etUser.getText().toString().replace(" ", "");
            String password = etPass.getText().toString();
            if (checkForm(username, password)) {
                SpongeLoader.showLoading(_mActivity);
                CommonHandler.getInstance().key(username);
            }
        });
        $(R.id.tv_forget).setOnClickListener(v -> {
            getSupportDelegate().pop();
            getSupportDelegate().start(new LinkedDelegate());
        });
        $(R.id.tv_sign_in).setOnClickListener(v ->{
            getSupportDelegate().pop();
            getSupportDelegate().start(new MobileDelegate());
        });
        CallbackManager.getInstance()
                .addCallback(CallbackType.KEY, args -> {
                    final String key = (String) args;
                    final String username = etUser.getText().toString().replace(" ", "");
                    String password = etPass.getText().toString();
                    password = RSAUtils.encryptByte(key, password);
                    CommonHandler.getInstance().login(this, username, password);
                });
        etUser.setText(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private boolean checkForm(String username, String password) {
        if (username.isEmpty()) {
            show(getResources().getString(R.string.l_err_user));
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            show(getResources().getString(R.string.l_err_pass));
            return false;
        }
        return true;
    }

    @Override
    public void onLogin() {
        getSupportDelegate().startWithPop(new MainDelegate());
    }

    @Override
    public boolean onBackPressedSupport() {
        _mActivity.finish();
        return true;
    }
}
