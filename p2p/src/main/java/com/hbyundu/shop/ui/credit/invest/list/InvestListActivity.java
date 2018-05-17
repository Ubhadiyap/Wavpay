package com.hbyundu.shop.ui.credit.invest.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.invest.InvestListAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestListModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.invest.detail.InvestProjectDetailActivity;
import com.hbyundu.shop.ui.credit.invest.recharge.RechargeActivity;
import com.hbyundu.shop.ui.order.list.OrderListActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by apple on 2017/12/3.
 */

public class InvestListActivity extends BaseActivity {

    private PullToRefreshListView mLsvDeals;

    private InvestListAdapter mAdapter;

    private List<DealsActItemModel> mListModel = new ArrayList<>();

    private int mSelectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_list);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.investment_loan);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_invest) {
            investAction();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invest_list, menu);
        return true;
    }

    private void initViews() {
        initPullView();
        bindDefaultData();
    }

    private void initPullView() {
        mLsvDeals = (PullToRefreshListView) findViewById(R.id.frag_borrow_invest_lsv_deals);
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
        mAdapter = new InvestListAdapter(mListModel, this);
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

                    Intent intent = new Intent(InvestListActivity.this, InvestProjectDetailActivity.class);
                    intent.putExtra("dealId", Long.valueOf(((DealsActItemModel) mAdapter.getItem((int) id)).getId()));
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshData() {
        InvestListAPI.getInstance().refresh(new SubscriberOnListener<List<InvestListModel>>() {
            @Override
            public void onSucceed(List<InvestListModel> data) {
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
        InvestListAPI.getInstance().loadMore(new SubscriberOnListener<List<InvestListModel>>() {
            @Override
            public void onSucceed(List<InvestListModel> data) {
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


    private List<DealsActItemModel> convertData(List<InvestListModel> data) {
        List<DealsActItemModel> deals = new ArrayList<>();
        for (InvestListModel invest : data) {
            DealsActItemModel deal = new DealsActItemModel();
            deal.setId(String.valueOf(invest.dealId));
            deal.setName(invest.dealName);
            deal.setDeal_status(this, invest.dealStatus);
            deal.setRepay_time(String.valueOf(invest.repayTime));
            deal.setRate(String.valueOf(invest.rate));
            deal.setRate_foramt(NumberUtils.formatRate(invest.rate));
            deal.setBorrow_amount_format(MoneyUtils.formatMoney(invest.borrowAmount, Locale.ENGLISH));
            deal.setProgress_point(invest.baifenbi);
            deal.setRepay_time_type(this, String.valueOf(invest.repayTimeType));
            deals.add(deal);
        }
        return deals;
    }

    private void investAction() {
        Intent intent = new Intent(InvestListActivity.this, RechargeActivity.class);
        startActivity(intent);
    }
}
