package com.wavpayment.wavpay.ui.left.information;


import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2017/11/23.
 */

public class InfoDataConverter extends DataConverter<InfoEntity> {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final ArrayList<InfoEntity> entities = (ArrayList<InfoEntity>) getData();
        for (InfoEntity it : entities) {
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, it.getType())
                    .setField(MultipleFields.TITLE, it.getName())
                    .setField(MultipleFields.TEXT, it.getContent())
                    .setField(MultipleFields.ITEM,it.getDelegate())
                    .setField(MultipleFields.SPAN_SIZE, 4)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
