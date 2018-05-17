package com.wavpayment.wavpay.ui.left.information;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.NO_TITLE;
import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


public class InfoHandler {
    private final RecyclerView RECYCLERVIEW;
    private final DataConverter CONVERTER;
    private MultipleRecyclerAdapter mAdapter;

    private InfoHandler(RecyclerView recyclerview, DataConverter converter) {
        RECYCLERVIEW = recyclerview;
        CONVERTER = converter;
    }

    public static InfoHandler create(RecyclerView recyclerView, InfoDataConverter dataConverter) {
        return new InfoHandler(recyclerView, dataConverter);
    }

    public void refresh(SpongeDelegate delegate) {
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        final String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
        final String photo = baseUrl.substring(0, baseUrl.length() - 1) + data.getString("headImg");
        final String nick = data.getString("nickName");
        final String sign = data.getString("sign");
        final String phone = data.getString("account");
        final int i = data.getIntValue("gender");
        String gender;
        if (i == 1) {
            gender = "Male";
        } else if (i == 2) {
            gender = "Female";
        } else {
            gender = "Others";
        }
        final List<InfoEntity> dArray = new ArrayList<>();
        dArray.add(new InfoEntity(CommonItemType.INFO_PHOTO, "Profile Photo", photo));
        dArray.add(new InfoEntity(CommonItemType.INFO_TEXT, "Name", nick, new NickInfoDelegate()));
        dArray.add(new InfoEntity(CommonItemType.INFO_TEXT, "Status", sign, new StatusDelegate()));
        dArray.add(new InfoEntity(CommonItemType.INFO_TEXT, "Gender", gender, new GenderDelegate()));
        dArray.add(new InfoEntity(CommonItemType.VIEW));
        dArray.add(new InfoEntity(CommonItemType.INFO_TEXT, "ID", phone));
        dArray.add(new InfoEntity(CommonItemType.INFO_QR, "My QR Code", "", new QRInfoDelegate()));
        mAdapter = InfoAdapter.create(CONVERTER.setData(dArray));
        RECYCLERVIEW.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
            final SpongeDelegate item = entity.getField(MultipleFields.ITEM);
            if (item != null) {
                delegate.getSupportDelegate().start(item);
                if (position == 1 || position == 2 || position == 3) {
                    final TextView content = view.findViewById(R.id.tv_content);
                    ((BaseInfoDelegate) item).setListener(args -> content.setText(String.valueOf(args)));
                }
            }
            if (position == 0) {
                final Activity activity = Sponge.getConfiguration(ConfigKeys.ACTIVITY);
                final RxDialogChooseImage dialog = new RxDialogChooseImage(activity, NO_TITLE);
                dialog.show();
            }
        });
    }
}
