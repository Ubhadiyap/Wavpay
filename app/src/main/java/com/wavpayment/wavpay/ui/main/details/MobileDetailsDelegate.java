package com.wavpayment.wavpay.ui.main.details;

import com.wavpayment.wavpay.R;

/**
 *
 * Created by Administrator on 2017/11/25.
 */

public class MobileDetailsDelegate extends BaseDetailsDelegate {


    @Override
    protected void init() {
        setTitle(getString(R.string.mobile));
        setImage("mobile");
        setName("Top-up", "Amount", getString(R.string.mobile));
        setHint("Please Choose", "Please Choose", "Key in number");
        eTwo.setText("");
    }


    @Override
    protected int onType() {
        return 1;
    }
}
