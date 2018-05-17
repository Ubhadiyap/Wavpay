package com.hbyundu.shop.ui.credit.apply;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.launcher.LoginActivity;

/**
 * Created by apple on 2017/12/3.
 */

public class ApplyLoansActivity extends BaseActivity implements View.OnClickListener {

    private EditText mAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans_apply);

        initTitle();
        initView();
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.loan_apply_loan);
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
        mAmountEditText = (EditText) findViewById(R.id.activity_loans_apply_amount_et);
        findViewById(R.id.activity_loans_apply_confirm_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_loans_apply_confirm_btn) {
            confirmAction();
        }
    }

    private void confirmAction() {
        if (!UserManager.getInstance(getApplicationContext()).isAuth()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (mAmountEditText.getText().length() == 0) {
            return;
        }

        double money = Double.valueOf(mAmountEditText.getText().toString());
        if (money <= 0) {
            return;
        }

        Intent intent = new Intent(this, ApplyLoansRateActivity.class);
        intent.putExtra("money", money);
        startActivity(intent);
    }
}
