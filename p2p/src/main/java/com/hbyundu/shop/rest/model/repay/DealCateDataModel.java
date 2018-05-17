package com.hbyundu.shop.rest.model.repay;

import java.util.List;

/**
 * Created by apple on 2017/12/22.
 */

public class DealCateDataModel {

    public List<DealCateYongTuModel> accessarray;
    public List<String> month;
    public String huankuanzhouqi;
    public String huankuanfangshi;
    public String choubiaoqixian;

    public static class DealCateYongTuModel {
        public int yongtuid;
        public String yongtuname;
    }
}
