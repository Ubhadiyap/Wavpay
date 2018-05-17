package com.hbyundu.shop.rest.model.recommend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class RecommendCategoryModel {
    @SerializedName("cateid")
    public int cateId;
    @SerializedName("catename")
    public String cateName;
}
