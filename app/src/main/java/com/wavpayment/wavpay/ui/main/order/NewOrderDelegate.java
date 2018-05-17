package com.wavpayment.wavpay.ui.main.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.loader.SpongeLoader;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxKeyboardTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.main.order.OrderHandler;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.bank.BankFields;
import com.wavpayment.wavpay.ui.main.transfer.TranCTDelegate;
import com.wavpayment.wavpay.utils.Utils;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;

import java.util.HashMap;
import java.util.Map;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;

/**
 * 转账
 * Created by Administrator on 2017/12/27.
 */

public class NewOrderDelegate extends BaseOrderDelegate {

    private String user = null;
    private String jsonData = null;
    private int type;
    private String realName = null;

    @Override
    public Object setLayout() {
        return R.layout.activity_order;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
        next();
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        final String balance = data.getString("balance");
        ((TextView) $(R.id.tv_ibd)).setText("Balance (RM" + balance + ")");
        user = data.getString("account");//设置当前账号
        CallbackManager.getInstance()
                .addCallback(CallbackType.PAY, args -> {
                    SpongeLoader.stopLoading();
                    TranAccDelegate top = new TranAccDelegate();
                    top.setOrderId(args.toString());
                    getSupportDelegate().pop();
                    getSupportDelegate().start(top);
                });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (type != TRANSFER) {
            initData(type, jsonData);
        }
    }

    private void initData(int payType, String json) {
        JSONObject data = JSON.parseObject(json);
        String uri = Sponge.getConfiguration(ConfigKeys.API_HOST);
        final String headImg = data.getString("headImg");
        uri = uri.substring(0, uri.length() - 1) + headImg;
        final String realName = data.getString("realName");
        final String nickName = data.getString("nickName");
        amount.setEnabled(true);
        if (payType == COLLECTION || payType == TRANSFER) {
            tradeType = 1;
            transferType = 1;
            payAccount = user;
            if (payType==TRANSFER){
                collectAccount = data.getString("account");//收款账号
            }else {
                collectAccount = data.getString("collectionAccount");//收款账号
            }
            payAmount = data.getString("collectionAmount");//收款金额
            amount.setText(payAmount);
            if (payAmount == null) {
                amount.setEnabled(true);
            } else {
                amount.setEnabled(false);
            }
        } else if (payType == PAYMENT) {
            tradeType = 4;
            transferType = 1;
            collectAccount = user;
            payAccount = data.getString("paymentAccount");//付款账户
            payWays = data.getString("paymentBankCard");//付款银行卡
            mchOrderId = data.getString("mchOrderId");
            goodsInfo = data.getString("goodsInfo");
            if (!payWays.isEmpty()) {
                transferType = 2;
            }
        }
        nick.setText(nickName);
        Glide.with(this)
                .load(uri)
                .apply(RECYCLER_OPTIONS)
                .into(photo);
    }

