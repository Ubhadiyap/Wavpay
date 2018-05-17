package com.hbyundu.shop.ui.goods.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.base.Config;
import com.hbyundu.shop.rest.model.goods.GoodsListModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 2017/11/29.
 */

public class GoodsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    private List<GoodsListModel> mGoods;

    public GoodsListAdapter(Context context, List<GoodsListModel> goods) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mGoods = goods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_goods, parent, false);
        view.setOnClickListener(this);
        RecyclerView.ViewHolder viewHolder = new GoodsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        GoodsViewHolder viewHolder = (GoodsViewHolder)holder;
        viewHolder.setData(mGoods.get(position));
    }

    @Override
    public int getItemCount() {
        return mGoods != null ? mGoods.size() : 0;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView allPriceTextView;
        TextView yfkTextView;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_goods_iv);
            nameTextView = itemView.findViewById(R.id.item_goods_name_tv);
            allPriceTextView = itemView.findViewById(R.id.item_goods_all_price_tv);
            yfkTextView = itemView.findViewById(R.id.item_goods_all_yfk_tv);
        }

        public void setData(GoodsListModel model) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
            Glide.with(itemView.getContext()).load(Config.SERVER + model.img).apply(options).into(imageView);
            nameTextView.setText(model.name);
            allPriceTextView.setText(itemView.getContext().getString(R.string.full_price) + " " + MoneyUtils.formatMoney(model.score, Locale.ENGLISH));
            yfkTextView.setText(MoneyUtils.formatMoney(model.score, Locale.ENGLISH));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
