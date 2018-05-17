package com.hbyundu.shop.ui.credit.apply;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.repay.LoanListAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.LoanItemModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.invest.list.DealsActItemModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by apple on 2017/12/3.
 */

public class ApplyLoanListActivity extends BaseActivity {

    private PullToRefreshListView mLsvDeals;

    private ApplyLoanListAdapter mAdapter;

    private List<DealsActItemModel> mListModel = new ArrayList<>();

    private int mSelectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_list);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_loans);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        initPullView();
        bindDefaultData();
    }

    private void initPullView() {
        mLsvDeals = (PullToRefreshListView) findViewById(R.id.activity_loan_list_lv);
        mLsvDeals.setMode(PullToRefreshBase.Mode.BOTH);
        mLsvDeals.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLsvDeals.setRefreshing();
            }
        }, 400);
    }

    private void bindDefaultData() {
        mAdapter = new ApplyLoanListAdapter(mListModel, this);
        mLsvDeals.setAdapter(mAdapter);
        mLsvDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListModel != null && mListModel.size() > id) {
                    if (mSelectedIndex != -1 && mSelectedIndex < mListModel.size()) {
                        mListModel.get(mSelectedIndex).setSelect(false);
                    }

                    mSelectedIndex = (int) id;
                    mListModel.get(mSelectedIndex).setSelect(true);
                    mAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(ApplyLoanListActivity.this, ApplyLoanDetailActivity.class);
                    intent.putExtra("dealId", Long.valueOf(((DealsActItemModel) mAdapter.getItem((int) id)).getId()));
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshData() {
        LoanListAPI.getInstance().refresh(UserManager.getInstance(getApplicationContext()).getUid(),
                new SubscriberOnListener<List<LoanItemModel>>() {
            @Override
            public void onSucceed(List<LoanItemModel> data) {
                mListModel.clear();
                mListModel.addAll(convertData(data));
                mAdapter.notifyDataSetChanged();

                mLsvDeals.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                mLsvDeals.onRefreshComplete();
            }
        });
    }

    private void loadMoreData() {
        LoanListAPI.getInstance().loadMore(UserManager.getInstance(getApplicationContext()).getUid(),
                new SubscriberOnListener<List<LoanItemModel>>() {
            @Override
            public void onSucceed(List<LoanItemModel> data) {
                mListModel.addAll(convertData(data));
                mAdapter.notifyDataSetChanged();

                mLsvDeals.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                mLsvDeals.onRefreshComplete();
            }
        });
    }


    private List<DealsActItemModel> convertData(List<LoanItemModel> data) {
        List<DealsActItemModel> deals = new ArrayList<>();
        for (LoanItemModel invest : data) {
            DealsActItemModel deal = new DealsActItemModel();
            deal.setId(String.valueOf(invest.dealId));
            deal.setName(invest.title);
            deal.setDeal_status(this, String.valueOf(invest.dealStatus));
            deal.setRepay_time(String.valueOf(invest.repayLimit));
            deal.setRate(String.valueOf(invest.rate));
            deal.setRate_foramt(NumberUtils.formatRate(invest.rate));
            deal.setBorrow_amount_format(MoneyUtils.formatMoney(invest.borrowAmount, Locale.ENGLISH));
            deal.setRepay_time_type(this, String.valueOf(invest.repayTimeType));
            deal.isDelete = invest.isDelete;
            deal.deleteMsg = invest.deleteMsg;
            deal.publishWait = invest.publishWait;
            deal.customStatus = invest.customStatus;
            deals.add(deal);
        }
        return deals;
    }
}
