package com.wavpayment.wavpay.ui.left.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.RxAppTool;
import com.wavpayment.wavpay.R;

/**
 * 关于
 * Created by Administrator on 2018/1/4.
 */

public class AboutDelegate extends SpongeDelegate{
    @Override
    public Object setLayout() {
        return R.layout.delegate_about;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        String version = RxAppTool.getAppVersionName(_mActivity);
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        ((TextView)$(R.id.tv_version)).setText("Wavpay "+version);
    }

}
