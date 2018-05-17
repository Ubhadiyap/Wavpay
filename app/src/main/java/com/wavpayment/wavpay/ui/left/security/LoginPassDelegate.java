package com.wavpayment.wavpay.ui.left.security;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.login.BasePassDelegate;

/**
 *
 * Created by Administrator on 2017/12/26.
 */

public class LoginPassDelegate extends BasePassDelegate{

    @Override
    protected void initView() {
        setTitle(getString(R.string.login_pass));
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
        return 1;
    }

    @Override
    protected void back() {
        getSupportDelegate().pop();
    }
}
