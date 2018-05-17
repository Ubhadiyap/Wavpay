package com.hbyundu.shop.rest.model.invest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class InvestDetailModel {
    public String name;
    public String dealsn;
    public double totalmoney;
    public double symoney;
    public double minloney;
    public double rate;
    public int repay_time;
    public String method;
    public String sytime;
    public int repay_time_type;
    public String deal_status;
    public long user_id;
    public String baifenbi;
    public double money_encrypt;
    @SerializedName("publish_wait")
    public int publishWait;
    @SerializedName("is_delete")
    public int isDelete;
    @SerializedName("delete_msg")
    public String deleteMsg;
    @SerializedName("custom_status")
    public int customStatus;
}
