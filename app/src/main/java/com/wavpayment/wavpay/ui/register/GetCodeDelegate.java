package com.wavpayment.wavpay.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jyn.vcview.VerificationCodeView;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.timer.BaseTimerTask;
import com.lan.sponge.util.timer.ITimerListener;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;

import java.util.Timer;

/**
 *
 * Created by Administrator on 2018/1/18.
 */

public class GetCodeDelegate extends SpongeDelegate implements ITimerListener {

    private Timer mTimer = null;
    private int mCount = 60;
    private TextView timer;
    private VerificationCodeView code;
    private String mobile;
    private String content = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_get_code;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(v -> {
            getSupportDelegate().pop();
            getSupportDelegate().start(new MobileDelegate());
        });
        ((TextView) $(R.id.tv_title)).setText("Code");
        $(R.id.next).setOnClickListener(v -> {
            if (content != null && content.length() == 4) {
                CommonHandler.getInstance().verifyCode(this, mobile, content);
            } else {
                RxToast.showToast(getResources().getString(R.string.r_code));
            }
        });
        timer = $(R.id.btn_code);
        code = $(R.id.et_code);
        timer.setEnabled(false);
        timer.setOnClickListener(v -> {
            initTimer();
            CommonHandler.getInstance().code(mobile);
        });
        initTimer();
        code.setOnCodeFinishListener(con -> content = con);
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(() -> {
            if (timer != null) {
                timer.setEnabled(true);
                timer.setText(mCount + " "+ getResources().getString(R.string.countdown));
                timer.setEnabled(false);
                mCount--;
                if (mCount < 0) {
                    timer.setClickable(true);
                    if (mTimer != null) {
                        mCount = 60;
                        mTimer.cancel();
                        mTimer = null;
                        timer.setEnabled(true);
                        timer.setText(getResources().getString(R.string.btn_code));
                        timer.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().pop();
        getSupportDelegate().start(new MobileDelegate());
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
