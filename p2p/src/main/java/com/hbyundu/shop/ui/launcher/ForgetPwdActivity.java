package com.hbyundu.shop.ui.launcher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.launcher.FindPwdAPI;
import com.hbyundu.shop.rest.api.wav.WavCodeAPI;
import com.hbyundu.shop.rest.api.wav.WavKeyAPI;
import com.hbyundu.shop.rest.api.wav.WavPasswordAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.util.DeviceUuidUtils;
import com.hbyundu.shop.vendor.encrypt.RSA;

import net.itgoo.validator.MinLengthValidator;
import net.itgoo.validator.Validator;
import net.itgoo.validator.Validators;

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPasswordEditText;

    private EditText mMobileEditText;

    private EditText mCodeEditText;

    private Validators mValidators;

    private SVProgressHUD mProgressHUD;

    private String mWavKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initTitleBar();
        initViews();
        initValidators();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.find_password);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mMobileEditText = (EditText) findViewById(R.id.activity_forget_mobile_et);
        mPasswordEditText = (EditText) findViewById(R.id.activity_forget_password_et);
        mCodeEditText = (EditText) findViewById(R.id.activity_forget_code_et);
        findViewById(R.id.activity_forget_submit_btn).setOnClickListener(this);
        findViewById(R.id.activity_forget_get_code_btn).setOnClickListener(this);
    }

    private void initValidators() {
        mValidators = new Validators();
        mValidators.setShowError(false);
        mValidators.put(mMobileEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_enter_your_mobile_prompt))
        });
        mValidators.put(mCodeEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_enter_verifying_code))
        });
        mValidators.put(mPasswordEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_enter_your_password_prompt))
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
        if (view.getId() == R.id.activity_forget_submit_btn) {
            findPassword();
        } else if (view.getId() == R.id.activity_forget_get_code_btn) {
            getSmsCodeAction();
        }
    }

    private void getSmsCodeAction() {
        if (mMobileEditText.getText().length() == 0) {
            mProgressHUD.showErrorWithStatus(getString(R.string.please_enter_your_mobile_prompt));
            return;
        }

        WavCodeAPI.getInstance().getCode(mMobileEditText.getText().toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                Toast.makeText(ForgetPwdActivity.this, R.string.get_code_succeed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ForgetPwdActivity.this, R.string.get_code_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findPassword() {
        mValidators.clearError();
        if (!mValidators.isValid()) {
            mProgressHUD.showErrorWithStatus(mValidators.getLastError());
            return;
        }

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);

        changePwd();
    }

    private void changePwd() {
        FindPwdAPI.getInstance().findPwd(mMobileEditText.getText().toString(), mPasswordEditText.getText().toString(), mMobileEditText.getText().toString(),
                new SubscriberOnListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        getKey();
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
        WavKeyAPI.getInstance().getKey(mMobileEditText.getText().toString(), DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                mWavKey = data;
                changePassword();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showSuccessWithStatus(getString(R.string.change_succeed));
                mProgressHUD.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(SVProgressHUD svProgressHUD) {
                        finish();
                    }
                });
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void changePassword() {
        String password = RSA.encryptByPublic(mPasswordEditText.getText().toString(), mWavKey);
        WavPasswordAPI.getInstance().resetPassword(mMobileEditText.getText().toString(), password,
                mCodeEditText.getText().toString(), DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), 1, new SubscriberOnListener<String>() {
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
                        mProgressHUD.showSuccessWithStatus(getString(R.string.change_succeed));
                        mProgressHUD.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(SVProgressHUD svProgressHUD) {
                                finish();
                            }
                        });
                    }
                });
    }
}
