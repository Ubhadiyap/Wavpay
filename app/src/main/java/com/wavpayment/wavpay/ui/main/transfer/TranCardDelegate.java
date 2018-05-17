package com.wavpayment.wavpay.ui.main.transfer;

import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.bank.BankFields;
import com.wavpayment.wavpay.ui.main.bank.CardTypeDelegate;
import com.wavpayment.wavpay.ui.main.order.PaymentDelegate;
import com.wavpayment.wavpay.ui.main.order.WebPayDelegate;
import com.wavpayment.wavpay.utils.Utils;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


public class TranCardDelegate extends SpongeDelegate {

    private String payAmount = null;//支付金额 ok
    private String payWays = null;//付款方式 ok
    private String collectWays = null;//收款方式 ok

    private String bankCode = null;//银行卡类型
    private String cardName = null;//持卡人姓名

    private int sweepType = 1;//1余额，2，卡
    private String payBankCode = null;//
    private String realName = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_tran_card;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(view -> getSupportDelegate().pop());
        ((TextView) $(R.id.tv_title)).setText("Bank Account");
        $(R.id.ll_type).setOnClickListener(view -> {
            CardTypeDelegate card = new CardTypeDelegate();
            getSupportDelegate().start(card);
            card.setListener(args -> ((TextView) $(R.id.et_type)).setText(args[0]));
        });
        ((EditText) $(R.id.et_pass)).addTextChangedListener(new TextWatcher() {
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
                    payWays = entity.getField(BankFields.cardNumber);
                    payBankCode = entity.getField(BankFields.bankCode);
                    realName = entity.getField(BankFields.realName);
                    String content = payWays.substring(payWays.length() - 4, payWays.length());
                    ((TextView) $(R.id.tv_ibd)).setText(payBankCode + "(" + content + ")");
                    sweepType = 2;
                }
            });
        });
        $(R.id.btn_next).setOnClickListener(view -> {
            cardName = ((EditText) $(R.id.et_account)).getText().toString();
            collectWays = ((EditText) $(R.id.et_code)).getText().toString();
            bankCode = ((TextView) $(R.id.et_type)).getText().toString();
            payAmount = ((EditText) $(R.id.et_pass)).getText().toString();
            if (checkForm(cardName, bankCode, collectWays, payAmount)) {
                final double s = Double.parseDouble(payAmount);
                if (s < 20) {
                    RxToast.showToast("The amount cannot be less than 20 RM");
                    return;
                }
                $(R.id.btn_next).setEnabled(false);
                if (sweepType == 1) {
                    CommonHandler.getInstance().verifyName(bankCode, cardName, collectWays);
                } else if (sweepType == 2) {
                    Map<String, String> map = new HashMap<>();
                    map.put("collBankCode", bankCode);
                    map.put("cardName", cardName);
                    map.put("bankAccount", collectWays);
                    map.put("amount", payAmount);
                    map.put("payBankCode", payBankCode);
                    netWork(map);
                }
            } else {
                RxToast.showToast("Please fill in the complete information");
            }


        });

        CallbackManager.getInstance()
                .addCallback(CallbackType.V_CARD, args -> {
                    String memo = ((EditText) $(R.id.et_memo)).getText().toString();
                    Map<String, String> map = new HashMap<>();
                    map.put("name", cardName);
                    map.put("bankCode", bankCode);
                    map.put("bankAccount", collectWays);
                    map.put("amount", payAmount);
                    map.put("memo", memo);
                    netWorkTwo(map);
                });
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        final String balance = data.getString("balance");
        ((TextView) $(R.id.tv_ibd)).setText("Balance (RM" + balance + ")");
    }


    private void netWorkTwo(Map<String, String> map) {
        Network.getInstance()
                .post("withdraw/toOther.html", map)
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
                            } else if (code == 102) {
                                RxToast.showToast("not sufficient funds");
                                $(R.id.btn_next).setEnabled(true);
                            } else {
                                $(R.id.btn_next).setEnabled(true);
                            }
                        }
                    }
                });
    }


    private void netWork(Map<String, String> map) {
        Network.getInstance()
                .post("fpxTrade/transfer2bank.html", map)
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
                            } else {
                                $(R.id.btn_next).setEnabled(true);
                            }
                        }
                    }
                });
    }

    private boolean checkForm(String... args) {
        boolean isCheck = true;
        for (String s : args) {
            if (s.isEmpty()) {
                isCheck = false;
            }
        }
        return isCheck;
    }

}
