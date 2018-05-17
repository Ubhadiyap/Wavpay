package com.hbyundu.shop.ui.credit.invest.list;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.R.color;
import com.hbyundu.shop.application.App;
import com.hbyundu.shop.vendor.adapter.SDBaseAdapter;
import com.hbyundu.shop.vendor.util.SDTypeParseUtil;
import com.hbyundu.shop.vendor.util.SDViewBinder;
import com.hbyundu.shop.vendor.util.ViewHolder;
import com.hbyundu.shop.vendor.widget.RoundProgressBar;

import java.util.List;

public class InvestListAdapter extends SDBaseAdapter<DealsActItemModel> {

    public InvestListAdapter(List<DealsActItemModel> listModel, Activity activity) {
        super(listModel, activity);
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, DealsActItemModel model) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_invest_list, null);
        }
        TextView tvStatus = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_borrow_status); // 状态
        TextView tvProjectName = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_borrow_name); // 项目名称
        TextView tvAmountOfMoney = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_amount_of_money); // 金额
        TextView tvAmountOfMoneyLabel = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_amount_of_money_label);
        TextView tvRate = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_borrow_rate_year); // 年利率
        TextView tvRateLabel = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_borrow_rate_year_label);
        TextView tvTimeLimit = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_time_limit); // 期限
        TextView tvTimeLimitLabel = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_txt_time_limit_label);
        LinearLayout llBottom = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_lin_bottom); // 底部布局
        LinearLayout llTop = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_ll_top); // 头部布局
        RoundProgressBar rpbProgress = ViewHolder.get(convertView, R.id.item_lsv_borrow_invest_pgb_borrow_progress); // 圆形进度条

        if (model != null) {
            // --------设置默认颜色---------
            SDViewBinder.setTextViewColorByColorId(tvStatus, color.white);
            SDViewBinder.setTextViewColorByColorId(tvProjectName, color.white);
            SDViewBinder.setTextViewColorByColorId(tvAmountOfMoney, color.text_gray);
            SDViewBinder.setTextViewColorByColorId(tvAmountOfMoneyLabel, color.text_gray);
            SDViewBinder.setTextViewColorByColorId(tvRate, color.text_orange);
            SDViewBinder.setTextViewColorByColorId(tvRateLabel, color.text_gray);
            SDViewBinder.setTextViewColorByColorId(tvTimeLimit, color.text_gray);
            SDViewBinder.setTextViewColorByColorId(tvTimeLimitLabel, color.text_gray);
            llBottom.setBackgroundResource(R.drawable.layer_invest_item_bottom);
            llTop.setBackgroundResource(R.drawable.layer_invest_item_top);

            // ------设置默认进度条-----
            rpbProgress.setDefaultConfig();
            // ------------------设置选中项------------------
            if (model.isSelect()) {
                SDViewBinder.setTextViewColorByColorId(tvAmountOfMoney, color.white);
                SDViewBinder.setTextViewColorByColorId(tvAmountOfMoneyLabel, color.white);
                SDViewBinder.setTextViewColorByColorId(tvRate, color.white);
                SDViewBinder.setTextViewColorByColorId(tvRateLabel, color.white);
                SDViewBinder.setTextViewColorByColorId(tvTimeLimit, color.white);
                SDViewBinder.setTextViewColorByColorId(tvTimeLimitLabel, color.white);
                rpbProgress.setTextColor(mActivity.getResources().getColor(color.white));
                llBottom.setBackgroundResource(R.drawable.layer_invest_item_bottom_select);
            }

            // ------设置进度条-----
            if (model.isLoaning()) {
                rpbProgress.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_blue));
            }
            if (!TextUtils.isEmpty(model.getProgress_point())) {
                rpbProgress.setProgress(SDTypeParseUtil.getFloatFromString(model.getProgress_point(), 0));
            }

            // -------借款类型-------------
            tvStatus.setText(model.getDeal_status_format_string());
            if (model.isLoaning()) {
                llTop.setBackgroundResource(R.drawable.layer_invest_item_top_borrowing);
            }

            // -------借款名字--------
            SDViewBinder.setTextView(tvProjectName, model.getName(), App.getStringById(R.string.not_found));
            // -------金额--------
            SDViewBinder.setTextView(tvAmountOfMoney, model.getBorrow_amount_format(), App.getStringById(R.string.not_found));
            // -------年利率--------
            SDViewBinder.setTextView(tvRate, model.getRate_foramt(), App.getStringById(R.string.not_found));
            // -------期限--------
            if (model.getRepay_time() != null && model.getRepay_time_type_format() != null) {
                tvTimeLimit.setText(model.getRepay_time() + model.getRepay_time_type_format());
            } else {
                tvTimeLimit.setText(App.getStringById(R.string.not_found));
            }

        }

        return convertView;
    }

}
