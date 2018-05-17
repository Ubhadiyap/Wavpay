package com.wavpayment.wavpay.ui.left.security;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.login.BasePassDelegate;

/**
 *
 * Created by Administrator on 2017/12/26.
 */

public class SPaymentDelegate extends BasePassDelegate {
    @Override
    protected void initView() {
        setTitle(getString(R.string.payment_title));
    }

    @Override
    protected void onKey(String key) {

        CommonHandler.getInstance().linked(userName, password, code,"2");
    }

    @Override
    protected void onLinked() {
        getSupportDelegate().pop();
    }

    @Override
    protected int codeType() {
        return 3;
    }

    @Override
    protected void back() {
        getSupportDelegate().pop();
    }
}
