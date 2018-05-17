package com.wavpayment.wavpay.ui.main.details;

import com.wavpayment.wavpay.R;

/**
 *
 * Created by Administrator on 2017/11/25.
 */

public class ElectricDetailsDelegate extends BaseDetailsDelegate {


    @Override
    protected void init() {
        setTitle(getString(R.string.electri));
        setImage("electricity");
        setName("Top-up", "Amount", "Account");
        setHint("Please Choose", "Please Choose", "Key in number");
        eTwo.setText("");
    }

    @Override
    protected int onType() {
        return 2;
    }
}
