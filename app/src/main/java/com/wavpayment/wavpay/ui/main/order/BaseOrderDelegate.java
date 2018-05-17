package com.wavpayment.wavpay.ui.main.order;

import android.widget.EditText;
import android.widget.TextView;

import com.lan.sponge.delegate.SpongeDelegate;

import java.util.Map;
import java.util.WeakHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by Administrator on 2018/1/23.
 */

public abstract class BaseOrderDelegate extends SpongeDelegate{

    protected EditText amount;
    protected EditText msg;
    protected TextView ibd;
    protected CircleImageView photo;
    protected TextView nick;
    protected int tradeType = 1;//转账//1、转账，2、，3、，4、,5、
    protected String payAccount = null;//支付账号
    protected String payAccountType = null;//支付账号类型ok
    protected String payAmount = null;//支付金额
    protected String collectAccount = null;//收款账号
    protected String collectAccountType = null;//收款账号类型
    protected String payWays = null;//付款方式


    protected String bankCode = null;//银行卡类型
    protected String cardName = null;//持卡人姓名
    protected String collectWays = null;//收方卡号
    protected boolean isTransfer = false;//区分false扫码转账，true账号转账
    protected String url = null;
    protected int transferType = 1;//1余额，2，卡 二维码方转账类型
    protected int sweepType = 1;//1余额，2，卡 扫二维码方转账类型
    protected String memo;

    public static final int TRANSFER = 1001;//转账
    public static final int COLLECTION = 1002;//收款
    public static final int PAYMENT = 1003;//付款


    //-------------转账---------------------------------1
    //余额->余额
    protected Map<String, String> setUpToPayOne() {
        final Map<String, String> params = new WeakHashMap<>();
        params.put("tradeType", String.valueOf(tradeType));
        params.put("payAccount", payAccount);
        params.put("payAccountType", payAccountType);
        params.put("payAmount", payAmount);
        params.put("collectAccount", collectAccount);
        params.put("collectAccountType", collectAccountType);
        params.put("isTransfer",String.valueOf(isTransfer));
        params.put("memo", memo);
        return params;
    }

    //余额->卡
    protected Map<String, String> setUpToPayTwo() {
        final Map<String, String> params = new WeakHashMap<>();
        params.put("tradeType", String.valueOf(tradeType));
        params.put("payAccount", payAccount);
        params.put("payAccountType", payAccountType);
        params.put("payAmount", payAmount);
        params.put("collectWays", collectWays);//银行卡
        params.put("bankCode", bankCode);
        params.put("cardName", cardName);
        params.put("isTransfer",String.valueOf(isTransfer));
        params.put("memo", memo);
        return params;
    }
    //--------订单支付-----------------------------------4
    protected String mchOrderId = "";
    protected String goodsInfo = "";
    //余额->余额
    protected Map<String, String> setOrderPayOne() {
        final Map<String, String> params = new WeakHashMap<>();
        params.put("tradeType", String.valueOf(tradeType));
        params.put("payAccount", payAccount);
        params.put("payAccountType", payAccountType);
        params.put("payAmount", payAmount);
        params.put("mchOrderId",mchOrderId);
        params.put("goodsInfo",goodsInfo);
        params.put("collectAccount", collectAccount);
        params.put("collectAccountType", collectAccountType);
        params.put("memo", memo);
        return params;
    }


    
}
