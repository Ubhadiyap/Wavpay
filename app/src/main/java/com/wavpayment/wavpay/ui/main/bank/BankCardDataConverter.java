package com.wavpayment.wavpay.ui.main.bank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * 银行卡数据
 * Created by Administrator on 2017/12/27.
 */

class BankCardDataConverter extends DataConverter {


    @Override
    public ArrayList<MultipleItemEntity> convert() {
        if (!getJsonData().isEmpty()) {
            final JSONArray data = JSON.parseObject(getJsonData()).getJSONArray("bankList");
            final int size = data.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final JSONObject item = data.getJSONObject(i);
                    final int accountType = item.getIntValue("accountType");
                    final String balance = item.getString("balance");
                    final String bankCode = item.getString("bankCode");
                    final String cardNumber = item.getString("cardNumber");
                    final String realName = item.getString("realName");
                    final int id = item.getIntValue("id");
                    final int userId = item.getIntValue("userId");
                    MultipleItemEntity entity = MultipleItemEntity.builder()
                            .setField(MultipleFields.ITEM_TYPE, BankType.CARD_LIST)
                            .setField(MultipleFields.SPAN_SIZE, 4)
                            .setField(BankFields.accountType, accountType)
                            .setField(BankFields.balance, balance)
                            .setField(BankFields.bankCode, bankCode)
                            .setField(BankFields.cardNumber, cardNumber)
                            .setField(BankFields.realName, realName)
                            .setField(BankFields.ID, id)
                            .setField(BankFields.userId, userId)
                            .setField(MultipleFields.TAG, false)
                            .build();
                    ENTITIES.add(entity);
                }
            } else {
                MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setField(MultipleFields.ITEM_TYPE, BankType.CARD_LIST)
                        .setField(MultipleFields.SPAN_SIZE, 4)
                        .setField(BankFields.accountType, "")
                        .setField(BankFields.balance, "")
                        .setField(BankFields.bankCode, "Please Add Card")
                        .setField(BankFields.cardNumber, "")
                        .setField(BankFields.realName, "")
                        .setField(BankFields.ID, "")
                        .setField(BankFields.userId, "")
                        .setField(MultipleFields.TAG, true)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
