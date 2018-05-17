package com.wavpayment.wavpay.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.ui.login.AccountManager;
import com.wavpayment.wavpay.ui.login.IUserChecker;
import com.wavpayment.wavpay.ui.login.LoginDelegate;

import java.io.File;


public class StartDelegate extends SpongeDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_start;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.start).setEnabled(false);
        //if (!isRoot()) {
        if (true) {
            $(R.id.start).setEnabled(true);
            $(R.id.root).setVisibility(View.GONE);
        } else {
            $(R.id.root).setVisibility(View.VISIBLE);
        }
        $(R.id.start).setOnClickListener(v -> getSupportDelegate().start(new LoginDelegate()));
    }


    /**
     * 判断手机是否ROOT
     */
    public boolean isRoot() {

        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        AccountManager.checkAccount(new IUserChecker() {
            @Override
            public void onSignIn() {
                getSupportDelegate().start(new MainDelegate());
            }

            @Override
            public void onNotSignIn() {

            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        _mActivity.finish();
        return true;
    }
}
