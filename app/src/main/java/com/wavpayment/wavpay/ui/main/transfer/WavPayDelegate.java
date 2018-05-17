package com.wavpayment.wavpay.ui.main.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.order.BaseOrderDelegate;
import com.wavpayment.wavpay.ui.main.order.NewOrderDelegate;
import com.wavpayment.wavpay.ui.register.GetCodeDelegate;
import com.wavpayment.wavpay.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Administrator on 2017/12/27.
 */

public class WavPayDelegate extends SpongeDelegate {

    private EditText account;

    @Override
    public Object setLayout() {
        return R.layout.delegate_wavpay;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText("WavPay");
        account = $(R.id.name);
        $(R.id.next).setOnClickListener(view -> {
            final String name = account.getText().toString();
            if (!name.isEmpty()) {
                $(R.id.next).setEnabled(false);
                Map<String, String> map = new HashMap<>();
                map.put("account", name);
                Network.getInstance()
                        .get(NetUrl.ACCOUNTS, map)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                SpongeLoader.stopLoading();
                                if (Utils.isJSONValid(response.body())) {
                                    int code = JSON.parseObject(response.body()).getInteger("statusCode");
                                    switch (code) {
                                        case 200:
                                            $(R.id.next).setEnabled(true);
                                            RxToast.showToast(Sponge.getAppContext().getString(R.string.r_err_200));
                                            break;
                                        case 403:
                                            NewOrderDelegate order = new NewOrderDelegate();
                                            order.setData(BaseOrderDelegate.TRANSFER,name);
                                            getSupportDelegate().start(order);
                                            $(R.id.next).setEnabled(true);
                                            break;
                                    }
                                }
                            }
                        });
            }
        });

        $(R.id.contact).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            _mActivity.startActivityForResult(intent, 134);
        });

        CallbackManager.getInstance().addCallback(CallbackType.PHONE, args -> account.setText(args.toString()));
    }
}
