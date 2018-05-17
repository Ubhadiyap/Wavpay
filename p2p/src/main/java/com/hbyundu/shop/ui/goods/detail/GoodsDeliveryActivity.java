package com.hbyundu.shop.ui.goods.detail;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.order.OrderPlaceAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsDeliveryModel;
import com.hbyundu.shop.rest.model.order.OrderResultModel;
import com.hbyundu.shop.ui.BaseActivity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

public class GoodsDeliveryActivity extends BaseActivity {

    private ArrayList<GoodsDeliveryModel> mDeliveries = new ArrayList<>();

    private GoodsDeliveryAdapter mGoodsDeliveryAdapter;

    private ListView mListView;

    private PopupWindow mDeliveryPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_delivery);

        mDeliveries = (ArrayList<GoodsDeliveryModel>) getIntent().getSerializableExtra("delivery");

        initToolbar();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_goods_delivery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            addDelivery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.delivery_address);
    }

    private void initViews() {
        mGoodsDeliveryAdapter = new GoodsDeliveryAdapter(this, mDeliveries, 0);

        mListView = (ListView) findViewById(R.id.activity_goods_delivery_lv);
        mListView.setAdapter(mGoodsDeliveryAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mGoodsDeliveryAdapter.select(i);
            }
        });

        findViewById(R.id.activity_goods_delivery_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(mGoodsDeliveryAdapter.getSelect(), "goods_change_delivery");
                finish();
            }
        });
    }

    private void addDelivery() {
        if (mDeliveryPopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.popup_add_delivery, null);
            mDeliveryPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mDeliveryPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mDeliveryPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

            final EditText nameEditText = view.findViewById(R.id.popup_add_delivery_name_et);
            final EditText mobileEditText = view.findViewById(R.id.popup_add_delivery_mobile_et);
            final EditText addressEditText = view.findViewById(R.id.popup_add_delivery_address_et);
            view.findViewById(R.id.popup_add_delivery_submit_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(nameEditText.getText().toString())
                            || TextUtils.isEmpty(mobileEditText.getText().toString())
                            || TextUtils.isEmpty(addressEditText.getText().toString())) {
                        Toast.makeText(GoodsDeliveryActivity.this, getString(R.string.please_fill_delivery_info), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GoodsDeliveryModel deliveryModel = new GoodsDeliveryModel();
                    deliveryModel.id = 0;
                    deliveryModel.deliveryName = nameEditText.getText().toString();
                    deliveryModel.deliveryTel = mobileEditText.getText().toString();
                    deliveryModel.deliveryAddr = addressEditText.getText().toString();
                    EventBus.getDefault().post(deliveryModel, "goods_change_delivery");

                    mDeliveryPopupWindow.dismiss();
                    finish();
                }
            });

        }

        mDeliveryPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}
