package com.hbyundu.shop.rest.model.recommend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by apple on 2017/12/18.
 */

public class RecommendCategoryDataModel {
    @SerializedName("accessarray")
    public List<RecommendCategoryModel> data;
}