    public void setData(int type, String jsonData) {
        this.type = type;
        if (type == TRANSFER) {
            OrderHandler.getInstance()
                    .vUser(jsonData)
                    .setOrderListener(data -> {
                        final int code = JSON.parseObject(data).getInteger("statusCode");
                        if (code == 200) {
                            final JSONObject json = JSON.parseObject(data).getJSONObject("user");
                            initData(type, json.toJSONString());
                        } else if (code == 121) {
                            RxToast.showToast("You cannot transfer money to yourself");
                            getSupportDelegate().pop();
                        }

                    });
        } else {
            this.jsonData = jsonData;
        }

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        amount = $(R.id.et_amount);
        msg = $(R.id.et_msg);
        ibd = $(R.id.tv_ibd);
        photo = $(R.id.iv_photo);
        nick = $(R.id.tv_nick);
        ((TextView) $(R.id.tv_title)).setText(R.string.amount_title);
        $(R.id.ll_select).setOnClickListener(view -> {
            final TranCTDelegate tran = new TranCTDelegate();
            getSupportDelegate().start(tran);
            tran.setListener(entity -> {
                int id = entity.getField(MultipleFields.ID);
                if (id == -1) {
                    sweepType = 1;
                    String balance = entity.getField(BankFields.balance);
                    ((TextView) $(R.id.tv_ibd)).setText("Balance (RM" + balance + ")");
                } else {
                    sweepType = 2;
                    payWays = entity.getField(BankFields.cardNumber);
                    bankCode = entity.getField(BankFields.bankCode);
                    realName = entity.getField(BankFields.realName);
                    String content = payWays.substring(payWays.length() - 4, payWays.length());
                    ((TextView) $(R.id.tv_ibd)).setText(bankCode + "(" + content + ")");
                }
            });
        });

        ((EditText) $(R.id.et_amount)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    private void next() {
        $(R.id.next).setOnClickListener(view -> {
            amount.setEnabled(true);
            payAmount = amount.getText().toString();
            payAccountType = "1";
            collectAccountType = "1";
            memo = msg.getText().toString();
            if (!payAmount.isEmpty()) {
                if (tradeType == 1) {
                    if (sweepType == 1 && transferType == 1) {//余额->余额
                        final PaymentDelegate pay = new PaymentDelegate();
                        pay.setPhone(payAccount);
                        pay.setParams(setUpToPayOne());
                        pay.setType(tradeType);
                        getSupportDelegate().pop();
                        getSupportDelegate().start(pay);
                    } else if (sweepType == 2 && transferType == 1||sweepType==2&& transferType ==2) {//卡->余
                        $(R.id.next).setEnabled(false);
                        Map<String,String> map = new HashMap<>();
                        map.put("collAccount",collectAccount);
                        map.put("payBankCode",bankCode);
                        map.put("amount",payAmount);
                        map.put("memo",memo);
                        netWork(map);
                    } else if (sweepType == 1 && transferType == 2) {//余额->卡
                        $(R.id.next).setEnabled(false);
                        Map<String,String> map = new HashMap<>();
                        map.put("name",realName);
                        map.put("bankCode",bankCode);
                        map.put("bankAccount",collectAccount);
                        map.put("amount",payAmount);
                        netWorkTwo(map);
                    }
                } else {
                    SpongeLoader.showLoading(_mActivity);
                    if (sweepType == 1 && transferType == 1) {//余额->余额
                        OrderHandler.getInstance().pay(setOrderPayOne());
                    }
                }
            } else {
                RxToast.showToast(getResources().getString(R.string.order_toast));
            }
        });
    }

    /**
     * 提现
     * @param map
     */
    private void netWork(Map<String,String> map){
        Network.getInstance()
                .post("fpxTrade/transfer2wavpay.html",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getIntValue("statusCode");
                            if (code == 200) {
                                String url = JSON.parseObject(response.body()).getString("url");
                                WebPayDelegate web = new WebPayDelegate();
                                web.setUrl(url);
                                getSupportDelegate().pop();
                                getSupportDelegate().start(web);
                            }else {
                                $(R.id.next).setEnabled(true);
                            }
                        }
                    }
                });
    }

    /**
     *
     * @param map
     */
    private void netWorkTwo(Map<String,String> map){
        Network.getInstance()
                .post("withdraw/toOther.html",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())) {
                            final int code = JSON.parseObject(response.body()).getIntValue("statusCode");
                            if (code == 200) {
                                String url = JSON.parseObject(response.body()).getString("url");
                                WebPayDelegate web = new WebPayDelegate();
                                web.setUrl(url);
                                getSupportDelegate().pop();
                                getSupportDelegate().start(web);
                            }else {
                                $(R.id.next).setEnabled(true);
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        RxKeyboardTool.showSoftInput(_mActivity, amount);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxKeyboardTool.hideSoftInput(_mActivity);
    }
}
