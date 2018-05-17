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
import com.hbyundu.shop.rest.api.launcher.SignUpAPI;
import com.hbyundu.shop.rest.api.wav.WavCheckAccountAPI;
import com.hbyundu.shop.rest.api.wav.WavCodeAPI;
import com.hbyundu.shop.rest.api.wav.WavKeyAPI;
import com.hbyundu.shop.rest.api.wav.WavRegisterAPI;
import com.hbyundu.shop.rest.api.wav.WavVerifyCodeForRegisterAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.SignUpResultModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.util.DeviceUuidUtils;
import com.hbyundu.shop.vendor.encrypt.RSA;

import net.itgoo.validator.MinLengthValidator;
import net.itgoo.validator.Validator;
import net.itgoo.validator.Validators;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPasswordEditText;

    private EditText mMobileEditText;

    private EditText mPayPasswordEditText;

    private EditText mCodeEditText;

    private Validators mValidators;

    private SVProgressHUD mProgressHUD;

    private String mWavKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTitleBar();
        initViews();
        initValidators();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.sign_up_account);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mMobileEditText = (EditText) findViewById(R.id.activity_register_mobile_et);
        mPasswordEditText = (EditText) findViewById(R.id.activity_register_password_et);
        mPayPasswordEditText = (EditText) findViewById(R.id.activity_register_pay_password_et);
        mCodeEditText = (EditText) findViewById(R.id.activity_register_code_et);
        findViewById(R.id.activity_register_submit_btn).setOnClickListener(this);
        findViewById(R.id.activity_register_get_code_btn).setOnClickListener(this);
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
        mValidators.put(mPayPasswordEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_input_pay_password))
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
        if (view.getId() == R.id.activity_register_submit_btn) {
            registerAction();
        } else if (view.getId() == R.id.activity_register_get_code_btn) {
            getSmsCodeAction();
        }
    }

    private void registerAction() {
        mValidators.clearError();
        if (!mValidators.isValid()) {
            mProgressHUD.showErrorWithStatus(mValidators.getLastError());
            return;
        }

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);

        verifyCode();
    }

    private void getSmsCodeAction() {
        if (mMobileEditText.getText().length() == 0) {
            mProgressHUD.showErrorWithStatus(getString(R.string.please_enter_your_mobile_prompt));
            return;
        }

        WavCodeAPI.getInstance().getCode(mMobileEditText.getText().toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                Toast.makeText(RegisterActivity.this, R.string.get_code_succeed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(RegisterActivity.this, R.string.get_code_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp() {
        SignUpAPI.getInstance().signUp(mMobileEditText.getText().toString(), mPasswordEditText.getText().toString(), mMobileEditText.getText().toString(),
                new SubscriberOnListener<SignUpResultModel>() {
                    @Override
                    public void onSucceed(SignUpResultModel data) {
                        checkWavAccount();
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressHUD.dismissImmediately();
                        mProgressHUD.showErrorWithStatus(getString(R.string.sign_up_failure));
                    }
                });
    }

    private void checkWavAccount() {
        WavCheckAccountAPI.getInstance().check(mMobileEditText.getText().toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                getKey();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.sign_up_failure));
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getKey() {
        WavKeyAPI.getInstance().getKey(mMobileEditText.getText().toString(), DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                mWavKey = data;
                signUpWavAccount();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showSuccessWithStatus(getString(R.string.sign_up_success));
                mProgressHUD.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(SVProgressHUD svProgressHUD) {
                        finish();
                    }
                });
            }
        });
    }

    private void verifyCode() {
        WavVerifyCodeForRegisterAPI.getInstance().verify(mMobileEditText.getText().toString(),
                mCodeEditText.getText().toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                signUp();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.sign_up_failure));
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void signUpWavAccount() {
        String payPassword = RSA.encryptByPublic(mPayPasswordEditText.getText().toString(), mWavKey);
        String password = RSA.encryptByPublic(mPasswordEditText.getText().toString(), mWavKey);
        WavRegisterAPI.getInstance().register(mMobileEditText.getText().toString(), password, payPassword,
                DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), new SubscriberOnListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        mProgressHUD.dismissImmediately();
                        mProgressHUD.showSuccessWithStatus(getString(R.string.sign_up_success));
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
                        mProgressHUD.showSuccessWithStatus(getString(R.string.sign_up_failure));
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
