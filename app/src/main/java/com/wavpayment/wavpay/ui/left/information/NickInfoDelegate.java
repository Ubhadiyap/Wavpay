package com.wavpayment.wavpay.ui.left.information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;


public class NickInfoDelegate extends BaseInfoDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_info_nick;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        ((TextView)$(R.id.tv_title)).setText("Name");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.confirm).setOnClickListener(v -> {
            final String nick = ((TextView)$(R.id.nickname)).getText().toString();
            CommonHandler.getInstance().update("nickName",nick);
            mListener.onInfo(nick);
        });
    }
}
