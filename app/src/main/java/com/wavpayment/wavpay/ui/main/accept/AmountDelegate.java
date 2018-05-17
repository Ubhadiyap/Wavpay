package com.wavpayment.wavpay.ui.main.accept;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.RxKeyboardTool;
import com.wavpayment.wavpay.R;

/**
 * 设置收款金额
 * Created by Administrator on 2017/12/22.
 */

public class AmountDelegate extends SpongeDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_amount;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        final EditText amount =  $(R.id.amount);
        ((TextView)$(R.id.tv_title)).setText(R.string.amount_title);
        $(R.id.next).setOnClickListener(v -> {
            final String args = amount.getText().toString();
            mListener.executeCallback(args);
            getSupportDelegate().pop();
        });

        ((EditText) $(R.id.amount)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }
    //回调
    protected CallbackListener mListener;

    public void setCallbackListener(CallbackListener mListener) {
        this.mListener = mListener;
    }

    interface CallbackListener{
        void executeCallback(String args);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxKeyboardTool.hideSoftInput(_mActivity);
    }
}
