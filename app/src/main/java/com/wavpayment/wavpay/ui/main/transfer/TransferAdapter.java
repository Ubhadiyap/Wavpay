package com.wavpayment.wavpay.ui.main.transfer;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.widgte.recycler.MultipleFields;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class TransferAdapter extends BaseQuickAdapter<MultipleItemEntity, BaseViewHolder> {
    //设置图片加载策略
    protected static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    TransferAdapter(@Nullable List<MultipleItemEntity> data) {
        super(R.layout.item_transfer, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItemEntity item) {
        final int id = item.getField(MultipleFields.ID);
        if (id!=-1) {
            String url = item.getField(MultipleFields.TAG);
            url = Sponge.getConfiguration(ConfigKeys.API_HOST) + url.substring(1, url.length());
            Glide.with(mContext)
                    .load(url)
                    .apply(RECYCLER_OPTIONS)
                    .into((CircleImageView) helper.getView(R.id.cv_photo));
            helper.setText(R.id.tv_title,mContext.getString(R.string.tran_to_wp));
        }else {
            helper.setText(R.id.tv_title,mContext.getString(R.string.tran_to_card));

        }
        final String rename = item.getField(MultipleFields.IMAGE_URL);
        final String phone = item.getField(MultipleFields.TITLE);
        helper.setText(R.id.tv_phone,rename+" "+ phone);
    }
}
