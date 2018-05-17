package com.wavpayment.wavpay.ui.main.bill;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.utils.DateUtils;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;
import com.wavpayment.wavpay.widgte.recycler.MultipleViewHolder;

import java.util.List;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


/**
 * 账单适配器
 * Created by Administrator on 2017/12/29.
 */

public class BillAdapter extends MultipleRecyclerAdapter {
    private String payAccount;

    protected BillAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(BillItemType.BILL_ITEM, R.layout.item_bill);
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject it = JSON.parseObject(response).getJSONObject("user");
        payAccount = it.getString("account");//设置当前账号
    }


    public static BillAdapter create(DataConverter converter) {
        return new BillAdapter(converter.convert());
    }


    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {
        final String acc = item.getField(BillFields.payAccount);
        final long trade = item.getField(BillFields.tradeTime);
        double amount = item.getField(BillFields.payAmount);
        double payAmount = Math.abs(amount);
        String tradeTime = DateUtils.getDateToString(trade);
        final String time = tradeTime.substring(tradeTime.length() - 5, tradeTime.length());
        final String day = tradeTime.substring(0, tradeTime.length() - 6);
        final int tradeType = item.getField(BillFields.tradeType);
        if (tradeType == 2) {
            helper.getView(R.id.tv_phone).setVisibility(View.GONE);
            helper.setText(R.id.tv_rm, "+" + payAmount);
            helper.setText(R.id.tv_time, time);
            helper.setText(R.id.tv_day, day);
            helper.setText(R.id.tv_type, "Top-up");
        } else if (tradeType == 3) {
            helper.getView(R.id.tv_phone).setVisibility(View.GONE);
            helper.setText(R.id.tv_rm, "-" + payAmount);
            helper.setText(R.id.tv_time, time);
            helper.setText(R.id.tv_day, day);
            helper.setText(R.id.tv_type, "Withdraw");
        } else if (tradeType == 1) {
            if (acc.equals(payAccount)) {
                final String phone = item.getField(BillFields.collectAccount);
                helper.setText(R.id.tv_phone, phone);
                helper.setText(R.id.tv_rm, "-" + payAmount);
                helper.setText(R.id.tv_time, time);
                helper.setText(R.id.tv_day, day);
                final String collUserName = item.getField(BillFields.collUserName);
                helper.setText(R.id.tv_type, collUserName);
            } else {
                final String phone = item.getField(BillFields.payAccount);
                helper.setText(R.id.tv_phone, phone);
                helper.setText(R.id.tv_rm, "+" + payAmount);
                helper.setText(R.id.tv_time, time);
                helper.setText(R.id.tv_day, day);
                final String payUserName = item.getField(BillFields.payUserName);
                helper.setText(R.id.tv_type, payUserName);
            }
        } else if (tradeType == 4) {

        } else if (tradeType == 5) {

        } else if (tradeType == 6) {

        }
    }
}
