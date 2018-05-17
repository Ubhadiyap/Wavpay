package com.wavpayment.wavpay.ui.left.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;

/**
 *
 * Created by Administrator on 2018/1/4.
 */

public class SettingDelegate extends SpongeDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_settring;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
    }
}
