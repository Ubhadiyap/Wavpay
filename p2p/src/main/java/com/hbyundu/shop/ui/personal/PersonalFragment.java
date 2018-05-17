package com.hbyundu.shop.ui.personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.user.UserInfoAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.user.UserInfoModel;
import com.hbyundu.shop.ui.credit.apply.ApplyLoanListActivity;
import com.hbyundu.shop.ui.credit.invest.my.InvestMineActivity;
import com.hbyundu.shop.ui.credit.repay.RepayBorrowListActivity;
import com.hbyundu.shop.ui.credit.withdraw.WithDrawListActivity;
import com.hbyundu.shop.ui.launcher.ChangePwdChoiceActivity;
import com.hbyundu.shop.ui.order.list.OrderListActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.PackageUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {

    private View mRootView;

    private TextView mRemainderTextView;

    private TextView mUsernameTextView;

    public PersonalFragment() {
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
            mRootView = inflater.inflate(R.layout.fragment_personal, container, false);
            initTitleBar(mRootView);
            initViews(mRootView);
            EventBus.getDefault().register(this);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserInfo();
            }
        }, 300);

        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initTitleBar(View parentView) {
        Toolbar toolbar = parentView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.tab_personal);
    }

    private void initViews(View parentView) {
        parentView.findViewById(R.id.fragment_person_repayment_ll).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_person_my_order_ll).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_person_my_invest_ll).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_person_change_pwd_ll).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_person_logout_btn).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_personal_remainder_title_tv).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_personal_remainder_tv).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_person_loan_ll).setOnClickListener(this);

        mUsernameTextView = parentView.findViewById(R.id.fragment_personal_username_tv);
        mRemainderTextView = parentView.findViewById(R.id.fragment_personal_remainder_tv);

        TextView versionTextView = parentView.findViewById(R.id.fragment_person_version_tv);
        versionTextView.setText("V" + PackageUtils.getVersionName(getActivity(), getActivity().getPackageName(), "1.0"));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_person_repayment_ll) {
            viewMyRepaymentAction();
        } else if (view.getId() == R.id.fragment_person_my_order_ll) {
            viewMyOrderAction();
        } else if (view.getId() == R.id.fragment_person_my_invest_ll) {
            viewMyInvestAction();
        } else if (view.getId() == R.id.fragment_person_change_pwd_ll) {
            changeAction();
        } else if (view.getId() == R.id.fragment_person_logout_btn) {
            logoutAction();
        } else if (view.getId() == R.id.fragment_personal_remainder_title_tv
                || view.getId() == R.id.fragment_personal_remainder_tv) {
            withDrawAction();
        } else if (view.getId() == R.id.fragment_person_loan_ll) {
            viewMyLoansAction();
        }
    }

    private void viewMyRepaymentAction() {
        Intent intent = new Intent(getActivity(), RepayBorrowListActivity.class);
        startActivity(intent);
    }

    private void changeAction() {
        Intent intent = new Intent(getActivity(), ChangePwdChoiceActivity.class);
        startActivity(intent);
    }

    private void viewMyOrderAction() {
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        startActivity(intent);
    }

    private void viewMyInvestAction() {
        Intent intent = new Intent(getActivity(), InvestMineActivity.class);
        startActivity(intent);
    }

    private void withDrawAction() {
        Intent intent = new Intent(getActivity(), WithDrawListActivity.class);
        startActivity(intent);
    }

    private void viewMyLoansAction() {
        Intent intent = new Intent(getActivity(), ApplyLoanListActivity.class);
        startActivity(intent);
    }

    private void logoutAction() {
        new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
                .setMessage(R.string.confirm_logout_prompt)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserManager.getInstance(getActivity().getApplicationContext()).clear();
                        EventBus.getDefault().post(Integer.valueOf(0), "home_change_tag_event");

                        mUsernameTextView.setText("");
                        mRemainderTextView.setText("");
                    }
                })
                .show();
    }

    private void getUserInfo() {
        UserInfoAPI.getInstance().userInfo(UserManager.getInstance(getActivity().getApplicationContext()).getUid(), new SubscriberOnListener<UserInfoModel>() {
            @Override
            public void onSucceed(UserInfoModel data) {
                setUserData(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void setUserData(UserInfoModel data) {
        mUsernameTextView.setText(UserManager.getInstance(getActivity().getApplicationContext()).getUsername());
        mRemainderTextView.setText(MoneyUtils.formatMoney(data.countMoney, Locale.ENGLISH));
    }

    @Subscriber(tag = "RefreshUserInfo")
    public void onRefreshUserInfoEvent(String param) {
        getUserInfo();
    }
}
