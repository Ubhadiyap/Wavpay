package com.hbyundu.shop.ui.goods.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.model.goods.GoodsDeliveryModel;

import java.util.ArrayList;

/**
 * Created by apple on 2018/1/9.
 */

public class GoodsDeliveryAdapter extends BaseAdapter {

    private ArrayList<GoodsDeliveryModel> mDeliveries;

    private Context mContext;

    private int mPosition;

    public GoodsDeliveryAdapter(Context context, ArrayList<GoodsDeliveryModel> deliveries, int position) {
        this.mContext = context;
        this.mDeliveries = deliveries;
        this.mPosition = position;
    }

    public void select(int position) {
        mPosition = position;
        notifyDataSetChanged();
    }

    public GoodsDeliveryModel getSelect() {
        return mDeliveries.get(mPosition);
    }

    @Override
    public int getCount() {
        return mDeliveries.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_delivery, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.item_goods_delivery_name_tv);
            viewHolder.address = view.findViewById(R.id.item_goods_delivery_address_tv);
            viewHolder.mobile = view.findViewById(R.id.item_goods_delivery_mobile_tv);
            viewHolder.checkBox = view.findViewById(R.id.item_goods_delivery_cb);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GoodsDeliveryModel deliveryModel = mDeliveries.get(i);
        viewHolder.name.setText(deliveryModel.deliveryName);
        viewHolder.address.setText(deliveryModel.deliveryAddr);
        viewHolder.mobile.setText(deliveryModel.deliveryTel);
        viewHolder.checkBox.setChecked(i == mPosition);

        return view;
    }

    class ViewHolder {
        TextView name;
        TextView address;
        TextView mobile;
        CheckBox checkBox;
    }
}
