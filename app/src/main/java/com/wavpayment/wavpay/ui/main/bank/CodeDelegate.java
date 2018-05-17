package com.wavpayment.wavpay.ui.main.bank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.timer.BaseTimerTask;
import com.lan.sponge.util.timer.ITimerListener;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * 验证码验证
 * Created by Administrator on 2017/12/28.
 */

public class CodeDelegate extends SpongeDelegate implements ITimerListener {
    private TextView timer;
    private Timer mTimer = null;
    private int mCount = 60;
    private String phone;
    private String type;

    @Override
    public Object setLayout() {
        return R.layout.delegate_code;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText("Bank Verification");
        $(R.id.btn_next).setOnClickListener(view -> network());
        timer = $(R.id.btn_code);
        timer.setOnClickListener(v -> {
            if (!phone.isEmpty()) {
                initTimer();
                timer.setTextColor(getResources().getColor(R.color.app_bg));
                timer.setClickable(false);
                CommonHandler.getInstance().code(phone);
            } else {
                show("Please enter the correct mobile number");
            }

        });
    }


    private void network() {
        final String phoneCode = ((TextView) $(R.id.et_code)).getText().toString();
        if (!phoneCode.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("bankCode", type);
            map.put("phone", phone);
            map.put("phoneCode", phoneCode);
            Network.getInstance()
                    .get(NetUrl.PHONECODE,map)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            int code = JSON.parseObject(response.body()).getInteger("statusCode");
                            switch (code) {
                                case 200:
                                    final AddCardDelegate addCard = new AddCardDelegate();
                                    addCard.setType(type);
                                    getSupportDelegate().startWithPop(addCard);
                                    break;
                                case 500:
                                    break;
                                case 410:
                                    RxToast.showToast(getString(R.string.r_err_410));
                                    break;
                                case 404:
                                    RxToast.showToast(getString(R.string.r_err_404));
                                    break;
                            }
                        }
                    });
        } else {
            RxToast.showToast("Please fill in the verification code");
        }
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
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
                timer.setText(mCount + " " + getResources().getString(R.string.countdown));
                mCount--;
                if (mCount < 0) {
                    if (mTimer != null) {
                        mCount = 60;
                        mTimer.cancel();
                        mTimer = null;
                        timer.setClickable(true);
                        timer.setText(getString(R.string.btn_code));
                        timer.setTextColor(getResources().getColor(R.color.app_bg));
                    }
                }
            }
        });
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
