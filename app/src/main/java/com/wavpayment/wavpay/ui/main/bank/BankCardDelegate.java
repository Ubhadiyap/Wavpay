package com.wavpayment.wavpay.ui.main.bank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.log.SpongeLogger;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.main.bank.BankCarPresenterImpl;
import com.wavpayment.wavpay.service.main.bank.IBankContract;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;

/**
 * 银行卡列表
 * Created by Administrator on 2017/12/6.
 */

public class BankCardDelegate extends SpongeDelegate implements IBankContract.View {

    private RecyclerView rvCard;
    private DataConverter converter;
    private BankCardAdapter mAdapter;
    private IBankContract.Presenter presenter;
    private ImageView ivRight;

    @Override
    public Object setLayout() {
        return R.layout.delegate_bank_card;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        rvCard = $(R.id.rv_card);
        ivRight = $(R.id.tv_right);
        presenter = new BankCarPresenterImpl(this);
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ivRight.setOnClickListener(view -> getSupportDelegate().start(new BankPaymentDelegate()));

        CallbackManager.getInstance()
                .addCallback(CallbackType.CARD, args -> presenter.cardList());
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final GridLayoutManager manager = new GridLayoutManager(_mActivity, 4);
        rvCard.setLayoutManager(manager);
        presenter.cardList();
    }

    @Override
    public void error(int code) {

    }

    @Override
    public void onCardList(String response) {
        SpongeLogger.e("onCardList", response);
        converter = new BankCardDataConverter();
        final JSONArray data = JSON.parseObject(response).getJSONArray("bankList");
        final int size = data.size();
        mAdapter = BankCardAdapter.create(converter.setJsonData(response),this);
        rvCard.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (size==0) {
                getSupportDelegate().start(new BankPaymentDelegate());
            }
        });
    }
}
