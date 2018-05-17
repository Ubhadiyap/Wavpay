package com.hbyundu.shop.rest.model.repay;

import java.util.List;

/**
 * Created by apple on 2017/12/26.
 */

public class RepaySubDataModel {
    public long dealId;
    public String title;
    public double borrowAmount;
    public double rate;
    public int repayLimit;
    public boolean repayTimeType;
    public double repayAmount;
    public double remainAmount;
    public List<RepaySubItemModel> list;
}
