package com.wavpayment.wavpay.ui.left.information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;


/**
 *
 * Created by Administrator on 2017/12/13.
 */

public class StatusDelegate extends BaseInfoDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_info_autograph;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView)$(R.id.tv_title)).setText("Status");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.confirm).setOnClickListener(v -> {
            final String status = ((TextView)$(R.id.autograph)).getText().toString();
            mListener.onInfo(status);
            CommonHandler.getInstance().update("sign",status);
        });
    }
}
