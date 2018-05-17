package com.hbyundu.shop.ui.order.list;

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
import com.hbyundu.shop.rest.model.order.OrderItemModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 2017/11/29.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    private List<OrderItemModel> mOrders;

    public OrderListAdapter(Context context, List<OrderItemModel> orders) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mOrders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_order, parent, false);
        view.setOnClickListener(this);
        RecyclerView.ViewHolder viewHolder = new OrderViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        OrderViewHolder viewHolder = (OrderViewHolder) holder;
        viewHolder.setData(mOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrders != null ? mOrders.size() : 0;
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

    class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView propertyTextView;
        TextView numberTextView;
        TextView amountTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_order_iv);
            nameTextView = itemView.findViewById(R.id.item_order_name_tv);
            propertyTextView = itemView.findViewById(R.id.item_order_property_tv);
            numberTextView = itemView.findViewById(R.id.item_order_number_tv);
            amountTextView = itemView.findViewById(R.id.item_order_amount_tv);
        }

        public void setData(OrderItemModel model) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_goods_default_image).dontAnimate();
            Glide.with(mContext).load(Config.SERVER + model.img).apply(options).into(imageView);
            nameTextView.setText(model.goodsName);
            numberTextView.setText(String.format(mContext.getString(R.string.goods_number_format), model.goodsCount));
            amountTextView.setText(MoneyUtils.formatMoney(model.sumPrice, Locale.ENGLISH));
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
