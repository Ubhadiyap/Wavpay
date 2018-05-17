package com.hbyundu.shop.rest.api.recommend;

import com.hbyundu.shop.rest.model.recommend.RecommendCategoryDataModel;
import com.hbyundu.shop.rest.model.recommend.RecommendCategoryModel;
import com.hbyundu.shop.rest.model.recommend.RecommendGoodsDataModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface RecommendService {

    @GET("storeclient/store/store.do")
    Observable<RecommendCategoryDataModel> category(@Query("CmdId") String cmdId);

    @GET("storeclient/store/store.do")
    Observable<RecommendGoodsDataModel> goods(@Query("CmdId") String cmdId, @Query("cateid") int categoryId);
}
