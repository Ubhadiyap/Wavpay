package com.hbyundu.shop.ui.credit.invest.recharge;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.invest.RechargeAPI;
import com.hbyundu.shop.rest.api.invest.RechargeRateAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.RechargeRateModel;
import com.hbyundu.shop.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private Spinner mMonthSpinner;
    private TextView mRateTextView;
    private EditText mAmountEditText;

    private List<RechargeRateModel> mRates = new ArrayList<>();
    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        initTitle();
        initViews();
        initData();
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.invest);
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
        mMonthSpinner = (Spinner) findViewById(R.id.activity_recharge_term_spinner);
        mRateTextView = (TextView) findViewById(R.id.activity_recharge_rate_tv);
        mAmountEditText = (EditText) findViewById(R.id.activity_recharge_amount_et);

        findViewById(R.id.activity_recharge_submit_btn).setOnClickListener(this);

        mProgressHUD = new SVProgressHUD(this);
    }

    private void initData() {
        RechargeRateAPI.getInstance().rate(new SubscriberOnListener<List<RechargeRateModel>>() {
            @Override
            public void onSucceed(List<RechargeRateModel> data) {
                mRates.addAll(data);
                setMonthData();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void setMonthData() {
        List<String> list = new ArrayList<>();
        for (RechargeRateModel rate : mRates) {
            list.add(String.format(getString(R.string.recharge_term_month_format), rate.month));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.invest_recharge_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthSpinner.setAdapter(adapter);
        mMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRateTextView.setText(mRates.get(position).rate);
                mRateTextView.setTag(mRates.get(position).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_recharge_submit_btn) {
            submitAction();
        }
    }

    private void submitAction() {
        if (mRateTextView.getText().toString().length() == 0
                || mAmountEditText.getText().toString().length() == 0) {
            return;
        }

        Long rateId = (Long) mRateTextView.getTag();
        RechargeAPI.getInstance().rate(UserManager.getInstance(getApplicationContext()).getUid(), rateId, Double.valueOf(mAmountEditText.getText().toString()),
                new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showSuccessWithStatus(getString(R.string.recharge_succeeded));
                mProgressHUD.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(SVProgressHUD svProgressHUD) {
                        finish();
                    }
                });
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.recharge_failed));
            }
        });

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }
}
