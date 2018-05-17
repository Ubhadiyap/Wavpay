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
import com.hbyundu.shop.rest.api.user.ChangePwdAPI;
import com.hbyundu.shop.rest.api.wav.WavCodeAPI;
import com.hbyundu.shop.rest.api.wav.WavKeyAPI;
import com.hbyundu.shop.rest.api.wav.WavPasswordAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.user.ChangePwdResultModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.util.DeviceUuidUtils;
import com.hbyundu.shop.vendor.encrypt.RSA;

/**
 * Created by zhangyue on 2017-12-19.
 */

public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText newPwEt, oldPwEt;
    private SVProgressHUD mProgressHUD;
    private EditText mCodeEditText;
    private TextView mMobileTextView;
    private String mWavKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.change_login_password);
    }

    private void initView() {
        mProgressHUD = new SVProgressHUD(this);
        newPwEt = (EditText) findViewById(R.id.activity_change_password_new_et);
        oldPwEt = (EditText) findViewById(R.id.activity_change_password_old_et);
        mCodeEditText = (EditText) findViewById(R.id.activity_change_password_code_et);
        mMobileTextView = (TextView) findViewById(R.id.activity_change_password_mobile_tv);
        findViewById(R.id.activity_change_password_submit_btn).setOnClickListener(this);

        mMobileTextView.setText(UserManager.getInstance(getApplicationContext()).getMobile());
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
    public void onClick(View v) {
        if (v.getId() == R.id.activity_change_password_submit_btn) {
            save();
        } else if (v.getId() == R.id.activity_change_password_get_code_btn) {
            getSmsCodeAction();
        }
    }

    private void getSmsCodeAction() {
        WavCodeAPI.getInstance().getCode(mMobileTextView.getText().toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                Toast.makeText(ChangePwdActivity.this, R.string.get_code_succeed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ChangePwdActivity.this, R.string.get_code_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save() {
        if (mCodeEditText.getText().length() == 0) {
            return;
        }

        if (newPwEt.getText().toString().length() == 0
                || oldPwEt.getText().toString().length() == 0) {
            return;
        }

        ChangePwdAPI.getInstance().changePwd(UserManager.getInstance(getApplicationContext()).getUid(), oldPwEt.getText().toString(),
                newPwEt.getText().toString(), new SubscriberOnListener<ChangePwdResultModel>() {
            @Override
            public void onSucceed(ChangePwdResultModel data) {
                getKey();

            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.password_change_failure));
            }
        });

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }

    @SuppressLint("MissingPermission")
    private void getKey() {
        WavKeyAPI.getInstance().getKey(mMobileTextView.getText().toString(), DeviceUuidUtils.getUUID(this.getApplicationContext()).toString(), new SubscriberOnListener<String>() {
            @Override
            public void onSucceed(String data) {
                mWavKey = data;
                changePassword();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showSuccessWithStatus(getString(R.string.password_change_success));
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
        String password = RSA.encryptByPublic(newPwEt.getText().toString(), mWavKey);
        WavPasswordAPI.getInstance().resetPassword(mMobileTextView.getText().toString(), password,
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
                        mProgressHUD.showSuccessWithStatus(getString(R.string.password_change_success));
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
