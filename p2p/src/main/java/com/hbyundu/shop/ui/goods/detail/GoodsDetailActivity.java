package com.hbyundu.shop.ui.goods.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dl7.tag.TagLayout;
import com.dl7.tag.TagView;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.goods.GoodsDetailAPI;
import com.hbyundu.shop.rest.base.Config;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.goods.GoodsDetailModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GoodsDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, OnItemClickListener {

    private View mContainerView;

    private TextView mFullPriceTextView;

    private TextView mTotalPayTextView;

    private LinearLayout mTagContainerView;

    private SVProgressHUD mProgressHUD;

    private ConvenientBanner mBanner;

    private GoodsDetailModel mGoodsDetailModel;

    private long mGoodsId;

    private HashMap<String, String> mSelectedPropertyMap = new HashMap<>();

    private String mTotalMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        initToolbar();
        initViews();
        initFloatingButton();

        mGoodsId = getIntent().getLongExtra("goodsId", 0);
        getGoodsDetail(mGoodsId);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void initFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextAction();
            }
        });
    }

    private void initViews() {
        mContainerView = findViewById(R.id.activity_goods_detail_content_cl);
        mTotalPayTextView = (TextView) findViewById(R.id.activity_goods_detail_total_pay_tv);
        findViewById(R.id.activity_goods_detail_next_tv).setOnClickListener(this);

        mProgressHUD = new SVProgressHUD(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getGoodsDetail(long goodsId) {
        GoodsDetailAPI.getInstance().goodsDetail(goodsId, new SubscriberOnListener<GoodsDetailModel>() {
            @Override
            public void onSucceed(GoodsDetailModel data) {
                mGoodsDetailModel = data;
                mGoodsDetailModel.id = mGoodsId;
                setBanner();
                setGoodsInfo();
                setProperties();
                setDescription();
                mContainerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void setBanner() {
        if (mGoodsDetailModel != null) {
            List<String> images = new ArrayList<>();
            images.add(Config.SERVER + mGoodsDetailModel.img);
            if (!TextUtils.isEmpty(mGoodsDetailModel.img1)) {
                images.add(Config.SERVER + mGoodsDetailModel.img1);
            }
            if (!TextUtils.isEmpty(mGoodsDetailModel.img2)) {
                images.add(Config.SERVER + mGoodsDetailModel.img2);
            }
            mBanner = (ConvenientBanner) findViewById(R.id.activity_goods_detail_banner);
            mBanner.setPages(new CBViewHolderCreator<BannerImageHolderView>() {
                @Override
                public BannerImageHolderView createHolder() {
                    return new BannerImageHolderView();
                }
            }, images)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            mBanner.setOnPageChangeListener(this);
            mBanner.setOnItemClickListener(this);
            mBanner.notifyDataSetChanged();
        }
    }

    private void setGoodsInfo() {
        if (mGoodsDetailModel != null) {
            TextView nameTextView = (TextView) findViewById(R.id.content_goods_detail_name_tv);
            mFullPriceTextView = (TextView) findViewById(R.id.content_goods_detail_full_price_tv);
            TextView downPaymentTextView = (TextView) findViewById(R.id.content_goods_detail_down_payment_tv);

            nameTextView.setText(mGoodsDetailModel.name);
            mFullPriceTextView.setText(getString(R.string.full_price) + " " + MoneyUtils.formatMoney(mGoodsDetailModel.score, Locale.ENGLISH));
            downPaymentTextView.setText( MoneyUtils.formatMoney(mGoodsDetailModel.score, Locale.ENGLISH));
        }
    }

    private void setProperties() {
        if (mGoodsDetailModel != null) {
            mTagContainerView = (LinearLayout) findViewById(R.id.content_goods_detail_tag_container_ll);
            if (mGoodsDetailModel.properties.size() == 0) {
                mTagContainerView.setVisibility(View.GONE);
                mTotalPayTextView.setText(MoneyUtils.formatMoney(Double.valueOf(mGoodsDetailModel.score), Locale.ENGLISH));
                findViewById(R.id.content_goods_detail_tag_divider).setVisibility(View.GONE);
                return;
            }

            Map<String, List<String>> propertyMap = new HashMap<>();
            for (Map<String, String> map : mGoodsDetailModel.properties) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!"stock_cfg".equals(key) && !"total".equals(key)
                            && !"goods_id".equals(key) && !"attr_str".equals(key)) {
                        if (!propertyMap.containsKey(key)) {
                            List<String> values = new ArrayList<>();
                            propertyMap.put(key, values);
                        }
                        List<String> values = propertyMap.get(key);
                        if (!values.contains(value)) {
                            propertyMap.get(key).add(value);
                        }
                    }
                }
            }

            Iterator<Map.Entry<String, List<String>>> it = propertyMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<String>> entry = it.next();
                createPropertyView(entry.getKey(), entry.getValue());
            }
        }
    }

    private void createPropertyView(final String name, final List<String> values) {
        View tagItemView = LayoutInflater.from(this).inflate(R.layout.item_content_goods_detail_tag, null, false);
        mTagContainerView.addView(tagItemView);

        TextView nameTextView = tagItemView.findViewById(R.id.item_content_goods_detail_tag_name_tv);
        final TagLayout tagLayout =  tagItemView.findViewById(R.id.item_content_goods_detail_tagView);

        nameTextView.setText(name);
        tagLayout.setTags(values);
        tagLayout.setTagCheckListener(new TagView.OnTagCheckListener() {
            @Override
            public void onTagCheck(int i, String s, boolean b) {
                if (b) {
                    mSelectedPropertyMap.put(name, s);
                } else {
                    mSelectedPropertyMap.remove(name);
                }
                changePrice();
            }
        });
    }

    private void setDescription() {
        if (mGoodsDetailModel != null) {
            TextView descriptionTextView = (TextView) findViewById(R.id.content_goods_detail_description_tv);
            descriptionTextView.setText(Html.fromHtml(mGoodsDetailModel.wapdes));
        }
    }

    private void changePrice() {
        boolean isHit = false;
        for (Map<String, String> map : mGoodsDetailModel.properties) {
            String store = null;
            String total = null;
            String attr = null;
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if ("stock_cfg".equals(key)) {
                    store = value;
                    continue;
                }
                if ("total".equals(key)) {
                    total = value;
                    continue;
                }
                if ("attr_str".equals(key)) {
                    attr = value;
                    continue;
                }

                if (!"stock_cfg".equals(key) && !"total".equals(key)
                        && !"goods_id".equals(key) && !"attr_str".equals(key)) {
                    if (mSelectedPropertyMap.containsKey(key) && value.equals(mSelectedPropertyMap.get(key))) {
                        isHit = true;
                    } else {
                        isHit = false;
                        break;
                    }
                }
            }

            if (isHit) {
                mFullPriceTextView.setText(getString(R.string.full_price) + MoneyUtils.formatMoney(Double.valueOf(total), Locale.ENGLISH));
                mTotalPayTextView.setText(MoneyUtils.formatMoney(Double.valueOf(total), Locale.ENGLISH));
                mTotalPayTextView.setTag(attr);
                mTotalMoney = total;
                return;
            }
        }
        mFullPriceTextView.setText(getString(R.string.full_price) + " " + MoneyUtils.formatMoney(mGoodsDetailModel.score, Locale.ENGLISH));
        mTotalPayTextView.setText("");
        mTotalPayTextView.setTag(null);
        mTotalMoney = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_goods_detail_next_tv) {
            nextAction();
        }
    }

    private void nextAction() {
        if (mTotalPayTextView.getText().toString().length() == 0) {
            mProgressHUD.showErrorWithStatus(getString(R.string.please_select_goods_attrs));
            return;
        }
        enterConfirmOrder();
    }

    private void enterConfirmOrder() {
        StringBuffer sb = new StringBuffer();
        Iterator<Map.Entry<String, String>> it = mSelectedPropertyMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (sb.toString().length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getValue());
        }

        Intent intent = new Intent(this, GoodsConfirmActivity.class);
        intent.putExtra("goods", mGoodsDetailModel);
        if (mSelectedPropertyMap.size() != 0) {
            intent.putExtra("attrs_value", sb.toString());
            intent.putExtra("attrs_key", mTotalPayTextView.getTag().toString());
        }
        if (mTotalMoney != null) {
            intent.putExtra("total", Double.valueOf(mTotalMoney).doubleValue());
        } else {
            intent.putExtra("total", mGoodsDetailModel.score);
        }

        startActivity(intent);
    }

    public class BannerImageHolderView implements Holder<String> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.mipmap.ic_goods_default_image);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String ad) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
            Glide.with(GoodsDetailActivity.this).load(ad).apply(options).into(imageView);
        }
    }
}
