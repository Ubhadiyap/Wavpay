package com.hbyundu.shop.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.launcher.LoginAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;
import com.hbyundu.shop.ui.BaseActivity;

import net.itgoo.validator.MinLengthValidator;
import net.itgoo.validator.Validator;
import net.itgoo.validator.Validators;

import org.simple.eventbus.EventBus;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mNameEditText;

    private EditText mPasswordEditText;

    private Validators mValidators;

    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initTitleBar();
        initViews();
        initValidators();
    }

    private void initTitleBar() {
        findViewById(R.id.activity_login_close_btn).setOnClickListener(this);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mNameEditText = (EditText) findViewById(R.id.activity_login_username_et);
        mPasswordEditText = (EditText) findViewById(R.id.activity_login_password_et);
        findViewById(R.id.activity_login_submit_btn).setOnClickListener(this);
        findViewById(R.id.activity_login_forget_tv).setOnClickListener(this);
        findViewById(R.id.activity_login_register_btn).setOnClickListener(this);
    }

    private void initValidators() {
        mValidators = new Validators();
        mValidators.setShowError(false);
        mValidators.put(mNameEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_enter_your_username_prompt))
        });
        mValidators.put(mPasswordEditText, new Validator[]{
                new MinLengthValidator(1, getString(R.string.please_enter_your_password_prompt))
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_login_submit_btn) {
            submitAction();
        } else if (view.getId() == R.id.activity_login_forget_tv) {
            forgetAction();
        } else if (view.getId() == R.id.activity_login_register_btn) {
            registerAction();
        } else if (view.getId() == R.id.activity_login_close_btn) {
            closeAction();
        }
    }

    private void closeAction() {
        finish();
    }

    private void submitAction() {
        mValidators.clearError();
        if (!mValidators.isValid()) {
            mProgressHUD.showErrorWithStatus(mValidators.getLastError());
            return;
        }

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);

        LoginAPI.getInstance().login(mNameEditText.getText().toString(), mPasswordEditText.getText().toString(), new SubscriberOnListener<LoginResultModel>() {
            @Override
            public void onSucceed(LoginResultModel data) {
                UserManager.getInstance(getApplicationContext()).setUid(data.userId);
                UserManager.getInstance(getApplicationContext()).setUsername(mNameEditText.getText().toString());
                UserManager.getInstance(getApplicationContext()).setMobile(data.mobile);
                EventBus.getDefault().post(Long.valueOf(data.userId), "LoginSuccessEvent");
                mProgressHUD.dismissImmediately();
                finish();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.sign_in_failure));
            }
        });
    }

    private void forgetAction() {
        Intent intent = new Intent(this, ForgetPwdActivity.class);
        startActivity(intent);
    }

    private void registerAction() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
