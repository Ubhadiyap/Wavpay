package com.wavpayment.wavpay.service.common;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wavpayment.wavpay.R;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class CommonItemAdapter extends BaseQuickAdapter<CommonItemEntity,BaseViewHolder>{
    //设置图片加载策略
    protected static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    public CommonItemAdapter(@Nullable List<CommonItemEntity> data) {
        super(R.layout.item_common_item_more,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonItemEntity item) {
        Glide.with(mContext)
                .load(item.getImageId())
                .apply(RECYCLER_OPTIONS)
                .into((ImageView) helper.getView(R.id.iv_img));
        helper.setText(R.id.tv_title,item.getItemName());
    }
}
