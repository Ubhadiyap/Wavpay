package com.hbyundu.shop.rest.api.order;

import com.hbyundu.shop.rest.model.order.OrderConfirmResultModel;
import com.hbyundu.shop.rest.model.order.OrderDataModel;
import com.hbyundu.shop.rest.model.order.OrderItemModel;
import com.hbyundu.shop.rest.model.order.OrderRepayDataModel;
import com.hbyundu.shop.rest.model.order.OrderRepayResultModel;
import com.hbyundu.shop.rest.model.order.OrderResultModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface OrderService {

    @GET("storeclient/store/store.do")
    Observable<OrderResultModel> placeOrder(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("goodsid") long goodsId, @Query("goodAttr") String goodAttr,
                                            @Query("fenqi") int fenqi, @Query("count") int count, @Query("deliveryAddr") String deliveryAddr,
                                            @Query("deliveryTel") String deliveryTel, @Query("deliveryName") String deliveryName, @Query("memo") String memo,
                                            @Query("addressType") int addressType, @Query("addressId") long addressId, @Query("dealId") long dealId);

    @GET("storeclient/store/store.do")
    Observable<OrderDataModel> orders(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("page") long page);

    @GET("storeclient/store/store.do")
    Observable<OrderItemModel> orderDetail(@Query("CmdId") String cmdId, @Query("orderId") long orderId);

    @GET("storeclient/store/store.do")
    Observable<OrderRepayDataModel> orderRepayList(@Query("CmdId") String cmdId, @Query("orderId") long orderId);

    @GET("storeclient/store/store.do")
    Observable<OrderRepayResultModel> orderRepay(@Query("CmdId") String cmdId, @Query("fenqiId") long fenqiId);

    @GET("storeclient/store/store.do")
    Observable<OrderConfirmResultModel> orderConfirm(@Query("CmdId") String cmdId, @Query("orderId") long orderId);
}
