package com.hbyundu.shop.rest.model.launcher;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 2017/12/18.
 */

public class LoginResultModel {
    @SerializedName("userid")
    public Long userId;
    public String res;
    @SerializedName("errormessage")
    public String error;
    public String mobile;
}
