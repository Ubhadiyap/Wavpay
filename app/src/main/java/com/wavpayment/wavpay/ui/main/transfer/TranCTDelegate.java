package com.wavpayment.wavpay.ui.main.transfer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.widget.BaseDecoration;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.bank.BankFields;
import com.wavpayment.wavpay.ui.main.bank.BankType;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

public class TranCTDelegate extends SpongeDelegate {
    private List<MultipleItemEntity> entities = null;
    private RecyclerView rvCommon;
    private TranCTAdapter mAdapter;

    private boolean isBalance = true;//显示余额

    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("Bank Account");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        rvCommon = $(R.id.rv_common);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        rvCommon.setLayoutManager(manager);
        rvCommon.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), com.lan.sponge.R.color.app_background), 5));
        network();

    }

    private void network() {
        Network.getInstance()
                .get(NetUrl.CARDS)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final int code = object.getInteger("statusCode");
                        if (code == 200) {
                            entities = new ArrayList<>();
                            if (isBalance) {
                                final String balan = object.getString("balance");
                                MultipleItemEntity b = MultipleItemEntity.builder()
                                        .setField(MultipleFields.ITEM_TYPE, BankType.CARD_LIST)
                                        .setField(MultipleFields.SPAN_SIZE, 4)
                                        .setField(MultipleFields.ID, -1)
                                        .setField(BankFields.accountType, "")
                                        .setField(BankFields.balance, balan)
                                        .setField(BankFields.bankCode, "")
                                        .setField(BankFields.cardNumber, "")
                                        .setField(BankFields.realName, "")
                                        .setField(BankFields.ID, "")
                                        .setField(BankFields.userId, "")
                                        .setField(MultipleFields.TAG, false)
                                        .build();
                                entities.add(b);
                            }
                            final JSONArray banks = object.getJSONArray("bankList");
                            final int size = banks.size();
                            for (int i = 0; i < size; i++) {
                                final JSONObject item = banks.getJSONObject(i);
                                final int accountType = item.getIntValue("accountType");
                                final String balance = item.getString("balance");
                                final String bankCode = item.getString("bankCode");
                                final String cardNumber = item.getString("cardNumber");
                                final String realName = item.getString("realName");
                                final int id = item.getIntValue("id");
                                final int userId = item.getIntValue("userId");
                                MultipleItemEntity entity = MultipleItemEntity.builder()
                                        .setField(MultipleFields.ITEM_TYPE, BankType.CARD_LIST)
                                        .setField(MultipleFields.SPAN_SIZE, 4)
                                        .setField(MultipleFields.ID, 0)
                                        .setField(BankFields.accountType, accountType)
                                        .setField(BankFields.balance, balance)
                                        .setField(BankFields.bankCode, bankCode)
                                        .setField(BankFields.cardNumber, cardNumber)
                                        .setField(BankFields.realName, realName)
                                        .setField(BankFields.ID, id)
                                        .setField(BankFields.userId, userId)
                                        .setField(MultipleFields.TAG, false)
                                        .build();
                                entities.add(entity);
                            }
                            mAdapter = new TranCTAdapter(entities);
                            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                                MultipleItemEntity entity = entities.get(position);
                                mListener.onType(entity);
                                getSupportDelegate().pop();
                            });
                            rvCommon.setAdapter(mAdapter);
                        }
                    }
                });
    }

    protected CardTypeListener mListener;

    public void setListener(CardTypeListener listener) {
        this.mListener = listener;
    }

    public interface CardTypeListener {
        void onType(MultipleItemEntity entity);
    }


    public void setBalance(boolean balance) {
        isBalance = balance;
    }

}
