package com.wavpayment.wavpay.ui.left.information;

import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;
import com.wavpayment.wavpay.widgte.recycler.MultipleViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class InfoAdapter extends MultipleRecyclerAdapter {


    private InfoAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(CommonItemType.VIEW, R.layout.item_common_view);
        addItemType(CommonItemType.INFO_PHOTO, R.layout.item_info_photo);
        addItemType(CommonItemType.INFO_QR, R.layout.item_info_qr);
        addItemType(CommonItemType.INFO_TEXT, R.layout.item_info_text);
    }

    public static InfoAdapter create(List<MultipleItemEntity> data) {
        return new InfoAdapter(data);
    }

    public static InfoAdapter create(DataConverter converter) {
        return new InfoAdapter(converter.convert());
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {
        final String content = item.getField(MultipleFields.TEXT);
        final String title = item.getField(MultipleFields.TITLE);
        switch (helper.getItemViewType()) {
            case CommonItemType.INFO_PHOTO:
                if (!TextUtils.isEmpty(content)) {
                    Glide.with(mContext)
                            .load(content)
                            .apply(RECYCLER_OPTIONS)
                            .into((CircleImageView) helper.getView(R.id.cv_photo));
                }
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_CROP, args -> {
                                    Glide.with(mContext)
                                            .load(args)
                                            .apply(RECYCLER_OPTIONS)
                                            .into((CircleImageView) helper.getView(R.id.cv_photo));
                                    CommonHandler.getInstance().update(args.toString());
                                }

                        );
                helper.setText(R.id.tv_title, title);
                break;
            case CommonItemType.INFO_QR:
                helper.setText(R.id.tv_title, title);
                break;
            case CommonItemType.INFO_TEXT:
                helper.setText(R.id.tv_title, title);
                helper.setText(R.id.tv_content, content);
                if (title.equals("ID")) {
                    helper.getView(R.id.iv_arr).setVisibility(View.INVISIBLE);
                }
                break;
            default:
                break;

        }
    }
}
