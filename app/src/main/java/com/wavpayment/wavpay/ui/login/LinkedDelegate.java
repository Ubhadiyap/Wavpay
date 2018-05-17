package com.wavpayment.wavpay.ui.login;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;


/**
 *  找回密码
 * Created by Administrator on 2017/11/17.
 */

public class LinkedDelegate extends BasePassDelegate{

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.linked_title));
    }

    @Override
    protected void onKey(String key) {
        CommonHandler.getInstance().linked(userName, password, code,"1");
    }

    @Override
    protected void onLinked() {
        getSupportDelegate().startWithPop(new LoginDelegate());
    }

    @Override
    protected int codeType() {
        return 2;
    }

    @Override
    protected void back() {
        getSupportDelegate().pop();
        getSupportDelegate().start(new LoginDelegate());
    }

    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        getSupportDelegate().start(new LoginDelegate());
        return true;
    }
}
