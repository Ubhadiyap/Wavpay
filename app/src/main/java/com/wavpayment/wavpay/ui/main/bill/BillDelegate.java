package com.wavpayment.wavpay.ui.main.bill;

import android.graphics.Color;
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
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.main.bill.BillPresenterImpl;
import com.wavpayment.wavpay.service.main.bill.IBillContract;
import com.wavpayment.wavpay.ui.main.balance.TopAccDelegate;
import com.wavpayment.wavpay.ui.main.balance.WithdrawPayDelegate;
import com.wavpayment.wavpay.ui.main.order.TranAccDelegate;
import com.wavpayment.wavpay.ui.main.order.TranPayDelegate;
import com.wavpayment.wavpay.widgte.PopWinShare;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;

/**
 * 账单
 * Created by Administrator on 2017/12/29.
 */

public class BillDelegate extends SpongeDelegate implements IBillContract.View {

    private RecyclerView rvBill;
    private IBillContract.Presenter presenter;
    private BillAdapter mAdapter;
    private int pageIndex = 0;
    private int limit = 15;
    private PopWinShare winShare;
    private String count = "";

    @Override
    public Object setLayout() {
        return R.layout.delegate_bill;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        presenter = new BillPresenterImpl(this);
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        $(R.id.tv_right).setOnClickListener(view -> {
            winShare = new PopWinShare(_mActivity);
            winShare.setFocusable(true);
            winShare.showAsDropDown($(R.id.tv_right));
            winShare.setListener(id -> {
                pageIndex = 0;
                String name = "";
                switch (id) {
                    case R.id.rb_all:
                        count = "";
                        name = getString(R.string.bill_all);
                        break;
                    case R.id.rb_tran:
                        count = String.valueOf(1);
                        name = getString(R.string.bill_tran);
                        break;
                    case R.id.rb_top:
                        count = String.valueOf(2);
                        name = getString(R.string.bill_top);
                        break;
                    case R.id.rb_wit:
                        count = String.valueOf(3);
                        name = getString(R.string.bill_wit);
                        break;
                    case R.id.rb_pay:
                        name = getString(R.string.bill_pay);
                        count = String.valueOf(5);
                        break;
                }
                SpongeLoader.showLoading(_mActivity);
                ((TextView) $(R.id.tv_right)).setText(name);
                presenter.all(String.valueOf(pageIndex), String.valueOf(limit), count);
                winShare.dismiss();
                winShare = null;
            });
        });
        ((TextView) $(R.id.tv_title)).setText(getString(R.string.bill_title));
        ((TextView) $(R.id.tv_right)).setText(getString(R.string.bill_all));
        ((TextView) $(R.id.tv_right)).setTextColor(Color.BLUE);
        ((TextView) $(R.id.tv_right)).setTextSize(20);
        rvBill = $(R.id.rv_bill);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView(rvBill);
        SpongeLoader.showLoading(_mActivity);
        Sponge.getHandler().postDelayed(() -> presenter.all(String.valueOf(pageIndex), String.valueOf(limit), count), 500);
    }

    @Override
    public void success(String response) {
        SpongeLoader.stopLoading();
        final JSONObject page = JSON.parseObject(response).getJSONObject("page");
        final JSONArray array = page.getJSONArray("content");
        if (pageIndex == 0) {
            BillDataConverter converter = new BillDataConverter();
            mAdapter = BillAdapter.create(converter.setJsonData(response));
            rvBill.setAdapter(mAdapter);
            mAdapter.setOnLoadMoreListener(() -> {
                pageIndex++;
                presenter.all(String.valueOf(pageIndex), String.valueOf(limit), count);
            }, rvBill);
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
                        getSupportDelegate().start(tp);
                    } else {
                        final TranAccDelegate ta = new TranAccDelegate();
                        ta.setOrderId(orderId);
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
            mAdapter.addData(converter.setJsonData(response).convert());
        }

    }
}
