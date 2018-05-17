package com.wavpayment.wavpay.ui.main.Index;

import com.wavpayment.wavpay.service.common.CommonEntity;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;


public class IndexDataConverter extends DataConverter<CommonEntity> {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final ArrayList<CommonEntity> DATA = (ArrayList<CommonEntity>) getData();
        for (int i = 0; i < DATA.size(); i++) {
            final CommonEntity ITEM = DATA.get(i);
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE,ITEM.getItemType())
                    .setField(MultipleFields.ITEM,ITEM.getItem())
                    .setField(MultipleFields.BANNERS,ITEM.getBanner())
                    .setField(MultipleFields.TITLE,ITEM.getTitle())
                    .setField(MultipleFields.SPAN_SIZE,4)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
