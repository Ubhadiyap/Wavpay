package com.wavpayment.wavpay.ui.left.security;

import android.view.View;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;
import com.wavpayment.wavpay.widgte.recycler.MultipleViewHolder;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class SecurityAdapter extends MultipleRecyclerAdapter {


    private SecurityAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(CommonItemType.INFO_TEXT, R.layout.item_info_text);
    }

    public static SecurityAdapter create(List<MultipleItemEntity> data) {
        return new SecurityAdapter(data);
    }

    public static SecurityAdapter create(DataConverter converter) {
        return new SecurityAdapter(converter.convert());
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {
        final String title = item.getField(MultipleFields.TITLE);
        switch (helper.getItemViewType()) {
            case CommonItemType.INFO_TEXT:
                helper.setText(R.id.tv_title, title);
                helper.getView(R.id.tv_content).setVisibility(View.INVISIBLE);
                break;
            default:
                break;

        }
    }

}
