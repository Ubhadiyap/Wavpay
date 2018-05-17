package com.wavpayment.wavpay.ui.main.details;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lan.sponge.util.log.SpongeLogger;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/12/21.
 */

public class TypePayAdapter extends BaseQuickAdapter<MultipleItemEntity,BaseViewHolder>{

    public TypePayAdapter(@Nullable List<MultipleItemEntity> data) {
        super(R.layout.item_info_gender,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        final String orgName =  item.getField(TypeType.orgName);
        helper.setText(R.id.tv_name,orgName);
    }
}
