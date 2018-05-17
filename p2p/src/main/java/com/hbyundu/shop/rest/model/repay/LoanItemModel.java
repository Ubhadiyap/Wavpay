package com.hbyundu.shop.rest.model.repay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/26.
 */

public class LoanItemModel {
    public long dealId;
    public String title;
    public double borrowAmount;
    public double rate;
    public int repayLimit;
    public double repay;
    public double imposeMoney;
    public String date;
    public boolean repayTimeType;
    public String orId;
    public double avaliableMoney;
    public long createTime;
    @SerializedName("deal_status")
    public int dealStatus;
    @SerializedName("publish_wait")
    public int publishWait;
    @SerializedName("is_delete")
    public int isDelete;
    @SerializedName("delete_msg")
    public String deleteMsg;
    @SerializedName("custom_status")
    public int customStatus;
}
