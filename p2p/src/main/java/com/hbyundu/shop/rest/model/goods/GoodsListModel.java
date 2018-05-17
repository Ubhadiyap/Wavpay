package com.hbyundu.shop.rest.model.goods;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class GoodsListModel {
    @SerializedName("id")
    public Long id;
    @SerializedName("name")
    public String name;
    public double yfk;
    public String img;
    public double score;
}
