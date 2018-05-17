package com.hbyundu.shop.ui.credit.repay;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.repay.RepayListAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.RepayItemModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.NumberUtils;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 个人中心偿还贷款还款列表
 *
 * @author yhz
 */
public class RepayBorrowListFragment extends Fragment {

    public static final int STATE = 4;

//    @ViewInject(id = R.id.frag_repay_borrow_list_lsv_content)
    private PullToRefreshListView mLsvContent = null;

    private List<Uc_RefundActItemModel> mListModel = null;

    private RepayBorrowFragAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uc_frag_repay_borrow_list, container, false);
//        SDIoc.injectView(this, view);
        mLsvContent = view.findViewById(R.id.frag_repay_borrow_list_lsv_content);

        init();
        return view;
    }

    private void init() {
        bindDefaultData();
        initPullListView();
        loadData();
    }

    private void bindDefaultData() {
        mListModel = new ArrayList<Uc_RefundActItemModel>();
        mAdapter = new RepayBorrowFragAdapter(RepayBorrowListFragment.this, mListModel);
        mLsvContent.setAdapter(mAdapter);
    }

    private void initPullListView() {
        mLsvContent.setMode(Mode.BOTH);
        mLsvContent.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });
        mLsvContent.setRefreshing(false);
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLsvContent.setRefreshing();
            }
        }, 300);
    }

    private void refreshData() {
        RepayListAPI.getInstance().refresh(UserManager.getInstance(getActivity().getApplicationContext()).getUid(), STATE, new SubscriberOnListener<List<RepayItemModel>>() {
            @Override
            public void onSucceed(List<RepayItemModel> data) {
                mListModel.clear();
                mListModel.addAll(convertData(data));
                mAdapter.notifyDataSetChanged();

                mLsvContent.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                mLsvContent.onRefreshComplete();
            }
        });
    }

    private void loadMoreData() {
        RepayListAPI.getInstance().loadMore(UserManager.getInstance(getActivity().getApplicationContext()).getUid(), STATE, new SubscriberOnListener<List<RepayItemModel>>() {
            @Override
            public void onSucceed(List<RepayItemModel> data) {
                mListModel.addAll(convertData(data));
                mAdapter.notifyDataSetChanged();

                mLsvContent.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                mLsvContent.onRefreshComplete();
            }
        });
    }

    private List<Uc_RefundActItemModel> convertData(List<RepayItemModel> data) {
        List<Uc_RefundActItemModel> list = new ArrayList<>();
        for (RepayItemModel model : data) {
            Uc_RefundActItemModel item = new Uc_RefundActItemModel();
            item.setId(String.valueOf(model.dealId));
            item.setName(model.title);
            item.setSub_name(model.title);
            item.setBorrow_amount_format(MoneyUtils.formatMoney(model.borrowAmount, Locale.ENGLISH));
            item.setRate(NumberUtils.formatRate(model.rate));
            item.setRepay_time(String.valueOf(model.repayLimit));
            item.setRepay_time_type(String.valueOf(model.repayTimeType ? 1 : 0));
            item.setTrue_month_repay_money(MoneyUtils.formatMoney(model.repay, Locale.ENGLISH));
            try {
                item.setNext_repay_time_format(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(model.date)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(item);
        }

        return list;
    }
}