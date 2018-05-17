package com.wavpayment.wavpay.ui.main.bank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;

/**
 * 添加银行卡
 * Created by Administrator on 2017/12/28.
 */

public class CardDelegate extends SpongeDelegate implements CardTypeDelegate.CardTypeListener {
    @Override
    public Object setLayout() {
        return R.layout.delegate_card;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText("Card Information");
        $(R.id.item_one).setOnClickListener(view -> {
            final CardTypeDelegate type = new CardTypeDelegate();
            getSupportDelegate().start(type);
            type.setListener(this);
        });
        $(R.id.btn_next).setOnClickListener(view -> {
            final String type = ((TextView) $(R.id.et_one)).getText().toString();
            final String phone = ((TextView) $(R.id.et_two)).getText().toString();
            if (!type.isEmpty() && !phone.isEmpty()) {
                final CodeDelegate code = new CodeDelegate();
                code.setPhone(phone);
                code.setType(type);
                getSupportDelegate().startWithPop(code);
            }
        });
    }

    @Override
    public void onType(String... args) {
        ((TextView) $(R.id.et_one)).setText(args[0]);
    }
}
