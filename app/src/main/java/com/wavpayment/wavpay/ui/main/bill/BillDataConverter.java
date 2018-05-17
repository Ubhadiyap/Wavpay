package com.wavpayment.wavpay.ui.main.bill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 *  账单数据
 * Created by Administrator on 2017/12/29.
 */

public class BillDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        if (!getJsonData().isEmpty()) {
            final JSONObject page = JSON.parseObject(getJsonData()).getJSONObject("page");
            final JSONArray array = page.getJSONArray("content");
            if (array != null) {
                final int size = array.size();
                for (int i = 0; i < size; i++) {
                    final JSONObject item = array.getJSONObject(i);
                    final int id = item.getIntValue("id");
                    final int payAccountType = item.getIntValue("payAccountType");
                    final String collectAccount = item.getString("collectAccount");
                    final String collectAccountType = item.getString("collectAccountType");
                    final double payAmount = item.getDoubleValue("payAmount");
                    final String payAccount = item.getString("payAccount");
                    final double collectAmount = item.getDoubleValue("collectAmount");
                    final String payBeforeBalance = item.getString("payBeforeBalance");
                    final String collectBeforeBalance = item.getString("collectBeforeBalance");
                    final String goodsInfo = item.getString("goodsInfo");
                    final String payMemo = item.getString("payMemo");
                    final int tradeStatus = item.getIntValue("tradeStatus");
                    final int tradeType = item.getInteger("tradeType");
                    final String payWays = item.getString("payWays");
                    final String collectWays = item.getString("collectWays");
                    final String bankCode = item.getString("bankCode");
                    final String cardName = item.getString("cardName");
                    final String billTypeId = item.getString("billTypeId");
                    final String discount = item.getString("discount");
                    final String mchOrderId = item.getString("mchOrderId");
                    final String point = item.getString("point");
                    final String refundWays = item.getString("refundWays");
                    final String refenceId = item.getString("refenceId");
                    final String progress = item.getString("progress");
                    final String topupAccount = item.getString("topupAccount");
                    final String facePrice = item.getString("facePrice");
                    final String topupType = item.getString("topupType");
                    final String topupRefenceId = item.getString("topupRefenceId");
                    final String serviceFee = item.getString("serviceFee");
                    final long createTime = item.getLongValue("createTime");
                    final long tradeTime = item.getLongValue("tradeTime");
                    final String orderId = item.getString("orderId");
                    final String collUserName = item.getString("collUserName");
                    final String payUserName = item.getString("payUserName");
                    final MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setField(MultipleFields.ITEM_TYPE, BillItemType.BILL_ITEM)
                            .setField(MultipleFields.SPAN_SIZE, 4)
                            .setField(BillFields.id, id)
                            .setField(BillFields.payAccountType, payAccountType)
                            .setField(BillFields.collectAccount, collectAccount)
                            .setField(BillFields.collectAccountType, collectAccountType)
                            .setField(BillFields.payAmount, payAmount)
                            .setField(BillFields.payAccount, payAccount)
                            .setField(BillFields.collectAmount, collectAmount)
                            .setField(BillFields.payBeforeBalance, payBeforeBalance)
                            .setField(BillFields.collectBeforeBalance, collectBeforeBalance)
                            .setField(BillFields.goodsInfo, goodsInfo)
                            .setField(BillFields.payMemo, payMemo)
                            .setField(BillFields.tradeStatus, tradeStatus)
                            .setField(BillFields.tradeType, tradeType)
                            .setField(BillFields.payWays, payWays)
                            .setField(BillFields.collectWays, collectWays)
                            .setField(BillFields.bankCode, bankCode)
                            .setField(BillFields.cardName, cardName)
                            .setField(BillFields.billTypeId, billTypeId)
                            .setField(BillFields.discount, discount)
                            .setField(BillFields.mchOrderId, mchOrderId)
                            .setField(BillFields.point, point)
                            .setField(BillFields.refundWays, refundWays)
                            .setField(BillFields.refenceId, refenceId)
                            .setField(BillFields.progress, progress)
                            .setField(BillFields.topupAccount, topupAccount)
                            .setField(BillFields.facePrice, facePrice)
                            .setField(BillFields.topupType, topupType)
                            .setField(BillFields.topupRefenceId, topupRefenceId)
                            .setField(BillFields.serviceFee, serviceFee)
                            .setField(BillFields.createTime, createTime)
                            .setField(BillFields.tradeTime, tradeTime)
                            .setField(BillFields.orderId, orderId)
                            .setField(BillFields.payUserName,payUserName)
                            .setField(BillFields.collUserName,collUserName)
                            .build();
                    ENTITIES.add(entity);
                }
            }
        }
        return ENTITIES;
    }
}
