package com.hbyundu.shop.rest.model.goods;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2017/12/18.
 */

public class GoodsDetailModel implements Serializable{
    public long id;
    public String name;
    @SerializedName("sub_name")
    public String subName;
    public String img;
    public String img1;
    public String img2;
    public String brief;
    public String wapdes;
    public double score;
    @SerializedName("shuxing")
    public List<Map<String, String>> properties;
    @SerializedName("fenqi")
    public List<List<Double>> installment;
}
