package com.hbyundu.shop.rest.model.invest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by apple on 2017/12/18.
 */

public class InvestListDataModel {
    @SerializedName("accessarray")
    public List<InvestListModel> data;
}
