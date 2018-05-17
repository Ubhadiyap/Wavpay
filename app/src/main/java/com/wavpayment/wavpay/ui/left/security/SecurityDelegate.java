package com.wavpayment.wavpay.ui.left.security;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/12/26.
 */

public class SecurityDelegate extends SpongeDelegate{
    private RecyclerView rvIndex;
    private SecurityAdapter mAdapter;
    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView)$(R.id.tv_title)).setText(R.string.security_title);
        rvIndex = $(R.id.rv_common);
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView(rvIndex);
        final List<MultipleItemEntity> entities = new ArrayList<>();
        final MultipleItemEntity payment = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,CommonItemType.INFO_TEXT)
                .setField(MultipleFields.TITLE,getString(R.string.security_payment))
                .setField(MultipleFields.TEXT, "")
                .setField(MultipleFields.ITEM,new SPaymentDelegate())
                .setField(MultipleFields.SPAN_SIZE, 4)
                .build();
        entities.add(payment);
        final MultipleItemEntity login = MultipleItemEntity.builder()
                .setField(MultipleFields.ITEM_TYPE,CommonItemType.INFO_TEXT)
                .setField(MultipleFields.TITLE,getString(R.string.security_login))
                .setField(MultipleFields.TEXT, "")
                .setField(MultipleFields.ITEM,new LoginPassDelegate())
                .setField(MultipleFields.SPAN_SIZE, 4)
                .build();
        entities.add(login);
        mAdapter = SecurityAdapter.create(entities);
        rvIndex.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
            final SpongeDelegate item = entity.getField(MultipleFields.ITEM);
            getSupportDelegate().start(item);
        });
    }
}
