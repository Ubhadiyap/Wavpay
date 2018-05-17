package com.hbyundu.shop.ui.recommend;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.recommend.RecommendCategoryAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.recommend.RecommendCategoryModel;
import com.hbyundu.shop.ui.goods.search.GoodsSearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment implements View.OnClickListener {

    private View mRootView;

    private TabLayout mTabLayout;

    private ViewPager mContentViewPager;

    private List<Fragment> mTabFragments;

    private ContentPagerAdapter mContentAdapter;

    private List<RecommendCategoryModel> mCategoryArray = new ArrayList<>();

    public RecommendFragment() {
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
            mRootView = inflater.inflate(R.layout.fragment_recommend, container, false);
            initContentView(mRootView);
            initTabView(mRootView);
            initData();
        }

        return mRootView;
    }

    private void initTabView(View parentView) {
        mTabLayout = parentView.findViewById(R.id.fragment_recommend_tl);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.colorRecommendTabTextNormal),
                ContextCompat.getColor(getActivity(), R.color.colorRecommendTabTextSelected));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        ViewCompat.setElevation(mTabLayout, 0);
        mTabLayout.setupWithViewPager(mContentViewPager);

        parentView.findViewById(R.id.fragment_recommend_search_tv).setOnClickListener(this);
    }

    private void initContentView(View parentView) {
        mContentViewPager = parentView.findViewById(R.id.fragment_recommend_content_vp);
        mContentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        mContentViewPager.setAdapter(mContentAdapter);
    }

    private void initData() {
        RecommendCategoryAPI.getInstance().category(new SubscriberOnListener<List<RecommendCategoryModel>>() {
            @Override
            public void onSucceed(List<RecommendCategoryModel> data) {
                mCategoryArray.addAll(data);
                setData();
                mTabLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void setData() {
        mTabFragments = new ArrayList<>();
        for (RecommendCategoryModel model : mCategoryArray) {
            mTabFragments.add(RecommendSubFragment.newInstance(model.cateId));
        }
        mContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_recommend_search_tv) {
            searchAction();
        }
    }

    private void searchAction() {
        Intent intent = new Intent(getActivity(), GoodsSearchActivity.class);
        startActivity(intent);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mTabFragments.get(position);
        }

        @Override
        public int getCount() {
            return mCategoryArray.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCategoryArray.get(position).cateName;
        }
    }

}
