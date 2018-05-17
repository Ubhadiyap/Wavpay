package com.hbyundu.shop.ui.order.repay;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.order.OrderRepayAPI;
import com.hbyundu.shop.rest.api.order.OrderRepayListAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderRepayItemModel;
import com.hbyundu.shop.rest.model.order.OrderRepayResultModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.repay.Uc_Quick_RefundActLoan_ListModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.SDFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderRepayActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;

    private TextView mTxtTotalRepayMoney;

    private long mOrderId;

    private OrderRepayBorrowRepayListAdapter mOrderRepayListAdapter;

    private List<Uc_Quick_RefundActLoan_ListModel> mListLoanListModel = new ArrayList<>();

    private String mIds;

    private float mTotalMoneyNeedpay = 0;

    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_repay);

        mOrderId = getIntent().getLongExtra("orderId", 0);

        initTitleBar();
        initViews();
        getData();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.repay);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mTxtTotalRepayMoney = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_total_repay_money);

        mListView = (ListView) findViewById(R.id.act_repay_borrow_repay_lsv_repay_record);

        mOrderRepayListAdapter = new OrderRepayBorrowRepayListAdapter(mListLoanListModel, this, new RepayBorrowRepayActivity_RepayBorrowRepayListAdapterListener());
        mListView.setAdapter(mOrderRepayListAdapter);

        findViewById(R.id.act_repay_borrow_repay_btn_confirm_repay).setOnClickListener(this);
    }

    private void getData() {
        OrderRepayListAPI.getInstance().orderRepayList(mOrderId, new SubscriberOnListener<List<OrderRepayItemModel>>() {
            @Override
            public void onSucceed(List<OrderRepayItemModel> data) {
                mListLoanListModel.clear();
                mListLoanListModel.addAll(convertData(data));
                mOrderRepayListAdapter.updateListViewData(mListLoanListModel);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.act_repay_borrow_repay_btn_confirm_repay) {
            repayAction();
        }
    }

    private void repayAction() {
        if (mIds == null || mIds.length() == 0) {
            return;
        }

        OrderRepayAPI.getInstance().orderRepay(Long.valueOf(mIds), new SubscriberOnListener<OrderRepayResultModel>() {
            @Override
            public void onSucceed(OrderRepayResultModel data) {
                mProgressHUD.dismissImmediately();
                if (data.state == 0 || data.state == 1) {
                    mProgressHUD.showSuccessWithStatus(getString(R.string.repay_success));
                    mProgressHUD.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(SVProgressHUD svProgressHUD) {
                            finish();
                        }
                    });
                } else if (data.state == 3) {
                    mProgressHUD.showErrorWithStatus(getString(R.string.remainder_not_enough));
                } else {
                    mProgressHUD.showErrorWithStatus(getString(R.string.repay_failure));
                }
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.repay_failure));
            }
        });

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }

    private List<Uc_Quick_RefundActLoan_ListModel> convertData(List<OrderRepayItemModel> data) {
        List<Uc_Quick_RefundActLoan_ListModel> list = new ArrayList<>();
        for (OrderRepayItemModel model : data) {
            Uc_Quick_RefundActLoan_ListModel item = new Uc_Quick_RefundActLoan_ListModel();
            item.setId(String.valueOf(model.id));
            item.setMonth_need_all_repay_money(String.valueOf(model.yuegong));
            item.setMonth_need_all_repay_money_format(MoneyUtils.formatMoney(model.yuegong, Locale.ENGLISH));
            item.setRepay_day_format(model.repayDate);
            item.setCanSelect(model.status == 0);
            if (model.status == 1) { // 已还
                item.setMonth_has_repay_money_all_format(MoneyUtils.formatMoney(model.yuegong, Locale.ENGLISH));
                item.setMonth_repay_money_format(MoneyUtils.formatMoney(0, Locale.ENGLISH));
            } else {
                item.setMonth_repay_money_format(MoneyUtils.formatMoney(model.yuegong, Locale.ENGLISH));
                item.setMonth_has_repay_money_all_format(MoneyUtils.formatMoney(0, Locale.ENGLISH));
            }

            list.add(item);
        }

        return list;
    }

    class RepayBorrowRepayActivity_RepayBorrowRepayListAdapterListener implements OrderRepayBorrowRepayListAdapter.RepayBorrowRepayListAdapterListener {
        @Override
        public void onCheckedChange(String ids, float totalMoneyNeedpay) {
            mIds = ids;
            mTotalMoneyNeedpay = totalMoneyNeedpay;
            try {
                mTxtTotalRepayMoney.setText(SDFormatUtil.formatMoneyChina(String.valueOf(totalMoneyNeedpay)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
