package com.hbyundu.shop.rest.api.invest;

import com.hbyundu.shop.rest.model.invest.InvestDetailModel;
import com.hbyundu.shop.rest.model.invest.InvestListDataModel;
import com.hbyundu.shop.rest.model.invest.InvestResultModel;
import com.hbyundu.shop.rest.model.invest.RechargeRateDataModel;
import com.hbyundu.shop.rest.model.invest.RechargeResultModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface InvestService {

    @GET("storeclient/store/store.do")
    Observable<InvestListDataModel> investList(@Query("CmdId") String cmdId, @Query("pages") int pages);

    @GET("storeclient/store/store.do")
    Observable<InvestDetailModel> investDetail(@Query("CmdId") String cmdId, @Query("dealid") long dealId, @Query("userid") long userid);

    @GET("storeclient/store/store.do")
    Observable<InvestListDataModel> investMineList(@Query("CmdId") String cmdId, @Query("pages") long page, @Query("userid") long userid);

    @GET("storeclient/store/store.do")
    Observable<InvestResultModel> invest(@Query("CmdId") String cmdId, @Query("dealId") long dealId, @Query("userid") long userid,
                                         @Query("payPasswd") String payPasswd, @Query("investMoney") Double investMoney);

    @GET("storeclient/store/store.do")
    Observable<RechargeRateDataModel> getRate(@Query("CmdId") String cmdId);

    @GET("storeclient/store/store.do")
    Observable<RechargeResultModel> recharge(@Query("CmdId") String cmdId, @Query("userId") long userId,
                                             @Query("rateId") long rateId, @Query("money") double money);
}
