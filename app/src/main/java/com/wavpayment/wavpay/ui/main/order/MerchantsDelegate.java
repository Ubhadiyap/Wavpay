package com.wavpayment.wavpay.ui.main.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.log.SpongeLogger;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.ui.main.bank.BankFields;
import com.wavpayment.wavpay.ui.main.transfer.TranCTDelegate;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;

import java.util.Map;
import java.util.WeakHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;

/**
 * 交易支付
 * Created by Administrator on 2018/1/14.
 */

public class MerchantsDelegate extends SpongeDelegate {

    public static final int CAN_LEARN = 104;//一码通
    public static final int MER_PAY = 105;//交易支付

    @Override
    public Object setLayout() {
        return R.layout.delegate_merchants;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        $(R.id.ll_select).setOnClickListener(v -> {
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
                    String content = payWays.substring(payWays.length() - 4, payWays.length());
                    ((TextView) $(R.id.tv_ibd)).setText(bankCode + "(" + content + ")");
                }
            });
        });
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        final String balance = data.getString("balance");
        ((TextView) $(R.id.tv_ibd)).setText("Balance (RM" + balance + ")");
        next();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }

    private void initData() {
        SpongeLogger.e("",url);
        final JSONObject object = JSON.parseObject(url);
        final String merchantName = object.getString("merchantName");
        collectAccount = object.getString("mchId");
        goodsInfo = object.getString("goodInfo");
        mchOrderId = object.getString("mchOrderId");
        payAmount = object.getString("amount");

        String uri = Sponge.getConfiguration(ConfigKeys.API_HOST);
        final String headImg = object.getString("headImg");
        uri += headImg.substring(1, headImg.length());
        Glide.with(this)
                .load(uri)
                .apply(RECYCLER_OPTIONS)
                .into((CircleImageView) $(R.id.iv_photo));
        ((TextView) $(R.id.or_bus)).setText(merchantName);
        ((TextView) $(R.id.or_mchId)).setText(collectAccount);
        ((TextView) $(R.id.name)).setText(goodsInfo);
        ((TextView) $(R.id.amount)).setText(payAmount);
    }

    public void setData(String jsonData) {
        url = jsonData;
      }


    private void next() {
        $(R.id.next).setOnClickListener(view -> {
            payAccountType = "1";
            collectAccountType = "2";
            final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
            final JSONObject data = JSON.parseObject(response).getJSONObject("user");
            payAccount = data.getString("account");//设置当前账号
            if (!payAmount.isEmpty()) {
                final PaymentDelegate pay = new PaymentDelegate();//获取支付金额
                pay.setPhone(payAccount);
                if (sweepType == 1 && transferType == 1) {//余额->余额
                    pay.setParams(setOrderPayOne());
                } else if (sweepType == 2 && transferType == 1) {//卡->余
                    pay.setParams(setOrderTwo());
                }
                pay.setType(tradeType);
                getSupportDelegate().pop();
                getSupportDelegate().start(pay);
            } else {
                RxToast.showToast(getResources().getString(R.string.order_toast));
            }
        });
    }

    //--------订单支付-----------------------------------4
    private int tradeType = 4;//转账//1、转账，2、，3、，4、5、， ok
    private String payAccount = null;//支付账号
    private String payAccountType = null;//支付账号类型ok
    private String payAmount = null;//支付金额
    private String collectAccount = null;//收款账号
    private String collectAccountType = null;//收款账号类型
    private String payWays = null;//付款方式
    private String bankCode = null;//银行卡类型
    private int transferType = 1;//1余额，2，卡 二维码方转账类型
    private int sweepType = 1;//1余额，2，卡 扫二维码方转账类型
    private String url = null;
    private String mchOrderId = "";
    private String goodsInfo = "";

    //余额->余额
    private Map<String, String> setOrderPayOne() {
        final Map<String, String> params = new WeakHashMap<>();
        params.put("tradeType", String.valueOf(tradeType));
        params.put("payAccount", payAccount);
        params.put("payAccountType", payAccountType);
        params.put("payAmount", payAmount);
        params.put("collectAccount", collectAccount);
        params.put("collectAccountType", collectAccountType);
        params.put("mchOrderId", mchOrderId);
        params.put("goodsInfo", goodsInfo);
        return params;
    }

    //卡->余额
    private Map<String, String> setOrderTwo() {
        final Map<String, String> params = new WeakHashMap<>();
        params.put("tradeType", String.valueOf(tradeType));
        params.put("payAccount", payAccount);
        params.put("payAccountType", payAccountType);
        params.put("payAmount", payAmount);
        params.put("payWays", payWays);//卡号-""->余额
        params.put("collectAccount", collectAccount);//当前账号
        params.put("collectAccountType", collectAccountType);
        params.put("mchOrderId", mchOrderId);
        params.put("goodsInfo", goodsInfo);
        return params;
    }
}
