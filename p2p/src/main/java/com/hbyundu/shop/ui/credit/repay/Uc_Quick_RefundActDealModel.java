package com.hbyundu.shop.ui.credit.repay;

import android.content.Context;

import com.hbyundu.shop.R;
import com.hbyundu.shop.vendor.util.SDFormatUtil;

public class Uc_Quick_RefundActDealModel {

    private String id = null;
    private String name = null;
    private String sub_name = null;
    private String borrow_amount = null;
    private String borrow_amount_format = null;
    private String rate = null;// 利率
    private String rate_foramt = null;
    private String repay_time = null; // 期限
    private String repay_time_format = null; // 期限
    private String repay_time_type = null; // 0：天 ，其他的：月
    private String next_repay_time = null; // 下次还款日

    private String repay_money = null; //已还总额
    private String repay_money_format = null; //已还总额
    private String need_remain_repay_money = null; //待还金额
    private String need_remain_repay_money_format = null; //待还金额


    public String getRepay_time_format() {
        return repay_time_format;
    }

    public void setRepay_time_format(String repay_time_format) {
        this.repay_time_format = repay_time_format;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getBorrow_amount() {
        return borrow_amount;
    }

    public void setBorrow_amount(String borrow_amount) {
        this.borrow_amount = borrow_amount;
    }

    public String getBorrow_amount_format() {
        return borrow_amount_format;
    }

    public void setBorrow_amount_format(String borrow_amount_format) {
        this.borrow_amount_format = borrow_amount_format;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate_foramt() {
        return rate_foramt;
    }

    public void setRate_foramt(String rate_foramt) {
        this.rate_foramt = rate_foramt;
    }

    public String getRepay_time() {
        return repay_time;
    }

    public void setRepay_time(String repay_time) {
        this.repay_time = repay_time;
    }

    public String getRepay_time_type() {
        return repay_time_type;
    }

    public void setRepay_time_type(Context context, String repay_time_type) {
        this.repay_time_type = repay_time_type;
        if (this.repay_time_type != null) {
            if (this.repay_time_type.equals("0")) {
                this.repay_time_format = this.repay_time + " " + context.getString(R.string.days);
            } else {
                this.repay_time_format = this.repay_time + " " + context.getString(R.string.months);
            }
        }
    }

    public String getNext_repay_time() {
        return next_repay_time;
    }

    public void setNext_repay_time(String next_repay_time) {
        this.next_repay_time = next_repay_time;
    }

    public String getRepay_money() {
        return repay_money;
    }

    public void setRepay_money(String repay_money) {
        this.repay_money = repay_money;
        this.repay_money_format = SDFormatUtil.formatMoneyChina(repay_money);
    }

    public String getRepay_money_format() {
        return repay_money_format;
    }

    public void setRepay_money_format(String repay_money_format) {
        this.repay_money_format = repay_money_format;
    }

    public String getNeed_remain_repay_money() {
        return need_remain_repay_money;
    }

    public void setNeed_remain_repay_money(String need_remain_repay_money) {
        this.need_remain_repay_money = need_remain_repay_money;
        this.need_remain_repay_money_format = SDFormatUtil.formatMoneyChina(need_remain_repay_money);
    }

    public String getNeed_remain_repay_money_format() {
        return need_remain_repay_money_format;
    }

    public void setNeed_remain_repay_money_format(String need_remain_repay_money_format) {
        this.need_remain_repay_money_format = need_remain_repay_money_format;
    }
}
