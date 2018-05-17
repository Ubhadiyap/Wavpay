package com.wavpayment.wavpay.ui.left.information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;

/**
 * d by Administrator on 2017/11/23.
 */

public class InfoDelegate extends SpongeDelegate {

    private RecyclerView rvIndex;
    private InfoHandler mInfoHandler;

    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        rvIndex = $(R.id.rv_common);
        mInfoHandler = InfoHandler.create(rvIndex, new InfoDataConverter());
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView(rvIndex);
        mInfoHandler.refresh(this);
    }



}
