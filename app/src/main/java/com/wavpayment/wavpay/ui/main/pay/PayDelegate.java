package com.wavpayment.wavpay.ui.main.pay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.main.accept.AcceptDelegate;
import com.wavpayment.wavpay.ui.main.bank.BankFields;
import com.wavpayment.wavpay.ui.main.bill.BillDelegate;
import com.wavpayment.wavpay.ui.main.order.MerchantsDelegate;
import com.wavpayment.wavpay.ui.main.order.TranPayDelegate;
import com.wavpayment.wavpay.ui.main.transfer.TranCTDelegate;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


/**
 * 生成付款码
 * Created by Administrator on 2017/11/25.
 */

public class PayDelegate extends SpongeDelegate {


    private String cardNumber;
    private String bankCode;

    @Override
    public Object setLayout() {
        return R.layout.delegate_pay;
    }


    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        final String ba = data.getString("balance");
        ((TextView) $(R.id.tv_card_name)).setText("Balance (RM" + ba + ")");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.tv_right).setOnClickListener(view -> getSupportDelegate().start(new BillDelegate()));
        $(R.id.btn_accept).setOnClickListener(v -> getSupportDelegate().start(new AcceptDelegate()));
        $(R.id.tv_change).setOnClickListener(view -> {
            final TranCTDelegate tran = new TranCTDelegate();
            getSupportDelegate().start(tran);
            tran.setListener(entity -> {
                int id = entity.getField(MultipleFields.ID);
                if (id == -1) {
                    String balance = entity.getField(BankFields.balance);
                    ((TextView) $(R.id.tv_card_name)).setText("Balance (RM" + balance + ")");
                    CommonHandler.getInstance().payCode($(R.id.iv_code), $(R.id.iv_linecode), "");
                } else {
                    cardNumber = entity.getField(BankFields.cardNumber);
                    bankCode = entity.getField(BankFields.bankCode);
                    final int bid = entity.getField(BankFields.ID);
                    String content = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
                    ((TextView) $(R.id.tv_card_name)).setText(bankCode + "(" + content + ")");
                    CommonHandler.getInstance().payCode($(R.id.iv_code), $(R.id.iv_linecode), String.valueOf(bid));
                }
            });
        });

        CallbackManager.getInstance()
                .addCallback(CallbackType.PAY_PUSH, args -> {
                    final JSONObject object = JSON.parseObject(args.toString());
                    final String account = object.getString("account");
                    if (account != null) {
                        final String orderId = object.getString("orderId");
                        TranPayDelegate tran = new TranPayDelegate();
                        tran.setOrderId(orderId);
                        getSupportDelegate().startWithPop(tran);
                    }
                })
                .addCallback(CallbackType.PAY_PAY, args -> {
                    MerchantsDelegate merchants = new MerchantsDelegate();
                    merchants.setData(args.toString());
                    getSupportDelegate().pop();
                    getSupportDelegate().start(merchants);
                });
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        CommonHandler.getInstance().payCode($(R.id.iv_code), $(R.id.iv_linecode), "");
    }
}
