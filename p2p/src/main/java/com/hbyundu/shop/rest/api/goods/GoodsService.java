package com.hbyundu.shop.rest.api.goods;

import com.hbyundu.shop.rest.model.goods.GoodsDeliveryDataModel;
import com.hbyundu.shop.rest.model.goods.GoodsDetailModel;
import com.hbyundu.shop.rest.model.goods.GoodsListDataModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface GoodsService {

    @GET("storeclient/store/store.do")
    Observable<GoodsListDataModel> categoryGoods(@Query("CmdId") String cmdId, @Query("cateid") long categoryId, @Query("pages") int pages);

    @GET("storeclient/store/store.do")
    Observable<GoodsDetailModel> goodsDetail(@Query("CmdId") String cmdId, @Query("goodsid") long goodsId);

    @GET("storeclient/store/store.do")
    Observable<GoodsListDataModel> search(@Query("CmdId") String cmdId, @Query("goodsname") String goodsName, @Query("pages") int pages);

    @GET("storeclient/store/store.do")
    Observable<GoodsDeliveryDataModel> userDelivery(@Query("CmdId") String cmdId, @Query("userId") long userId);
}
