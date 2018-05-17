package com.wavpayment.wavpay.ui.main.bank;


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
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行类型列表
 */
public class CardTypeDelegate extends SpongeDelegate {
    private List<MultipleItemEntity> entities = null;
    private RecyclerView rvCommon;
    private CardTypeAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        ((TextView) $(R.id.tv_title)).setText("Card Type");
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

    private void network(){
        Network.getInstance()
                .get(NetUrl.BANKCODES)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        final int code = object.getInteger("statusCode");
                        if (code==200){
                            entities = new ArrayList<>();
                            final JSONArray banks = object.getJSONArray("bankCodes");
                            final int size = banks.size();
                            for (int i = 0;i<size;i++){
                                final JSONObject item = (JSONObject) banks.get(i);
                                final String text = item.getString("text");
                                final String value = item.getString("value");
                                MultipleItemEntity entity = MultipleItemEntity.builder()
                                        .setField(MultipleFields.TEXT,text)
                                        .setField(MultipleFields.TITLE,value)
                                        .build();
                                entities.add(entity);
                            }
                            mAdapter = new CardTypeAdapter(entities);
                            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                                MultipleItemEntity entity = entities.get(position);
                                final String value = entity.getField(MultipleFields.TITLE);
                                final String text = entity.getField(MultipleFields.TEXT);
                                mListener.onType(value,text);
                                getSupportDelegate().pop();
                            });
                            rvCommon.setAdapter(mAdapter);
                        }
                    }
                });
    }

    private CardTypeListener mListener;

    public void setListener(CardTypeListener listener) {
        this.mListener = listener;
    }

    public interface CardTypeListener {
        void onType(String ...args);
    }
}
