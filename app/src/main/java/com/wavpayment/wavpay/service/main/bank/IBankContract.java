package com.wavpayment.wavpay.service.main.bank;

import com.lan.sponge.mvp.BaseView;

/**
 *
 * Created by Administrator on 2017/12/27.
 */

public interface IBankContract {

    interface View extends BaseView{
        void onCardList(String response);
    }

    interface Presenter{
        void cardList();//获取银行卡列表
    }
}
