package com.hbyundu.shop.ui.credit;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbyundu.shop.R;
import com.hbyundu.shop.ui.credit.apply.ApplyLoansActivity;
import com.hbyundu.shop.ui.credit.invest.list.InvestListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends Fragment implements View.OnClickListener {

    private View mRootView;

    public CreditFragment() {
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
            mRootView = inflater.inflate(R.layout.fragment_credit, container, false);
            initTitleBar(mRootView);
            initViews(mRootView);
        }

        return mRootView;
    }

    private void initTitleBar(View parentView) {
        Toolbar toolbar = parentView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.tab_credit);
    }

    private void initViews(View parentView) {
        parentView.findViewById(R.id.fragment_credit_apply_btn).setOnClickListener(this);
        parentView.findViewById(R.id.fragment_credit_invest_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        if (id == R.id.fragment_credit_apply_btn) {
            intent = new Intent(this.getActivity(), ApplyLoansActivity.class);
            startActivity(intent);
        } else if (id == R.id.fragment_credit_invest_btn) {
            intent = new Intent(this.getActivity(), InvestListActivity.class);
            startActivity(intent);
        }
    }
}
