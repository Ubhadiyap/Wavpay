package com.wavpayment.wavpay.ui.main.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.ui.main.order.PaymentDelegate;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.Map;
import java.util.WeakHashMap;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;

public abstract class BaseDetailsDelegate extends SpongeDelegate {


    protected int tradeType = 5;//ok
    protected String payAccount = null;//ok
    protected String payAccountType = "1";//ok
    protected String payAmount = null;//ok
    protected String transactionAccount = null;//要充值的账号ok

    protected String orgCode = null;//缴费机构码ok
    protected String orgName = null;//缴费机构名称ok

    protected String facePrice = null;//面额ok

    protected EditText eOne, eTwo;
    private TextView eZero, coverage;
    private MultipleItemEntity entity = null;
    private boolean isMultiple = false;
    private int isFixed = -1;
    private String accountRequire;

    protected abstract void init();

    protected abstract int onType();

    @Override
    public Object setLayout() {
        return R.layout.delegate_item_details;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        eZero = $(R.id.et_zero);
        eOne = $(R.id.et_one);
        eTwo = $(R.id.et_two);
        coverage = $(R.id.coverage);
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.btn_next).setOnClickListener(v -> onNext());
        $(R.id.item_zero).setOnClickListener(v -> {
            TypePayDelegate tp = new TypePayDelegate();
            tp.setType(onType());
            tp.setListener(entity -> {
                this.entity = entity;
                orgName = entity.getField(TypeType.orgName);
                orgCode = entity.getField(TypeType.orgCode);
                eZero.setText(orgName);
                isFixed = entity.getField(TypeType.isFixed);
                accountRequire = entity.getField(TypeType.accountRequire);
                String hint = null;
                if (isFixed == 1) {
                    String[] b = accountRequire.split(",");
                    hint = "Account length is" + b[0] + "~" + b[1];
                    coverage.setVisibility(View.VISIBLE);
                    eOne.setVisibility(View.GONE);
                } else if (isFixed == 2||isFixed==3) {
                    eOne.setVisibility(View.VISIBLE);
                    coverage.setVisibility(View.GONE);
                    String minMaxAmount = entity.getField(TypeType.minMaxAmount);
                    final String[] a = minMaxAmount.split(",");
                    if (accountRequire.contains(",")) {
                        String[] b = accountRequire.split(",");
                        hint = "Account length is" + b[0] + "~" + b[1];
                    } else {
                        hint = "Account length is" + accountRequire;
                    }
                    switch (isFixed){
                        case 2:
                            eOne.setHint("Minimum " + a[0] + ", highest" + a[1]);
                            break;
                        case 3:
                            isMultiple = true;
                            eOne.setHint("Minimum " + a[0] + ", highest" + a[1] + ",It has to be a \nmultiple of 5");
                            break;
                    }
                }
                eTwo.setHint(hint);
            });
            getSupportDelegate().start(tp);
        });
        $(R.id.item_one).setOnClickListener(v -> {
            if (entity != null) {
                if (isFixed == 1) {
                    final String amountArr = entity.getField(TypeType.amountArr);
                    final String[] arr = amountArr.split(",");
                    TypeFragment typeFragment = new TypeFragment(arr);
                    typeFragment.show(getChildFragmentManager(), "dialog");
                    typeFragment.setListener(amount -> {
                        if (!isMultiple) {
                            coverage.setText(amount);
                        }
                    });
                } else if (isFixed == 2 || isFixed == 3) {
                    //TODO
                }
            } else {
                RxToast.showToast("Please select the type of recharging");
            }
        });
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        payAccount = data.getString("account");//设置当前账号
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eTwo.setText("");
    }

    protected void setTitle(String title) {
        ((TextView) $(R.id.tv_title)).setText(title);
    }

    protected void setImage(Object o) {
        final String name = (String) o;
        final String response = RxSPTool.readJSONCache(_mActivity, "banner");
        final JSONArray pathList = JSON.parseObject(response).getJSONArray("pathList");
        for (int i = 0; i < pathList.size(); i++) {
            final JSONArray it = pathList.getJSONArray(i);
            String url = Sponge.getConfiguration(ConfigKeys.API_HOST);
            url = url.substring(0, url.length() - 1);
            if (it.getString(0).equals(name)) {
                url += pathList.getJSONArray(i).getString(1);
                Glide.with(_mActivity)
                        .load(url)
                        .apply(RECYCLER_OPTIONS)
                        .into((ImageView) $(R.id.iv_adv));
            }
        }
    }

    protected void setName(String... args) {
        ((TextView) $(R.id.tv_zero)).setText(args[0]);
        ((TextView) $(R.id.tv_one)).setText(args[1]);
        ((TextView) $(R.id.tv_two)).setText(args[2]);
    }

    protected void setHint(String... args) {
        ((TextView) $(R.id.et_zero)).setHint(args[0]);
        ((EditText) $(R.id.et_two)).setHint(args[2]);
        ((EditText) $(R.id.et_one)).setHint(args[1]);
        coverage.setHint(args[1]);

    }

    private void onNext() {
        if (check()) {
            PaymentDelegate pay = new PaymentDelegate();
            pay.setParams(setPayment());
            pay.setType(tradeType);
            pay.setPhone(payAccount);
            getSupportDelegate().start(pay);
        }
    }


    private boolean check() {
        if (isFixed == -1) {
            RxToast.showToast("Please select the type of recharging");
            return false;
        } else if (isFixed == 2 || isFixed == 3) {
            payAmount = eOne.getText().toString();
            if (payAmount.isEmpty()) {
                RxToast.showToast("Please fill in the amount");
                return false;
            }
            if (isMultiple) {
                if (payAmount.contains(".")){
                    String []m = payAmount.split("\\.");
                    int s = m[1].length();
                    int i = Integer.parseInt(m[0]);
                    int j = Integer.parseInt(m[1])*s;
                    if (i%5!=0&&j%5!=0){
                        RxToast.showToast("The payment amount is not a multiple of five");
                        return false;
                    }
                }else {
                    int i = Integer.parseInt(payAmount);
                    if (i%5!=0){
                        RxToast.showToast("The payment amount is not a multiple of five");
                        return false;
                    }
                }
            }
        }else if (isFixed==1){
            payAmount = coverage.getText().toString();
            if (!payAmount.isEmpty()) {
                facePrice = payAmount;
            } else {
                RxToast.showToast("Please fill in the amount");
                return false;
            }
        }
        facePrice = payAmount;
        transactionAccount = eTwo.getText().toString();
        int size = transactionAccount.length();
        if (accountRequire.contains(",")) {
            String[] b = accountRequire.split(",");
            if (size>=Integer.parseInt(b[0])&&size<=Integer.parseInt(b[1])) {
                return true;
            }else {
                RxToast.showToast("Account length is" + b[0] + "~" + b[1]);
                return false;
            }
        } else if (size != Integer.parseInt(accountRequire)) {
            RxToast.showToast("Account length is" + accountRequire);
            return false;
        }else {
            return true;
        }
    }

    //余额->余额
    private Map<String, String> setPayment() {
        final Map<String, String> params = new WeakHashMap<>();
        params.put("tradeType", String.valueOf(tradeType));
        params.put("payAccount", payAccount);
        params.put("payAccountType", payAccountType);
        params.put("payAmount", payAmount);
        params.put("transactionAccount", transactionAccount);
        params.put("orgCode", orgCode);
        params.put("orgName", orgName);
        params.put("facePrice", facePrice);
        return params;
    }
}
