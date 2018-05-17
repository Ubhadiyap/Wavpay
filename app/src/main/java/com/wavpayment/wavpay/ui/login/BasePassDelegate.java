package com.wavpayment.wavpay.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.timer.BaseTimerTask;
import com.lan.sponge.util.timer.ITimerListener;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.utils.RSAUtils;

import java.util.Timer;

/**
 * 找回密码基类
 * Created by Administrator on 2017/12/26.
 */

public abstract class BasePassDelegate extends SpongeDelegate implements ITimerListener {

    protected EditText etAccount;
    protected EditText etCode;
    protected EditText etPass;
    private TextView timer;
    private Timer mTimer = null;
    private int mCount = 60;
    protected String userName;
    protected String password;
    protected String code;

    protected abstract void initView();

    protected abstract void onKey(String key);

    protected abstract void onLinked();

    protected abstract int codeType();

    protected abstract void back();

    @Override
    public Object setLayout() {
        return R.layout.delegate_linked;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
        initView();
        callback();

    }

    protected void setTitle(String title) {
        ((TextView) $(R.id.tv_title)).setText(title);
    }

    private void init() {
        etAccount = $(R.id.et_account);
        etCode = $(R.id.et_code);
        etPass = $(R.id.et_pass);
        timer = $(R.id.btn_code);
        timer.setOnClickListener(v -> {
            final String mobile = etAccount.getText().toString();
            if (!mobile.isEmpty()) {
                initTimer();
                timer.setTextColor(getResources().getColor(R.color.app_bg));
                timer.setClickable(false);
                CommonHandler.getInstance().code(mobile);
            } else {
                RxToast.showToast(getString(R.string.r_phone_null));
            }

        });
        $(R.id.iv_back).setOnClickListener(v -> back());
        $(R.id.btn_next).setOnClickListener(v -> {
            userName = etAccount.getText().toString();
            password = etPass.getText().toString();
            code = etCode.getText().toString();
            if (checkForm(password, code)) {
                SpongeLoader.showLoading(getContext());
                CommonHandler.getInstance().key(userName);
            }
        });
    }

    private void callback() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.CODE, args -> {
                    int code = (int) args;
                    String msg = null;
                    switch (code) {
                        case 101:
                            msg = getString(R.string.code_101);
                            break;
                        case 102:
                            msg = getString(R.string.code_102);
                            break;
                        case 200:
                            msg = getString(R.string.code_200);
                            break;
                    }
                    show(msg);
                })
                .addCallback(CallbackType.KEY, args -> {
                    final String key = (String) args;
                    password = RSAUtils.encryptByte(key, password);
                    onKey(key);
                })
                .addCallback(CallbackType.LINKED, args -> {
                    SpongeLoader.stopLoading();
                    show(getString(R.string.lin_s));
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                    onLinked();
                });
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(() -> {
            if (timer != null) {
                timer.setText(mCount + " " + getResources().getString(R.string.countdown));
                mCount--;
                if (mCount < 0) {
                    if (mTimer != null) {
                        mCount = 60;
                        mTimer.cancel();
                        mTimer = null;
                        timer.setClickable(true);
                        timer.setText(getResources().getString(R.string.btn_code));
                        timer.setTextColor(getResources().getColor(R.color.app_bg));
                    }
                }
            }
        });
    }

    protected boolean checkForm(String password, String code) {
        if (code.isEmpty()) {
            show(getString(R.string.r_code));
            return false;
        }
        if (codeType()==3){
            if (password.isEmpty() || password.length() != 6) {
                show(getString(R.string.p_pass));
                return false;
            }
        }else {
            if (password.isEmpty() || password.length() < 6) {
                show(getString(R.string.r_pass));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}
