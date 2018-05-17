package com.wavpayment.wavpay.ui.main.details;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wavpayment.wavpay.R;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/12/21.
 */

public class TypeDialogAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public TypeDialogAdapter(@Nullable List<String> data) {
        super(R.layout.item_info_gender,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,item);
    }
}
