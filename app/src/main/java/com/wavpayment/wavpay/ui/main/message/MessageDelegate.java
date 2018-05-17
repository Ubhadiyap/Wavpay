package com.wavpayment.wavpay.ui.main.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.balance.TopAccDelegate;
import com.wavpayment.wavpay.ui.main.balance.WithdrawPayDelegate;
import com.wavpayment.wavpay.ui.main.bill.BillDataConverter;
import com.wavpayment.wavpay.ui.main.bill.BillFields;
import com.wavpayment.wavpay.ui.main.order.TranAccDelegate;
import com.wavpayment.wavpay.ui.main.order.TranPayDelegate;
import com.wavpayment.wavpay.utils.Utils;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.HashMap;
import java.util.Map;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


/**
 * 未读消息
 * Created by Administrator on 2017/11/24.
 */

public class MessageDelegate extends SpongeDelegate {
    private RecyclerView RECYCLERVIEW;
    private MessagesAdapter mAdapter;

    private int pageIndex = 0;
    private int limit = 15;

    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("Message");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        RECYCLERVIEW = $(R.id.rv_common);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView(RECYCLERVIEW);
        Sponge.getHandler().postDelayed(() -> network(), 500);
    }

    private void network() {
        SpongeLoader.showLoading(_mActivity);
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("limit", String.valueOf(limit));
        Network.getInstance()
                .post(NetUrl.NOREADBILLS,map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                       if (Utils.isJSONValid(response.body())){
                           final int code = JSON.parseObject(response.body()).getIntValue("statusCode");
                           final JSONObject page = JSON.parseObject(response.body()).getJSONObject("page");
                           final JSONArray array = page.getJSONArray("content");
                           SpongeLoader.stopLoading();
                           if (code == 200) {
                               if (pageIndex == 0) {
                                   BillDataConverter converter = new BillDataConverter();
                                   mAdapter = MessagesAdapter.create(converter.setJsonData(response.body()));
                                   RECYCLERVIEW.setAdapter(mAdapter);
                                   mAdapter.setOnLoadMoreListener(() -> {
                                       pageIndex++;
                                       network();
                                   }, RECYCLERVIEW);
                                   if (array.size() < limit) {
                                       mAdapter.loadMoreEnd(true);
                                   }
                                   mAdapter.setOnItemClickListener((adapter, view, position) -> {
                                       final String res = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
                                       final JSONObject it = JSON.parseObject(res).getJSONObject("user");
                                       String payAccount = it.getString("account");//设置当前账号
                                       MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
                                       final String acc = entity.getField(BillFields.payAccount);
                                       final int tradeType = entity.getField(BillFields.tradeType);
                                       final String orderId = entity.getField(BillFields.orderId);
                                       if (tradeType == 2) {
                                           final TopAccDelegate top = new TopAccDelegate();
                                           top.setOrderId(orderId);
                                           getSupportDelegate().start(top);
                                       } else if (tradeType == 3) {
                                           WithdrawPayDelegate wpd = new WithdrawPayDelegate();
                                           wpd.setOrderId(orderId);
                                           getSupportDelegate().start(wpd);
                                       } else {
                                           if (acc.equals(payAccount)) {
                                               final TranPayDelegate tp = new TranPayDelegate();
                                               tp.setOrderId(orderId);
                                               tp.setRead(true);
                                               getSupportDelegate().start(tp);
                                           } else {
                                               final TranAccDelegate ta = new TranAccDelegate();
                                               ta.setOrderId(orderId);
                                               ta.setRead(true);
                                               getSupportDelegate().start(ta);
                                           }
                                       }
                                   });
                               } else {
                                   mAdapter.loadMoreComplete();
                                   if (array.size() < limit) {
                                       mAdapter.loadMoreEnd();
                                   }
                                   BillDataConverter converter = new BillDataConverter();
                                   mAdapter.addData(converter.setJsonData(response.body()).convert());
                               }
                           }
                       }
                    }
                });
    }

}
