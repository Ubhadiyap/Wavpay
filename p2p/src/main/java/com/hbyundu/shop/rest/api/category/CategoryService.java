package com.hbyundu.shop.rest.api.category;

import com.hbyundu.shop.rest.model.category.CategoryModel;
import com.hbyundu.shop.rest.model.launcher.LoginResultModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface CategoryService {

    @GET("storeclient/store/store.do")
    Observable<CategoryModel> category(@Query("CmdId") String cmdId);
}
