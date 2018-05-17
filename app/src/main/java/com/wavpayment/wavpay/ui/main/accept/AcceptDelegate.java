package com.wavpayment.wavpay.ui.main.accept;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.ui.main.bill.BillDelegate;
import com.wavpayment.wavpay.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 收款码
 * Created by Administrator on 2017/11/25.
 */

public class AcceptDelegate extends SpongeDelegate {

    private RelativeLayout mShoppingCartRly;
    private TextView mShoppingCartIv;
    private ImageView memey_tv;
    private String payAmount;//付款金额
    private ImageView ivQRCode;

    @Override
    public Object setLayout() {
        return R.layout.delegate_accept;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ivQRCode = $(R.id.iv_code);
        mShoppingCartRly = $(R.id.mShoppingCartRly);
        mShoppingCartIv = $(R.id.mShoppingCartIv);
        memey_tv = $(R.id.memey_tv);
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.amount).setOnClickListener(v -> {
            final AmountDelegate delegate = new AmountDelegate();
            getSupportDelegate().start(delegate);
            delegate.setCallbackListener(args -> {
                ((TextView) $(R.id.tv_amount)).setText(args);
                CommonHandler.getInstance().accCode(ivQRCode, args);
            });
        });
        $(R.id.tv_right).setOnClickListener(view -> getSupportDelegate().start(new BillDelegate()));

        CallbackManager.getInstance()
                .addCallback(CallbackType.ACCEPT_PUSH, args -> {
                    final JSONObject object = JSON.parseObject(args.toString());
                    final String account = object.getString("account");
                    final String headImg = object.getString("headImg");
                    final String nickName = object.getString("nickName");
                    payAmount = object.getString("payAmount");
                    if (headImg != null) {
                        final String url = Sponge.getConfiguration(ConfigKeys.API_HOST) + headImg.substring(1, headImg.length());
                        Glide.with(_mActivity)
                                .load(url)
                                .apply(RECYCLER_OPTIONS)
                                .into((CircleImageView) $(R.id.head_image));
                        ((TextView) $(R.id.name_tv)).setText(nickName);
                        if (account != null) {
                            IGlobalCallback<Integer> callbackInt = CallbackManager.getInstance().getCallback(CallbackType.INFO);
                            callbackInt.executeCallback(200);
                            Utils utils = new Utils();
                            utils.startAnimator(_mActivity,mShoppingCartRly,mShoppingCartIv,memey_tv,payAmount);
                        }
                    }

                });
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        CommonHandler.getInstance().accCode(ivQRCode, "");
    }

}
