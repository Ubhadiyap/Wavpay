package com.hbyundu.shop.rest.model.order;

import java.util.List;

/**
 * Created by apple on 2017/12/21.
 */

public class OrderItemModel {
    public long orderId;
    public String goodsName;
    public String goodName;
    public int goodsCount;
    public double sumPrice;
    public String deliveryAddr;
    public String deliveryTel;
    public String deliveryName;
    public String memo;
    public String date;
    public List<String> attr;
    public String img;
    public String orderNo;
    public int orderState;
}
