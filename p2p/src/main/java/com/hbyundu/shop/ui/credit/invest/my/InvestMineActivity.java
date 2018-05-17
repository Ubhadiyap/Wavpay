package com.hbyundu.shop.ui.credit.invest.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.invest.InvestMineListAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestListModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.invest.list.DealsActItemModel;
import com.hbyundu.shop.ui.credit.invest.list.InvestListAdapter;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.NumberUtils;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 我都投资界面(会员中心)
 *
 * @author js02
 */
public class InvestMineActivity extends BaseActivity {

//    @ViewInject(id = R.id.act_my_invest_lsv_deals)
    private PullToRefreshListView mLsvDeals = null;

    private InvestListAdapter mAdapter = null;

    private List<DealsActItemModel> mListModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invest);
//        SDIoc.injectView(this);

        mLsvDeals = (PullToRefreshListView) findViewById(R.id.act_my_invest_lsv_deals);

        initToolbar();
        initPullListView();
        bindDefaultData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_invest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPullListView() {
        mLsvDeals.setMode(Mode.BOTH);
        mLsvDeals.setOnRefreshListener(new OnRefreshListener2<ListView>() {
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
        }, 300);
    }

    private void bindDefaultData() {
        mAdapter = new InvestListAdapter(mListModel, this);
        mLsvDeals.setAdapter(mAdapter);
        mLsvDeals.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InvestMineActivity.this, InvestMineProjectDetailActivity.class);
                intent.putExtra("dealId", Long.valueOf(mListModel.get(position - mLsvDeals.getRefreshableView().getHeaderViewsCount()).getId()));
                startActivity(intent);
            }

        });
    }

    private void refreshData() {
        InvestMineListAPI.getInstance().refresh(UserManager.getInstance(getApplicationContext()).getUid(), new SubscriberOnListener<List<InvestListModel>>() {
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
        InvestMineListAPI.getInstance().loadMore(UserManager.getInstance(getApplicationContext()).getUid(), new SubscriberOnListener<List<InvestListModel>>() {
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
}