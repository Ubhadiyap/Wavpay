package com.hbyundu.shop.ui.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.base.Config;
import com.hbyundu.shop.rest.model.recommend.RecommendGoodsModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 2017/11/29.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    private List<RecommendGoodsModel> mGoods;

    public RecommendAdapter(Context context, List<RecommendGoodsModel> goods) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mGoods = goods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1) {
            view = mInflater.inflate(R.layout.item_goods_banner, parent, false);
            view.setOnClickListener(this);
            viewHolder = new RecommendBannerViewHolder(view);
        } else if (viewType == 2) {
            view = mInflater.inflate(R.layout.item_goods, parent, false);
            view.setOnClickListener(this);
            viewHolder = new RecommendGoodsViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        switch (getItemViewType(position)) {
            case 1:
                final RecommendBannerViewHolder bannerViewHolder = (RecommendBannerViewHolder) holder;
                bannerViewHolder.setData(mGoods.get(position));
                break;
            case 2:
                RecommendGoodsViewHolder goodsViewHolder = (RecommendGoodsViewHolder) holder;
                goodsViewHolder.setData(mGoods.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        RecommendGoodsModel item = mGoods.get(position);
        return item.isHot == 1 ? 1 : 2;
    }

    @Override
    public int getItemCount() {
        return mGoods != null ? mGoods.size() : 0;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class RecommendBannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public RecommendBannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_goods_banner_iv);
        }

        public void setData(RecommendGoodsModel model) {
            if (!TextUtils.isEmpty(model.hotImg)) {
                RequestOptions options = new RequestOptions().placeholder(imageView.getDrawable()).dontAnimate();
                Glide.with(mContext).load(Config.SERVER + model.hotImg).apply(options).into(imageView);
            }
        }
    }

    class RecommendGoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView allPriceTextView;
        TextView yfkTextView;

        public RecommendGoodsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_goods_iv);
            nameTextView = itemView.findViewById(R.id.item_goods_name_tv);
            allPriceTextView = itemView.findViewById(R.id.item_goods_all_price_tv);
            yfkTextView = itemView.findViewById(R.id.item_goods_all_yfk_tv);
        }

        public void setData(RecommendGoodsModel model) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
            Glide.with(mContext).load(Config.SERVER + model.img).apply(options).into(imageView);
            nameTextView.setText(model.goodsName);
            allPriceTextView.setText(mContext.getString(R.string.full_price) + " " + MoneyUtils.formatMoney(model.score, Locale.ENGLISH));
            yfkTextView.setText(MoneyUtils.formatMoney(model.score, Locale.ENGLISH));
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
