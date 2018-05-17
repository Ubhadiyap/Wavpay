package com.hbyundu.shop.ui.goods.list;

import android.content.Intent;
import android.graphics.Rect;
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
import com.hbyundu.shop.rest.api.goods.GoodsListAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsListModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.goods.detail.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoodsListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private MaterialRefreshLayout mRefreshLayout;

    private GoodsListAdapter mGoodsListAdapter;

    private List<GoodsListModel> mGoods = new ArrayList<>();

    private long mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        String title = getIntent().getStringExtra("categoryName");
        mCategoryId = getIntent().getLongExtra("categoryId", 0);

        initTitleBar(title);
        initView();
        loadData();
    }

    private void initTitleBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_goods_list_rv);
        mGoodsListAdapter = new GoodsListAdapter(getApplicationContext(), mGoods);
        mRecyclerView.setAdapter(mGoodsListAdapter);
        mGoodsListAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GoodsListActivity.this, GoodsDetailActivity.class);
                intent.putExtra("goodsId", mGoods.get(position).id);
                startActivity(intent);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        HashMap<String, Integer> spacesHashMap = new HashMap<>();
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 10);
        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(spacesHashMap));

        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.activity_goods_list_refresh_layout);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                loadMore();
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

    private void refresh() {
        GoodsListAPI.getInstance().refresh(mCategoryId, new SubscriberOnListener<List<GoodsListModel>>() {
            @Override
            public void onSucceed(List<GoodsListModel> data) {
                mGoods.clear();
                mGoods.addAll(data);
                mGoodsListAdapter.notifyDataSetChanged();

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

    private void loadMore() {
        GoodsListAPI.getInstance().loadMore(mCategoryId, new SubscriberOnListener<List<GoodsListModel>>() {
            @Override
            public void onSucceed(List<GoodsListModel> data) {
                mGoods.addAll(data);
                mGoodsListAdapter.notifyDataSetChanged();

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

    static class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
        private HashMap<String, Integer> mSpaceValueMap;
        public static final String TOP_DECORATION = "top_decoration";
        public static final String BOTTOM_DECORATION = "bottom_decoration";
        public static final String LEFT_DECORATION = "left_decoration";
        public static final String RIGHT_DECORATION = "right_decoration";

        public RecyclerViewSpacesItemDecoration(HashMap<String, Integer> spaceValueMap) {
            this.mSpaceValueMap = spaceValueMap;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildLayoutPosition(view);

            if (mSpaceValueMap.get(TOP_DECORATION) != null) {
                outRect.top = mSpaceValueMap.get(TOP_DECORATION);
            }
            if (mSpaceValueMap.get(LEFT_DECORATION) != null) {
                if (pos % 2 == 0) {
                    outRect.left = 0;
                } else {
                    outRect.left = mSpaceValueMap.get(LEFT_DECORATION);
                }
            }
            if (mSpaceValueMap.get(RIGHT_DECORATION) != null) {
                if (pos % 2 == 1) {
                    outRect.right = 0;
                } else {
                    outRect.right = mSpaceValueMap.get(RIGHT_DECORATION);
                }
            }
            if (mSpaceValueMap.get(BOTTOM_DECORATION) != null) {
                outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION);
            }
        }
    }
}
