package com.hbyundu.shop.ui.goods.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.goods.GoodsSearchAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsListModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.goods.detail.GoodsDetailActivity;
import com.hbyundu.shop.ui.goods.list.GoodsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoodsSearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    private EditText mKeywordEditText;

    private MaterialRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private GoodsListAdapter mGoodsListAdapter;

    private List<GoodsListModel> mGoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);

        initTitleBar();
        initViews();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        mKeywordEditText = (EditText) findViewById(R.id.activity_goods_search_et);
        mKeywordEditText.setOnEditorActionListener(this);
        mKeywordEditText.addTextChangedListener(this);

        findViewById(R.id.activity_goods_search_btn).setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_goods_search_rv);
        mGoodsListAdapter = new GoodsListAdapter(this, mGoods);
        mRecyclerView.setAdapter(mGoodsListAdapter);
        mGoodsListAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GoodsSearchActivity.this, GoodsDetailActivity.class);
                intent.putExtra("goodsId", mGoods.get(position).id);
                startActivity(intent);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        HashMap<String, Integer> spacesHashMap = new HashMap<>();
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 10);
        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(spacesHashMap));

        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.activity_goods_search_refresh_layout);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_goods_search_btn) {
            searchAction();
        }
    }

    private void searchAction() {
        if (mKeywordEditText.getText().toString().length() == 0) {
            return;
        }

        refresh();
        hideIMM();
    }

    private void refresh() {
        GoodsSearchAPI.getInstance().refresh(mKeywordEditText.getText().toString(), new SubscriberOnListener<List<GoodsListModel>>() {
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
        GoodsSearchAPI.getInstance().loadMore(mKeywordEditText.getText().toString(), new SubscriberOnListener<List<GoodsListModel>>() {
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

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch(actionId){
            case EditorInfo.IME_ACTION_SEARCH:
                searchAction();
                break;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().length() == 0) {
            mGoods.clear();
            mGoodsListAdapter.notifyDataSetChanged();
        }
    }

    public void hideIMM() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mKeywordEditText.getWindowToken(), 0);
    }

    class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
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
