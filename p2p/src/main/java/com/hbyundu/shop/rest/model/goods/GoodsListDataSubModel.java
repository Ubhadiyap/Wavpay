package com.hbyundu.shop.rest.model.goods;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by apple on 2017/12/18.
 */

public class GoodsListDataSubModel {
    @SerializedName("list")
    public List<GoodsListModel> data;
}
