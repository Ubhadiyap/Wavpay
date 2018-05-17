package com.hbyundu.shop.ui.goods.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.goods.GoodsUserDeliveryAPI;
import com.hbyundu.shop.rest.api.order.OrderPlaceAPI;
import com.hbyundu.shop.rest.base.Config;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsDeliveryModel;
import com.hbyundu.shop.rest.model.goods.GoodsDetailModel;
import com.hbyundu.shop.rest.model.order.OrderResultModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.apply.ApplyGoodsActivity;
import com.hbyundu.shop.ui.launcher.LoginActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.widget.MyRadioGroup;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.quantityview.QuantityView;

public class GoodsConfirmActivity extends BaseActivity implements View.OnClickListener {

    private static final int CODE_APPLY_LOANS = 1;

    private QuantityView mQuantityView;

    private GoodsDetailModel mGoodsDetailModel;

    private String mAttrsValue;

    private String mAttrsKey;

    private int mFenqiSelected = 1;

    private SVProgressHUD mProgressHUD;

    private ArrayList<GoodsDeliveryModel> mDeliveries = new ArrayList<>();

    private View mDeliveryView;

    private TextView mDeliveryAddressTextView;

    private TextView mDeliveryNameTextView;

    private TextView mDeliveryMobileTextView;

    private TextView mPaymentTextView;

    private TextView mRemarkEditText;

    private long mDeliveryId;

