package com.hbyundu.shop.rest.model.invest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class InvestListModel {
    @SerializedName("dealid")
    public Long dealId;
    @SerializedName("dealname")
    public String dealName;
    @SerializedName("borrow_amount")
    public double borrowAmount;
    @SerializedName("deal_status")
    public String dealStatus;
    @SerializedName("rate")
    public double rate;
    @SerializedName("repay_time")
    public int repayTime;
    @SerializedName("baifenbi")
    public String baifenbi;
    @SerializedName("repay_time_type")
    public int repayTimeType = 1;
}
