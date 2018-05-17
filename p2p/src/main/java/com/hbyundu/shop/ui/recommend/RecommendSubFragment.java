package com.hbyundu.shop.ui.recommend;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.recommend.RecommendGoodsAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.recommend.RecommendGoodsModel;
import com.hbyundu.shop.ui.goods.detail.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendSubFragment extends Fragment {

    private View mRootView;

    private RecyclerView mRecyclerView;

    private MaterialRefreshLayout mRefreshLayout;

    private RecommendAdapter mRecommendAdapter;

    private int mCategoryId;

    private List<RecommendGoodsModel> mGoods = new ArrayList<>();

    public static RecommendSubFragment newInstance(int categoryId) {
        RecommendSubFragment fragment = new RecommendSubFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public RecommendSubFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != mRootView) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflater.inflate(R.layout.fragment_recommend_sub, container, false);
            initView(mRootView);
            initParams();
        }

        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mGoods.isEmpty()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.autoRefresh();
                    }
                }, 300);
            }
        }
    }

    private void initView(View parent) {
        mRecyclerView = parent.findViewById(R.id.fragment_recommend_sub_rv);
        mRecommendAdapter = new RecommendAdapter(getActivity(), mGoods);
        mRecyclerView.setAdapter(mRecommendAdapter);
        mRecommendAdapter.setOnItemClickListener(new RecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("goodsId", mGoods.get(position).goodsId);
                startActivity(intent);
            }
        });

        mRefreshLayout = parent.findViewById(R.id.fragment_recommend_refresh_layout);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefreshLoadMore();
                    }
                }, 1000);
            }
        });
    }

    private void initParams() {
        Bundle bundle = getArguments();
        mCategoryId = bundle.getInt("categoryId");
    }

    private void refreshData() {
        RecommendGoodsAPI.getInstance().goods(mCategoryId, new SubscriberOnListener<List<RecommendGoodsModel>>() {
            @Override
            public void onSucceed(List<RecommendGoodsModel> data) {
                mGoods.clear();
                mGoods.addAll(data);

                setRecyclerViewStyle();
                mRecommendAdapter.notifyDataSetChanged();

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

    private void setRecyclerViewStyle() {
        int hotCount = 0;
        for (RecommendGoodsModel item : mGoods) {
            if (item.isHot == 1) {
                hotCount++;
            }
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                RecommendGoodsModel item = mGoods.get(position);
                return item.isHot == 1 ? 2 : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);

        HashMap<String, Integer> spacesHashMap = new HashMap<>();
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 20);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 10);
        spacesHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 10);

        RecyclerView.ItemDecoration itemDecoration = mRecyclerView.getItemDecorationAt(0);
        if (itemDecoration != null) {
            mRecyclerView.removeItemDecoration(itemDecoration);
        }
        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(spacesHashMap, hotCount));
    }

    class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
        HashMap<String, Integer> mSpaceValueMap;
        int hotCount = 0;

        public static final String TOP_DECORATION = "top_decoration";
        public static final String BOTTOM_DECORATION = "bottom_decoration";
        public static final String LEFT_DECORATION = "left_decoration";
        public static final String RIGHT_DECORATION = "right_decoration";

        public RecyclerViewSpacesItemDecoration(HashMap<String, Integer> spaceValueMap, int hotCount) {
            this.mSpaceValueMap = spaceValueMap;
            this.hotCount = hotCount;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildLayoutPosition(view);

            if (mSpaceValueMap.get(TOP_DECORATION) != null) {
                outRect.top = mSpaceValueMap.get(TOP_DECORATION);
            }
            if (mSpaceValueMap.get(LEFT_DECORATION) != null) {
                if (pos < hotCount || (pos - hotCount) % 2 == 0) {
                    outRect.left = 0;
                } else {
                    outRect.left = mSpaceValueMap.get(LEFT_DECORATION);
                }
            }
            if (mSpaceValueMap.get(RIGHT_DECORATION) != null) {
                if (pos < hotCount || (pos - hotCount) % 2 == 1) {
                    outRect.right = 0;
                } else {
                    outRect.right = mSpaceValueMap.get(RIGHT_DECORATION);
                }
            }
            if (mSpaceValueMap.get(BOTTOM_DECORATION) != null) {
                if (pos < (hotCount - 1)) {
                    outRect.bottom = 0;
                } else {
                    outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION);
                }
            }
        }
    }
}
