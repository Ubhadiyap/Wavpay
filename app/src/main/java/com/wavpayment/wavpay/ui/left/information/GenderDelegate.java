package com.wavpayment.wavpay.ui.left.information;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lan.sponge.widget.BaseDecoration;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

public class GenderDelegate extends BaseInfoDelegate {
    private List<MultipleItemEntity> entities = null;
    private RecyclerView rvCommon;

    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("Gender");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        rvCommon = $(R.id.rv_common);
        entities = new ArrayList<>();
        MultipleItemEntity male = MultipleItemEntity.builder()
                .setField(MultipleFields.ID, "Male")
                .setField(MultipleFields.TAG, false)
                .build();
        entities.add(male);
        MultipleItemEntity female = MultipleItemEntity.builder()
                .setField(MultipleFields.ID, "Female")
                .setField(MultipleFields.TAG, false)
                .build();
        entities.add(female);
        MultipleItemEntity others = MultipleItemEntity.builder()
                .setField(MultipleFields.ID, "Others")
                .setField(MultipleFields.TAG, false)
                .build();
        entities.add(others);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        rvCommon.setLayoutManager(manager);
        rvCommon.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), com.lan.sponge.R.color.app_background), 5));
        GenderAdapter mAdapter = new GenderAdapter(entities);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MultipleItemEntity entity = entities.get(position);
            final String gender = entity.getField(MultipleFields.ID);
            mListener.onInfo(gender);
            String g;
            if (gender.equals("Male")) {
                g = "1";
            } else if (gender.equals("Female")) {
                g = "2";
            } else {
                g = "3";
            }
            CommonHandler.getInstance().update("gender", g);
            getSupportDelegate().pop();
        });
        rvCommon.setAdapter(mAdapter);
    }
}
