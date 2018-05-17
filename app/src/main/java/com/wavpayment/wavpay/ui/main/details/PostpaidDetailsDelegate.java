package com.wavpayment.wavpay.ui.main.details;

import com.wavpayment.wavpay.R;

/**
 *
 * Created by Administrator on 2017/11/25.
 */

public class PostpaidDetailsDelegate extends BaseDetailsDelegate {


    @Override
    protected void init() {
        setTitle(getString(R.string.postpaid));
        setName("Top-up", "Amount", "Account");
        setHint("Please Choose", "Please Choose", "Key in number");
        setImage("month");

    }

    @Override
    protected int onType() {
        return 5;
    }
}
