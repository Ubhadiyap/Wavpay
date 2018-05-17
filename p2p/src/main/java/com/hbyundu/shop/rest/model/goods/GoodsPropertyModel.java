package com.hbyundu.shop.rest.model.goods;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class GoodsPropertyModel {
    @SerializedName("stock_cfg")
    public String cf;
    @SerializedName("goods_id")
    public Long goodsId;
    @SerializedName("my0")
    public String property0;
    @SerializedName("my1")
    public String property1;
    @SerializedName("my2")
    public String property2;
    @SerializedName("my3")
    public String property3;
    @SerializedName("my4")
    public String property4;
    @SerializedName("my5")
    public String property5;
}
