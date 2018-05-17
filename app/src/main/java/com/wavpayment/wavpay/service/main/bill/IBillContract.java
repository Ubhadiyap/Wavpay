package com.wavpayment.wavpay.service.main.bill;

/**
 *
 * Created by Administrator on 2017/12/29.
 */

public interface IBillContract {
    interface View{
        void success(String response);
    }

    interface Presenter{
        void all(String ...args);//全部账单
    }
}
