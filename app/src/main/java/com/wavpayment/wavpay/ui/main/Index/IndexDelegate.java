package com.wavpayment.wavpay.ui.main.Index;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.blankj.utilcode.util.ToastUtils;
import com.hbyundu.shop.ui.home.HomeActivity;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;

import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonEntity;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.service.main.index.IIndexContract;
import com.wavpayment.wavpay.service.main.index.IndexPresenterImpl;
import com.wavpayment.wavpay.ui.main.BottomItemDelegate;
import com.wavpayment.wavpay.ui.main.accept.AcceptDelegate;
import com.wavpayment.wavpay.ui.main.balance.BalanceDelegate;
import com.wavpayment.wavpay.ui.main.bank.BankCardDelegate;
import com.wavpayment.wavpay.ui.main.bill.BillDelegate;
import com.wavpayment.wavpay.ui.main.pay.PayDelegate;
import com.wavpayment.wavpay.ui.main.transfer.TransferDelegate;
import com.wavpayment.wavpay.widgte.banner.BannerCreator;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;

import java.util.ArrayList;


public class IndexDelegate extends BottomItemDelegate implements IIndexContract.View{
    private TextView tvBalance;
    private RecyclerView rvIndex;
    private DataConverter CONVERTER;
    private IndexAdapter mAdapter;
    private IIndexContract.Presenter presenter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        presenter = new IndexPresenterImpl(this);
        tvBalance = $(R.id.tv_balance);
        rvIndex = $(R.id.rv_index);
        CONVERTER = new IndexDataConverter();
        $(R.id.ll_bill).setOnClickListener(v -> getParentDelegate().getSupportDelegate().start(new BillDelegate()));
        $(R.id.ll_top_up).setOnClickListener(v -> getParentDelegate().getSupportDelegate().start(new BalanceDelegate()));
        //更新余额,回调
        CallbackManager.getInstance().addCallback(CallbackType.BALANCE, args -> tvBalance.setText(""+args));
       $(R.id.tv_common_accept).setOnClickListener(v -> getParentDelegate().getSupportDelegate().start(new AcceptDelegate()));
       $(R.id.tv_common_pay).setOnClickListener(v -> getParentDelegate().getSupportDelegate().start(new PayDelegate()));
       $(R.id.ll_bank).setOnClickListener(v -> {
            final BankCardDelegate bank = new BankCardDelegate();
            getParentDelegate().getSupportDelegate().start(bank);
       });
       $(R.id.ll_transfer).setOnClickListener(v ->getParentDelegate().getSupportDelegate().start(new TransferDelegate()));
       $(R.id.iv_mark).setOnClickListener(v -> {});

        CallbackManager.getInstance().addCallback(CallbackType.DIALOG,args -> dialog(args.toString()));
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView(rvIndex);
        presenter.initView();
        presenter.banner();
    }

    @Override
    public void error(int code) {

    }

    @Override
    public void onInitView(ArrayList<CommonEntity> data) {
        CONVERTER.clearData();
        mAdapter = IndexAdapter.create(CONVERTER.setData(data), this);
        rvIndex.setAdapter(mAdapter);
    }

    @Override
    public void onBanner(ArrayList<String> es) {
        ConvenientBanner<String> convenientBanner = $(R.id.banner_recycler_item);

/*           BannerCreator.setDefault(
                convenientBanner,
                es,
                   position -> {ToastUtils.showShort("position="+position);}
                );*/

        BannerCreator.setDefault(
                convenientBanner,
                es,
                position -> {startActivity(new Intent(_mActivity, HomeActivity.class));
        });
        CommonHandler.getInstance().info();

    }


}
