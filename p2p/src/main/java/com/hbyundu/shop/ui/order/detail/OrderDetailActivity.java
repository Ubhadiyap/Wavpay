package com.hbyundu.shop.ui.order.detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnShowListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.order.OrderConfirmAPI;
import com.hbyundu.shop.rest.api.order.OrderDetailAPI;
import com.hbyundu.shop.rest.base.Config;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.order.OrderConfirmResultModel;
import com.hbyundu.shop.rest.model.order.OrderItemModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.util.Locale;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private OrderItemModel mOrderItemModel;

    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initTitleBar();
        initViews();
        getData();
    }

    private void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_detail);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        long orderId = getIntent().getLongExtra("orderId", 0);
        OrderDetailAPI.getInstance().orderDetail(orderId, new SubscriberOnListener<OrderItemModel>() {
            @Override
            public void onSucceed(OrderItemModel data) {
                mOrderItemModel = data;
                setData();
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void setData() {
        if (mOrderItemModel == null) {
            return;
        }

        TextView deliveryNameTextView = (TextView) findViewById(R.id.activity_order_detail_delivery_name_tv);
        TextView mobileTextView = (TextView) findViewById(R.id.activity_order_detail_delivery_mobile_tv);
        TextView addressTextView = (TextView) findViewById(R.id.activity_order_detail_delivery_address_tv);
        ImageView goodsImageView = (ImageView) findViewById(R.id.activity_order_detail_iv);
        TextView goodsNameTextView = (TextView) findViewById(R.id.activity_order_detail_name_tv);
        TextView propertyTextView = (TextView) findViewById(R.id.activity_order_detail_property_tv);
        TextView priceTextView = (TextView) findViewById(R.id.activity_order_detail_price_tv);
        TextView orderNoTextView = (TextView) findViewById(R.id.activity_order_detail_order_no_tv);
        TextView orderTimeTextView = (TextView) findViewById(R.id.activity_order_detail_order_time_tv);
        TextView amountTextView = (TextView) findViewById(R.id.activity_order_detail_order_amount_tv);
        TextView statusTextView = (TextView) findViewById(R.id.activity_order_detail_status_tv);

        deliveryNameTextView.setText(mOrderItemModel.deliveryName);
        mobileTextView.setText(mOrderItemModel.deliveryTel);
        addressTextView.setText(getString(R.string.address_title) +  mOrderItemModel.deliveryAddr);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
        Glide.with(this).load(Config.SERVER + mOrderItemModel.img).apply(options).into(goodsImageView);
        goodsNameTextView.setText(mOrderItemModel.goodName);
        priceTextView.setText(MoneyUtils.formatMoney(mOrderItemModel.sumPrice, Locale.ENGLISH));
        orderTimeTextView.setText(mOrderItemModel.date);
        amountTextView.setText(MoneyUtils.formatMoney(mOrderItemModel.sumPrice, Locale.ENGLISH));
        orderNoTextView.setText(mOrderItemModel.orderNo);
        statusTextView.setText(getStatusText(mOrderItemModel.orderState));

        StringBuffer propertySb = new StringBuffer();
        for (String property: mOrderItemModel.attr) {
            if (propertySb.toString().length() > 0) {
                propertySb.append(",");
            }
            propertySb.append(property.replace("@_@", ":"));
        }
        propertyTextView.setText(propertySb.toString());

        if (mOrderItemModel.orderState == 1) {
            TextView repayTextView = (TextView) findViewById(R.id.activity_order_detail_order_action_tv);
            repayTextView.setOnClickListener(this);
            ((View)repayTextView.getParent()).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.activity_order_detail_sv).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_order_detail_order_action_tv) {
            confirmAction();
        }
    }

    private void confirmAction() {
        new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                .setMessage(R.string.confirm_receipt_prompt)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long orderId = getIntent().getLongExtra("orderId", 0);
                        orderConfirm(orderId);
                    }
                })
                .show();
    }

    private void orderConfirm(long orderId) {
        OrderConfirmAPI.getInstance().orderConfirm(orderId, new SubscriberOnListener<OrderConfirmResultModel>() {
            @Override
            public void onSucceed(OrderConfirmResultModel data) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showSuccessWithStatus(getString(R.string.operation_succeeded));
                mProgressHUD.setOnShowListener(new OnShowListener() {
                    @Override
                    public void onShow(SVProgressHUD svProgressHUD) {
                        finish();
                    }
                });
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
                mProgressHUD.showErrorWithStatus(getString(R.string.operation_failed));
            }
        });
        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }

    private String getStatusText(int status) {
        switch (status) {
            case 0:
                return getString(R.string.not_shipped);
            case 1:
                return getString(R.string.shipped);
            case 4:
                return getString(R.string.goods_signed_in);
            case 5:
                return getString(R.string.unpaid);
            default:
                return getString(R.string.unknown);
        }
    }
}
