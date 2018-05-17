package com.hbyundu.shop.rest.model.repay;

import java.util.List;

/**
 * Created by apple on 2017/12/22.
 */

public class DealRateModel {

    public List<DealRateItemModel> lilv;

    public static class DealRateItemModel {
        public String month;
        public String min;
        public String max;
    }
}
