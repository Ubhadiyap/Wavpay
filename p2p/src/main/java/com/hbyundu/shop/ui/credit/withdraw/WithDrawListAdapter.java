package com.hbyundu.shop.ui.credit.withdraw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.model.repay.RepayItemModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 2018/3/30.
 */

public class WithDrawListAdapter extends BaseAdapter {

    private List<RepayItemModel> mData;
    private Context mContext;
    private OnWithDrawClickListener mListener;
    private SimpleDateFormat mSdf;

    public WithDrawListAdapter(Context context, List<RepayItemModel> data) {
        this.mContext = context;
        this.mData = data;
        mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public void setListener(OnWithDrawClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_withdraw_list, viewGroup, false);
            viewHolder.moneyTextView = view.findViewById(R.id.item_withdraw_list_money_tv);
            viewHolder.actionTextView = view.findViewById(R.id.item_withdraw_list_action_tv);
            viewHolder.numberTextView = view.findViewById(R.id.item_withdraw_list_number_tv);
            viewHolder.timeTextView = view.findViewById(R.id.item_withdraw_list_date_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        RepayItemModel itemModel = mData.get(i);
        viewHolder.moneyTextView.setText(MoneyUtils.formatMoney(itemModel.avaliableMoney, Locale.ENGLISH));
        viewHolder.numberTextView.setText(itemModel.orId);
        viewHolder.timeTextView.setText(mSdf.format(new Date(itemModel.createTime * 1000)));
        viewHolder.actionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onWithDrawClick(i);
                }
            }
        });

        return view;
    }

    class ViewHolder {
        public TextView moneyTextView;
        public TextView actionTextView;
        public TextView numberTextView;
        public TextView timeTextView;
    }

    public interface OnWithDrawClickListener {
        void onWithDrawClick(int position);
    }
}
