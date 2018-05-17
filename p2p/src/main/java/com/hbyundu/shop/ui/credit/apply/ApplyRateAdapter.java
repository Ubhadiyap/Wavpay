package com.hbyundu.shop.ui.credit.apply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.model.repay.DealRateModel;
import com.hbyundu.shop.vendor.util.AverageCapitalPlusInterestUtils;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 2018/2/11.
 */

public class ApplyRateAdapter extends BaseAdapter {

    private Context mContext;

    private List<DealRateModel.DealRateItemModel> mRates;

    private double mTotalMoney;

    private int mSelected = -1;

    public ApplyRateAdapter(Context context, List<DealRateModel.DealRateItemModel> rates) {
        this.mContext = context;
        this.mRates = rates;
    }

    public void setTotalMoney(double totalMoney) {
        this.mTotalMoney =totalMoney;
    }

    public void setSelected(int position) {
        this.mSelected = position;
    }

    public int getSelected() {
        return mSelected;
    }

    @Override
    public int getCount() {
        return mRates == null ? 0 : mRates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_apply_goods_term, viewGroup, false);
        TextView rateTextView = view.findViewById(R.id.item_apply_goods_term_rate_tv);
        TextView detailTextView = view.findViewById(R.id.item_apply_goods_term_detail_tv);
        CheckBox checkBox = view.findViewById(R.id.item_apply_goods_term_cb);

        DealRateModel.DealRateItemModel rate = mRates.get(i);
        rateTextView.setText("F.I.R @ " + rate.min + "%");
        detailTextView.setText(String.format(mContext.getString(R.string.apply_rate_format), rate.month,
                MoneyUtils.formatMoney(AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(mTotalMoney,
                        Double.valueOf(rate.min) / 100, Integer.valueOf(rate.month)), Locale.ENGLISH)));
        checkBox.setChecked(i == mSelected);

        return view;
    }
}
