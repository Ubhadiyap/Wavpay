package com.wavpayment.wavpay.ui.main.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.widget.BaseDecoration;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.order.BaseOrderDelegate;
import com.wavpayment.wavpay.ui.main.order.NewOrderDelegate;
import com.wavpayment.wavpay.utils.Utils;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Administrator on 2017/12/27.
 */

public class TransferDelegate extends SpongeDelegate {
    private RecyclerView rvTran;
    private TransferAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_transfer;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText(getString(R.string.tran_title));
        $(R.id.ll_wavpay).setOnClickListener(view -> getSupportDelegate().start(new WavPayDelegate()));
        $(R.id.ll_card).setOnClickListener(view -> getSupportDelegate().start(new TranCardDelegate()));
        rvTran = $(R.id.rv_tran);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        rvTran.setLayoutManager(manager);
        rvTran.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), com.lan.sponge.R.color.app_background), 5));
        Sponge.getHandler().postDelayed(()->network(),500);
    }

    private void network() {
        Network.getInstance()
                .get(NetUrl.DELRECENTTRANSFER)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            final JSONObject object = JSON.parseObject(response.body());
                            final int code = object.getInteger("statusCode");
                            if (code == 200) {
                                ArrayList<MultipleItemEntity> es = new ArrayList<>();
                                final JSONArray data = object.getJSONArray("list");
                                for (int i = 0; i < data.size(); i++) {
                                    final JSONObject item = data.getJSONObject(i);
                                    final String accountOrCard = item.getString("accountOrCard");
                                    final String headImgOrBankCode = item.getString("headImgOrBankCode");
                                    final String realNameOrCardName = item.getString("realNameOrCardName");
                                    final String nick_name = item.getString("nick_name");
                                    final int id = item.getIntValue("id");
                                    MultipleItemEntity en = MultipleItemEntity.builder()
                                            .setField(MultipleFields.ID, id)
                                            .setField(MultipleFields.TITLE, accountOrCard)
                                            .setField(MultipleFields.TEXT, nick_name)
                                            .setField(MultipleFields.IMAGE_URL, realNameOrCardName)
                                            .setField(MultipleFields.TAG, headImgOrBankCode)
                                            .build();
                                    es.add(en);
                                }

                                mAdapter = new TransferAdapter(es);
                                rvTran.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener((adapter, view, position) -> {
                                    final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
                                    final int id = entity.getField(MultipleFields.ID);
                                    final String accountOrCard = entity.getField(MultipleFields.TITLE);
                                    if (id != -1) {
                                        NewOrderDelegate order = new NewOrderDelegate();
                                        order.setData(BaseOrderDelegate.TRANSFER, accountOrCard);
                                        getSupportDelegate().start(order);
                                    } else {
                                        getSupportDelegate().start(new TranCardDelegate());
                                    }
                                });

                                mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                                    final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
                                    final String number = entity.getField(MultipleFields.TITLE);
                                    dialog(position, number);
                                    return true;
                                });
                            }
                        }
                    }
                });
    }

    protected void dialog(int position, String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.tran_prompt));
        builder.setMessage(getString(R.string.tran_remove));
        builder.setNegativeButton(getString(R.string.d_cancel), (dialog, which) -> {
        });
        builder.setPositiveButton(getString(R.string.d_ok), (dialog, which) -> {
            mAdapter.remove(position);
            Map<String,String>map = new HashMap<>();
            map.put("accountOrCardNumber",number);
            Network.getInstance().post(NetUrl.DELRECENTTRANSFER,map);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
