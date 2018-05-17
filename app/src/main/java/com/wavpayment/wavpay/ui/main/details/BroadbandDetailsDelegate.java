package com.wavpayment.wavpay.ui.main.details;

import com.wavpayment.wavpay.R;

/**
 *
 * Created by Administrator on 2017/11/25.
 */

public class BroadbandDetailsDelegate extends BaseDetailsDelegate {


    @Override
    protected void init() {
        setTitle(getString(R.string.broadband));
        setImage("broadband");
        setName("Top-up", "Amount", "Account");
        setHint("Please Choose", "Please Choose", "Key in number");
    }


    @Override
    protected int onType() {
        return 6;
    }
}
