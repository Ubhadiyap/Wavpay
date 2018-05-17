package com.wavpayment.wavpay.ui.main.bank;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.List;

/**
 * 银行类型适配器
 * Created by Administrator on 2017/12/21.
 */

public class CardTypeAdapter extends BaseQuickAdapter<MultipleItemEntity,BaseViewHolder>{

    public CardTypeAdapter(@Nullable List<MultipleItemEntity> data) {
        super(R.layout.item_info_gender,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        helper.setText(R.id.tv_name,item.getField(MultipleFields.TEXT));
    }
}
