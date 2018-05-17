package com.hbyundu.shop.rest.api.repay;

import com.hbyundu.shop.rest.model.repay.DealAddResultModel;
import com.hbyundu.shop.rest.model.repay.DealCateDataModel;
import com.hbyundu.shop.rest.model.repay.DealRateModel;
import com.hbyundu.shop.rest.model.repay.LoanDataModel;
import com.hbyundu.shop.rest.model.repay.RepayDataModel;
import com.hbyundu.shop.rest.model.repay.RepayResultModel;
import com.hbyundu.shop.rest.model.repay.RepaySubDataModel;
import com.hbyundu.shop.rest.model.repay.WithdrawResultModel;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sunhaigang on 2017/9/7.
 */

public interface RepayService {

    @GET("storeclient/client/getdealcate.do")
    Observable<DealCateDataModel> dealCate();

    @POST("storeclient/client/adddeal.do")
    Observable<DealAddResultModel> addDeal(@Query("userid") long userId, @Query("title") String title, @Query("borrow_amount") double borrowAmount,
                                           @Query("rate") double rate, @Query("repay_time") int repayTime, @Query("description") String description,
                                           @Query("dealyongtu") String dealyongtu, @Query("choubiaoqixian") int choubiaoqixian, @Body MultipartBody multipartBody);

    @GET("storeclient/store/store.do")
    Observable<RepayDataModel> repayList(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("state") int state, @Query("page") int page);

    @GET("storeclient/store/store.do")
    Observable<RepaySubDataModel> repayDetail(@Query("CmdId") String cmdId, @Query("dealId") long dealId);

    @GET("storeclient/store/store.do")
    Observable<RepayResultModel> repay(@Query("CmdId") String cmdId, @Query("repayId") long repayId, @Query("payPasswd") String payPasswd);

    @GET("storeclient/client/getminmaxlilv.do")
    Observable<DealRateModel> getDealRate();

    @GET("storeclient/store/store.do")
    Observable<RepayDataModel> withdrawList(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("state") int state, @Query("page") int page);

    @GET("storeclient/store/store.do")
    Observable<WithdrawResultModel> withdraw(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("orderid") String orderId, @Query("money") String money);

    @GET("storeclient/store/store.do")
    Observable<LoanDataModel> loanList(@Query("CmdId") String cmdId, @Query("userid") long userId, @Query("page") int page);
}
