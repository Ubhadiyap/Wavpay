package com.hbyundu.shop.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hbyundu.shop.R;
import com.hbyundu.shop.ui.BaseActivity;

public class ChangePwdChoiceActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd_choice);

        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.change_pwd);
    }

    private void initView() {
        findViewById(R.id.activity_change_pwd_choice_change_login_pwd_ll).setOnClickListener(this);
        findViewById(R.id.activity_change_pwd_choice_change_pay_pwd_ll).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_change_pwd_choice_change_login_pwd_ll) {
            changeLoginPwdAction();
        } else if (view.getId() == R.id.activity_change_pwd_choice_change_pay_pwd_ll) {
            changePayPwdAction();
        }
    }

    private void changeLoginPwdAction() {
        Intent intent = new Intent(this, ChangePwdActivity.class);
        startActivity(intent);
    }

    private void changePayPwdAction() {
        Intent intent = new Intent(this, ChangePayPasswordActivity.class);
        startActivity(intent);
    }
}
