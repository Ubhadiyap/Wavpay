package com.hbyundu.shop.rest.model.recommend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class RecommendGoodsModel {
    @SerializedName("goodsid")
    public Long goodsId;
    @SerializedName("goodsname")
    public String goodsName;
    public String img;
    public String description;
    public double score;
    @SerializedName("hotimg")
    public String hotImg;
    @SerializedName("is_hot")
    public int isHot;
}