    private long mDealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_confirm);

        mGoodsDetailModel = (GoodsDetailModel) getIntent().getSerializableExtra("goods");
        mAttrsValue = getIntent().getStringExtra("attrs_value");
        mAttrsKey = getIntent().getStringExtra("attrs_key");

        initToolbar();
        initViews();
        getDeliveryData();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_confirmation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        initGoodsInfo();
        initDownPayment();
        findViewById(R.id.activity_goods_confirm_buy_tv).setOnClickListener(this);

        mDeliveryView = findViewById(R.id.activity_goods_confirm_delivery_ll);
        mDeliveryAddressTextView = (TextView) findViewById(R.id.activity_goods_confirm_delivery_address_tv);
        mDeliveryNameTextView = (TextView) findViewById(R.id.activity_goods_confirm_delivery_name_tv);
        mDeliveryMobileTextView = (TextView) findViewById(R.id.activity_goods_confirm_delivery_mobile_tv);
        mRemarkEditText = (EditText) findViewById(R.id.activity_goods_confirm_remark_et);
    }

    private void initGoodsInfo() {
        ImageView goodsImageView = (ImageView) findViewById(R.id.activity_goods_confirm_iv);
        TextView nameTextView = (TextView) findViewById(R.id.activity_goods_confirm_name_tv);
        TextView propertyTextView = (TextView) findViewById(R.id.activity_goods_confirm_property_tv);

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
        Glide.with(this).load(Config.SERVER + mGoodsDetailModel.img).apply(options).into(goodsImageView);
        nameTextView.setText(mGoodsDetailModel.name);
        propertyTextView.setText(mAttrsValue);
    }

    private void initDownPayment() {
        mQuantityView = (QuantityView) findViewById(R.id.activity_goods_confirm_quantity_view);
        mQuantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                showPayment();
            }

            @Override
            public void onLimitReached() {

            }
        });
        mPaymentTextView = (TextView) findViewById(R.id.activity_goods_confirm_down_payment_tv);
        showPayment();

        MyRadioGroup durationView = (MyRadioGroup) findViewById(R.id.activity_goods_confirm_choose_duration_rg);

        for (int i = 0; i < mGoodsDetailModel.installment.size(); i++) {
            List<Double> value = mGoodsDetailModel.installment.get(i);
            View view = createDurationView(value.get(1), value.get(0).intValue(), i + 1);
            durationView.addView(view);
        }

        durationView.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                mFenqiSelected = Integer.valueOf(String.valueOf(rb.getTag()));
            }
        });
    }

    private View createDurationView(double monthPay, int month, int position) {
        View durationView = LayoutInflater.from(this).inflate(R.layout.item_goods_confirm_choose_duration_repay, null, false);
        TextView textView = durationView.findViewById(R.id.item_goods_confirm_choose_duration_repay_tv);
        textView.setText(MoneyUtils.formatMoney(Double.valueOf(monthPay), Locale.ENGLISH) + " * " + month + " " + getString(R.string.months));
        RadioButton rb = (RadioButton) durationView.findViewById(R.id.item_goods_confirm_choose_duration_rb);
        rb.setId(position + 10000);
        rb.setTag(position);
        return durationView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_goods_confirm_buy_tv) {
            buyAction();
        }
    }

    private void buyAction() {
        if (!UserManager.getInstance(getApplicationContext()).isAuth()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

//        if (mFenqiSelected == 0 && mGoodsDetailModel.installment.size() > 0) {
//            mProgressHUD.showErrorWithStatus(getString(R.string.please_select_fenqi));
//            return;
//        }

        if (mDeliveryAddressTextView.getText().length() == 0) {
            mProgressHUD.showErrorWithStatus(getString(R.string.please_add_the_delivery_address));
            return;
        }

        showPayWay();
    }

    private void showPayWay() {
        new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.please_choose_the_method_of_payment)
                .setSingleChoiceItems(R.array.pay_way, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (i == 0) {
                            payWithBalance();
                        } else if (i == 1) {
                            payWithInstallment();
                        }
                    }
                }).show();
    }

    private void showPayment() {
        mPaymentTextView.setText(MoneyUtils.formatMoney(getAmount(), Locale.ENGLISH));
    }

    private void payWithBalance() {
        OrderPlaceAPI.getInstance().setDealId(0);
        pay();
    }

    private void payWithInstallment() {
        enterApplyLoans();
    }

    private void pay() {
        OrderPlaceAPI.getInstance().setUserId(UserManager.getInstance(getApplicationContext()).getUid()).setGoodsId(mGoodsDetailModel.id)
                .setGoodsAttr(mAttrsKey).setFenqi(mFenqiSelected).setCount(mQuantityView.getQuantity()).setDeliveryAddr(mDeliveryAddressTextView.getText().toString())
                .setDeliveryTel(mDeliveryMobileTextView.getText().toString()).setDeliveryName(mDeliveryNameTextView.getText().toString())
                .setMemo(mRemarkEditText.getText().toString());

        if (mDeliveryId == 0) {
            OrderPlaceAPI.getInstance().setAddressType(0).setAddressId(0);
        } else {
            OrderPlaceAPI.getInstance().setAddressType(1).setAddressId(mDeliveryId);
        }

        OrderPlaceAPI.getInstance().placeOrder(
                new SubscriberOnListener<OrderResultModel>() {
                    @Override
                    public void onSucceed(OrderResultModel data) {
                        mProgressHUD.dismissImmediately();

                        if (data.state == 0) {
                            mProgressHUD.showSuccessWithStatus(getString(R.string.place_order_success));
                            mProgressHUD.setOnDismissListener(new OnDismissListener() {
                                @Override
                                public void onDismiss(SVProgressHUD svProgressHUD) {
                                    finish();
                                }
                            });
                        } else if (data.state == 1) {
                            mProgressHUD.showErrorWithStatus(getString(R.string.remainder_not_enough));
                        } else {
                            mProgressHUD.showErrorWithStatus(getString(R.string.place_order_failure));
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressHUD.dismissImmediately();
                        mProgressHUD.showErrorWithStatus(getString(R.string.place_order_failure));
                    }
                });

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }

    private void getDeliveryData() {
        GoodsUserDeliveryAPI.getInstance().userDelivery(UserManager.getInstance(getApplicationContext()).getUid(), new SubscriberOnListener<List<GoodsDeliveryModel>>() {
            @Override
            public void onSucceed(List<GoodsDeliveryModel> data) {
                mDeliveries.addAll(data);
                showDelivery();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void showDelivery() {
        if (mDeliveries.size() > 0) {
            GoodsDeliveryModel deliveryModel = mDeliveries.get(0);
            mDeliveryNameTextView.setText(deliveryModel.deliveryName);
            mDeliveryMobileTextView.setText(deliveryModel.deliveryTel);
            mDeliveryAddressTextView.setText(deliveryModel.deliveryAddr);
            mDeliveryId = deliveryModel.id;
        }

        mDeliveryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsConfirmActivity.this, GoodsDeliveryActivity.class);
                intent.putExtra("delivery", mDeliveries);
                intent.putExtra("deliveryId", mDeliveryId);
                startActivity(intent);
            }
        });
    }

    private void changeDelivery(GoodsDeliveryModel delivery) {
        mDeliveryNameTextView.setText(delivery.deliveryName);
        mDeliveryMobileTextView.setText(delivery.deliveryTel);
        mDeliveryAddressTextView.setText(delivery.deliveryAddr);
        mDeliveryId = delivery.id;
    }

    private double getAmount() {
        double total = getIntent().getDoubleExtra("total", mGoodsDetailModel.score);
        BigDecimal unitPrice = new BigDecimal(total);
        BigDecimal quantity = new BigDecimal(mQuantityView.getQuantity());
        return unitPrice.multiply(quantity).doubleValue();
    }

    private void enterApplyLoans() {
        Intent intent = new Intent(GoodsConfirmActivity.this, ApplyGoodsActivity.class);
        intent.putExtra("money", getAmount());
        intent.putExtra("name", mGoodsDetailModel.name);
        startActivityForResult(intent, CODE_APPLY_LOANS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CODE_APPLY_LOANS == requestCode && resultCode == RESULT_OK && data != null) {
            mDealId = data.getLongExtra("dealId", 0);

            OrderPlaceAPI.getInstance().setDealId(mDealId);
            pay();
        }
    }

    @Subscriber(tag = "goods_change_delivery")
    public void changeGoodsDelivery(GoodsDeliveryModel delivery) {
        changeDelivery(delivery);
    }
}
