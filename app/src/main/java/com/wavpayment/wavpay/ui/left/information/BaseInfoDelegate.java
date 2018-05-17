package com.wavpayment.wavpay.ui.left.information;

import com.lan.sponge.delegate.SpongeDelegate;

/**
 *
 * Created by Administrator on 2017/12/13.
 */

public abstract class BaseInfoDelegate extends SpongeDelegate {
    protected InfoListener mListener;

    protected void setListener(InfoListener listener) {
        this.mListener = listener;
    }

    interface InfoListener {
        void onInfo(String args);
    }
}
