package com.hbyundu.shop.ui.launcher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.wav.WavCodeAPI;
import com.hbyundu.shop.rest.api.wav.WavKeyAPI;
import com.hbyundu.shop.rest.api.wav.WavPasswordAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.encrypt.RSA;
import com.hbyundu.shop.vendor.util.DeviceUuidUtils;

import net.itgoo.validator.MinLengthValidator;
import net.itgoo.validator.Validator;
import net.itgoo.validator.Validators;

public class ChangePayPasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView mMobileTextView;

    private EditText mPayPasswordEditText;

    private EditText mCodeEditText;

    private Validators mValidators;

    private SVProgressHUD mProgressHUD;

    private String mWavKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_password);

        initTitleBar();
        initViews();
        initValidators();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.change_pay_password);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mMobileTextView = (TextView) findViewById(R.id.activity_change_pay_password_mobile_tv);
        mPayPasswordEditText = (EditText) findViewById(R.id.activity_change_pay_password_pay_password_et);
        mCodeEditText = (EditText) findViewById(R.id.activity_change_pay_password_code_et);
        findViewById(R.id.activity_change_pay_password_submit_btn).setOnClickListener(this);
        findViewById(R.id.activity_change_pay_password_get_code_btn).setOnClickListener(this);

        mMobileTextView.setText(UserManager.getInstance(getApplicationContext()).getMobile());
    }

    private void initValidators() {
        mValidators = new Validators();
        mValidators.setShowError(false);
        mValidators.put(mCodeEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_enter_verifying_code))
        });
        mValidators.put(mPayPasswordEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_input_pay_password))
        });
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
        if (view.getId() == R.id.activity_change_pay_password_submit_btn) {
            changeAction();
        } else if (view.getId() == R.id.activity_change_pay_password_get_code_btn) {
            getSmsCodeAction();
        }
    }

    private void changeAction() {
        mValidators.clearError();
        if (!mValidators.isValid()) {
            mProgressHUD.showErrorWithStatus(mValidators.getLastError());
            return;
        }

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);

        getKey();
    }

    private void getSmsCodeAction() {
        WavCodeAPI.getInstance().getCode(mMobileTextView.getText().toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                Toast.makeText(ChangePayPasswordActivity.this, R.string.get_code_succeed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ChangePayPasswordActivity.this, R.string.get_code_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void changePayPassword() {
        String password = RSA.encryptByPublic(mPayPasswordEditText.getText().toString(), mWavKey);
        WavPasswordAPI.getInstance().resetPassword(mMobileTextView.getText().toString(), password,
                mCodeEditText.getText().toString(), DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), 2, new SubscriberOnListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        mProgressHUD.dismissImmediately();
                        mProgressHUD.showSuccessWithStatus(getString(R.string.change_succeed));
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
                        mProgressHUD.showErrorWithStatus(getString(R.string.change_failed));
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void getKey() {
        WavKeyAPI.getInstance().getKey(mMobileTextView.getText().toString(), DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                mWavKey = data;
                changePayPassword();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.change_failed));
            }
        });
    }
}
