package com.hbyundu.shop.ui.credit.withdraw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.launcher.LoginAPI;
import com.hbyundu.shop.rest.api.repay.WithdrawAPI;
import com.hbyundu.shop.rest.api.repay.WithdrawListAPI;
import com.hbyundu.shop.rest.api.wav.WavBelowAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;
import com.hbyundu.shop.rest.model.repay.RepayItemModel;
import com.hbyundu.shop.rest.model.repay.WithdrawResultModel;
import com.hbyundu.shop.ui.BaseActivity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class WithDrawListActivity extends BaseActivity implements WithDrawListAdapter.OnWithDrawClickListener {

    private PullToRefreshListView mListView;
    private List<RepayItemModel> mData = new ArrayList<>();
    private WithDrawListAdapter mAdapter;
    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_list);

        initToolbar();
        initViews();
        loadData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.withdraw);
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
        mListView = findViewById(R.id.activity_withdraw_list_lv);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });

        mAdapter = new WithDrawListAdapter(this, mData);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);

        mProgressHUD = new SVProgressHUD(this);
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setRefreshing();
            }
        }, 300);
    }

    private void refreshData() {
        WithdrawListAPI.getInstance().refresh(UserManager.getInstance(getApplicationContext()).getUid(), 4, new SubscriberOnListener<List<RepayItemModel>>() {
            @Override
            public void onSucceed(List<RepayItemModel> data) {
                mData.clear();
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();

                mListView.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                mListView.onRefreshComplete();
            }
        });
    }

    private void loadMoreData() {
        WithdrawListAPI.getInstance().loadMore(UserManager.getInstance(getApplicationContext()).getUid(), 4, new SubscriberOnListener<List<RepayItemModel>>() {
            @Override
            public void onSucceed(List<RepayItemModel> data) {
                mData.addAll(data);
                mAdapter.notifyDataSetChanged();

                mListView.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                mListView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onWithDrawClick(final int position) {
        View view = View.inflate(this, R.layout.view_withdraw_input, null);
        final EditText moneyEditText = (EditText) view.findViewById(R.id.view_withdraw_input_money_et);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.view_withdraw_input_password_et);

        new AlertDialog.Builder(this).setTitle(R.string.please_input_login_password)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (moneyEditText.getText().length() == 0
                                || passwordEditText.getText().length() == 0) {
                            return;
                        }

                        checkLoginAuth(moneyEditText.getText().toString(), passwordEditText.getText().toString(), position);

                        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }).show();

    }

    private void checkLoginAuth(final String money, String password, final int position) {
        String username = UserManager.getInstance(getApplicationContext()).getUsername();
        LoginAPI.getInstance().login(username, password, new SubscriberOnListener<LoginResultModel>() {
            @Override
            public void onSucceed(LoginResultModel data) {
                withdraw(money, position);
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.operation_failed));
            }
        });
    }

    private void belowAction(String money, int position) {
        RepayItemModel itemModel = mData.get(position);
        WavBelowAPI.getInstance().below(itemModel.orId, money, new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showSuccessWithStatus(getString(R.string.operation_succeeded));

                refreshData();
                EventBus.getDefault().post("", "RefreshUserInfo");
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.operation_failed));
            }
        });
    }

    private void withdraw(final String money, final int position) {
        RepayItemModel itemModel = mData.get(position);
        WithdrawAPI.getInstance().withdraw(UserManager.getInstance(getApplicationContext()).getUid(), itemModel.orId, money, new SubscriberOnListener<WithdrawResultModel>() {
            @Override
            public void onSucceed(WithdrawResultModel data) {
                belowAction(money, position);
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.operation_failed));
            }
        });
    }
}
