package com.wavpayment.wavpay.ui.main.bank;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.RxDataTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;
import com.wavpayment.wavpay.widgte.recycler.MultipleViewHolder;

import java.util.List;

/**
 *  银行卡适配器
 * Created by Administrator on 2017/12/27.
 */

public class BankCardAdapter extends MultipleRecyclerAdapter {

    private SpongeDelegate delegate;

    private BankCardAdapter(List<MultipleItemEntity> data, SpongeDelegate delegate) {
        super(data);
        this.delegate = delegate;
        addItemType(BankType.CARD_LIST, R.layout.item_bank_card);
    }

    public static BankCardAdapter create(DataConverter converter,SpongeDelegate delegate) {
        return new BankCardAdapter(converter.convert(),delegate);
    }


    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()) {
            case BankType.CARD_LIST:
                final String name = item.getField(BankFields.bankCode);
                final boolean tag = item.getField(MultipleFields.TAG);
                helper.setText(R.id.tv_card_name, name);
                final CardView bgCard = helper.getView(R.id.cd_card);
                final TextView del = helper.getView(R.id.btn_del);
                del.setOnClickListener(v -> {
                    final int bankId = item.getField(BankFields.ID);
                    final BankPaymentDelegate bankPay = new BankPaymentDelegate();
                    bankPay.setBankId(String.valueOf(bankId));
                    bankPay.setIsDel(false);
                    delegate.getSupportDelegate().start(bankPay);
                });
                if (tag) {
                    helper.getView(R.id.imageView).setVisibility(View.VISIBLE);
                    helper.getView(R.id.ll_add_card).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_card_n).setVisibility(View.INVISIBLE);
                    helper.getView(R.id.btn_del).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.imageView).setVisibility(View.GONE);
                    helper.getView(R.id.btn_del).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_card_n).setVisibility(View.VISIBLE);
                    String cardNumber = item.getField(BankFields.cardNumber);
                    cardNumber = RxDataTool.formatCard(cardNumber);
                    helper.setText(R.id.tv_card_n, cardNumber);
                    helper.getView(R.id.ll_add_card).setVisibility(View.INVISIBLE);
                }
                break;
            default:
                break;
        }
    }
}
