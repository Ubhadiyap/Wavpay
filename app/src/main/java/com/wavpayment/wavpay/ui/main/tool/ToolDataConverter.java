package com.wavpayment.wavpay.ui.main.tool;

import com.wavpayment.wavpay.service.common.CommonEntity;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class ToolDataConverter extends DataConverter<CommonEntity> {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final ArrayList<CommonEntity> DATA = (ArrayList<CommonEntity>) getData();
        for (int i = 0; i < DATA.size(); i++) {
            final CommonEntity ITEM = DATA.get(i);
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE,ITEM.getItemType())
                    .setField(MultipleFields.ITEM,ITEM.getItem())
                    .setField(MultipleFields.TITLE,ITEM.getTitle())
                    .setField(MultipleFields.SPAN_SIZE,4)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
