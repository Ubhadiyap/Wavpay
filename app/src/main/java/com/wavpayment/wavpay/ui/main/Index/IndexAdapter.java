package com.wavpayment.wavpay.ui.main.Index;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lan.sponge.widget.BaseDecoration;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonItemAdapter;
import com.wavpayment.wavpay.service.common.CommonItemEntity;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;
import com.wavpayment.wavpay.widgte.recycler.MultipleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class IndexAdapter extends MultipleRecyclerAdapter {
    private final SpongeDelegate DELEGATE;

    private IndexAdapter(List<MultipleItemEntity> data, SpongeDelegate delegate) {
        super(data);
        addItemType(CommonItemType.MORE, R.layout.item_common_more);
        this.DELEGATE = delegate;
    }

    public static IndexAdapter create(List<MultipleItemEntity> data, SpongeDelegate delegate) {
        return new IndexAdapter(data, delegate);
    }

    public static IndexAdapter create(DataConverter converter, SpongeDelegate delegate) {
        return new IndexAdapter(converter.convert(), delegate);
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()) {
            case CommonItemType.MORE:
                final String title = item.getField(MultipleFields.TITLE);
                final ArrayList<CommonItemEntity> items = item.getField(MultipleFields.ITEM);
                helper.setText(R.id.tv_title, title);
                CommonItemAdapter itemAdapter = new CommonItemAdapter(items);
                RecyclerView mRecyclerView = helper.getView(R.id.rv_booking);
                GridLayoutManager manager = new GridLayoutManager(mContext, 5);
                mRecyclerView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(mContext, R.color.white), 5));
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(itemAdapter);
                itemAdapter.setOnItemClickListener((adapter, view, position) -> {
                    final CommonItemEntity entity = (CommonItemEntity) adapter.getData().get(position);
                    final String name  = entity.getItemName();
                    if (entity.getDelegate() != null) {
                        DELEGATE.getParentDelegate().getSupportDelegate().start(entity.getDelegate());
                    } else {
                        IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.DIALOG);
                        callback.executeCallback(name);
                    }

                });
                break;
        }
    }



}
