package com.wavpayment.wavpay.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.utils.RSAUtils;

/**
 * Created by Administrator on 2018/1/18.
 */

public class RegisterDelegate extends SpongeDelegate {

    private String mobile;
    private EditText pass;
    private EditText pin;
    private CheckBox checkBox;
    private String password;
    private String ppin;
    private String key;

    @Override
    public Object setLayout() {
        return R.layout.delegate_register;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("Register");
        CommonHandler.getInstance().key(mobile);
        $(R.id.iv_back).setOnClickListener(v -> {
            getSupportDelegate().pop();
            getSupportDelegate().start(new MobileDelegate());
        });
        $(R.id.next).setOnClickListener(v -> {
            password = pass.getText().toString();
            ppin = pin.getText().toString();
            if (checkForm(password, ppin)) {
                SpongeLoader.showLoading(_mActivity);
                password = RSAUtils.encryptByte(key, password);
                CommonHandler.getInstance().register(this, mobile, password, ppin);
            }
        });


        CallbackManager.getInstance().addCallback(CallbackType.KEY, args -> key = (String) args);
        pass = $(R.id.et_pass);
        pin = $(R.id.et_pin);
        checkBox = $(R.id.check);

    }

    private boolean checkForm(String password, String pin) {
        if (pin.isEmpty() || pin.length() != 6) {
            RxToast.showToast("The payment PIN must be a number with a length of 6 digits");
            return false;
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            RxToast.showToast(getResources().getString(R.string.r_pass));
            return false;
        }
        if (!checkBox.isChecked()) {
            RxToast.showToast(getResources().getString(R.string.r_check));
            return false;
        }
        return true;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        getSupportDelegate().start(new MobileDelegate());
        return true;
    }
}
