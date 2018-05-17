package com.wavpayment.wavpay.service.main.index;


import com.lan.sponge.mvp.BaseView;
import com.wavpayment.wavpay.service.common.CommonEntity;

import java.util.ArrayList;

public interface IIndexContract {

    interface View extends BaseView{
        void onInitView(ArrayList<CommonEntity> data);
        void onBanner(ArrayList<String> es);
    }

    interface Presenter{
        void initView();
        void banner();
    }
}
