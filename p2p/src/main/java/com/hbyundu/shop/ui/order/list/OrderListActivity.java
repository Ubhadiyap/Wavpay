package com.hbyundu.shop.ui.order.list;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.order.OrdersAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderItemModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.order.detail.OrderDetailActivity;
import com.hbyundu.shop.vendor.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private MaterialRefreshLayout mRefreshLayout;

    private OrderListAdapter mOrderListAdapter;

    private List<OrderItemModel> mOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initTitleBar();
        initView();
        loadData();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_order);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_order_list_rv);
        mOrderListAdapter = new OrderListAdapter(this, mOrders);
        mRecyclerView.setAdapter(mOrderListAdapter);
        mOrderListAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderId", mOrders.get(position).orderId);
                startActivity(intent);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, 10, getResources().getColor(R.color.colorDivide)));

        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.activity_order_list_refresh_layout);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                loadMoreData();
            }
        });
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.autoRefresh();
            }
        }, 300);
    }

    private void refreshData() {
        OrdersAPI.getInstance().refresh(UserManager.getInstance(getApplicationContext()).getUid(), new SubscriberOnListener<List<OrderItemModel>>() {
            @Override
            public void onSucceed(List<OrderItemModel> data) {
                mOrders.clear();
                mOrders.addAll(data);
                mOrderListAdapter.notifyDataSetChanged();

                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishRefreshLoadMore();
            }

            @Override
            public void onError(String msg) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishRefreshLoadMore();
            }
        });
    }

    private void loadMoreData() {
        OrdersAPI.getInstance().loadMore(UserManager.getInstance(getApplicationContext()).getUid(), new SubscriberOnListener<List<OrderItemModel>>() {
            @Override
            public void onSucceed(List<OrderItemModel> data) {
                mOrders.addAll(data);
                mOrderListAdapter.notifyDataSetChanged();

                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishRefreshLoadMore();
            }

            @Override
            public void onError(String msg) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishRefreshLoadMore();
            }
        });
    }
}
