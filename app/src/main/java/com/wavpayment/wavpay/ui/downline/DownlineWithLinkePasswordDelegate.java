package com.wavpayment.wavpay.ui.downline;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.login.BasePassDelegate;
import com.wavpayment.wavpay.ui.login.LoginDelegate;

/**
 * 下线更改密码
 * Created by lenovo on 2018/5/16.
 */

public class DownlineWithLinkePasswordDelegate extends BasePassDelegate {
    @Override
    protected void initView() {
        setTitle(getString(R.string.downline_reset_password));
    }

    @Override
    protected void onKey(String key) {
        CommonHandler.getInstance().linked(userName, password, code,"2");
    }

    @Override
    protected void onLinked() {
        start(new LoginDelegate());
    }

    @Override
    protected int codeType() {
        return 1;
    }

    @Override
    protected void back() {
        start(new LoginDelegate());
    }
}
