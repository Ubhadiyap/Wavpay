package com.wavpayment.wavpay.ui.main.transfer;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.ui.main.bank.BankFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/12/21.
 */

public class TranCTAdapter extends BaseQuickAdapter<MultipleItemEntity,BaseViewHolder>{

    public TranCTAdapter(@Nullable List<MultipleItemEntity> data) {
        super(R.layout.item_info_gender,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        final int id = item.getField(MultipleFields.ID);
        if (id==-1){
            String balance = item.getField(BankFields.balance);
            helper.setText(R.id.tv_name,"Balance (RM"  + balance + ")");
        } else {
            String content = item.getField(BankFields.cardNumber);
            String bankCode = item.getField(BankFields.bankCode);
            content = content.substring(content.length() - 4, content.length());
            helper.setText(R.id.tv_name, bankCode + "(" + content + ")");
        }
    }
}
