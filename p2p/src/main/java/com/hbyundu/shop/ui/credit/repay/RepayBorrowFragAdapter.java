package com.hbyundu.shop.ui.credit.repay;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbyundu.shop.R;

import java.util.List;

/**
 * Title:个人中心偿还贷款还款列表
 *
 * @author: yhz CreateTime：2014-6-20 下午5:38:14
 */
public class RepayBorrowFragAdapter extends BaseAdapter {
    private Fragment mFragment = null;
    private List<Uc_RefundActItemModel> mListModel = null;

    public RepayBorrowFragAdapter(Fragment fragment, List<Uc_RefundActItemModel> list) {
        this.mFragment = fragment;
        this.mListModel = list;
    }

    public void updateListViewData(List<Uc_RefundActItemModel> listModel) {
        if (listModel != null && listModel.size() > 0) {
            this.mListModel = listModel;
        } else {
            this.mListModel.clear();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mListModel != null) {
            return mListModel.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (mListModel != null && position < mListModel.size()) {
            return mListModel.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mFragment.getActivity().getLayoutInflater().inflate(R.layout.uc_frag_repay_borrow_listitem, null);
            holder.ucFraRepBorLisItem_tv_subname = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_subname);
            holder.ucFraRepBorLisItem_tv_borrow_amount_format = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_borrow_amount_format);
            holder.ucFraRepBorLisItem_tv_rate = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_rate);
            holder.ucFraRepBorLisItem_tv_repay_time = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_repay_time);
            holder.ucFraRepBorLisItem_tv_true_month_repay_money = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_true_month_repay_money);
            holder.ucFraRepBorLisItem_tv_need_money = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_need_money);
            holder.ucFraRepBorLisItem_tv_next_repay_time = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_next_repay_time);
            holder.ucFraRepBorLisItem_tv_repayment = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_repayment);
            holder.ucFraRepBorLisItem_tv_prepayment = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_prepayment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Uc_RefundActItemModel model = (Uc_RefundActItemModel) getItem(position);
        if (model != null) {
            if (model.getSub_name() != null) {
                holder.ucFraRepBorLisItem_tv_subname.setText(model.getSub_name());
            }
            if (model.getBorrow_amount_format() != null) {
                holder.ucFraRepBorLisItem_tv_borrow_amount_format.setText(model.getBorrow_amount_format());
            }
            if (model.getRate() != null) {
                holder.ucFraRepBorLisItem_tv_rate.setText(model.getRate() + "%");
            }
            if (model.getRepay_time_type() != null && model.getRepay_time() != null) {

                switch (Integer.valueOf(model.getRepay_time_type())) {
                    case 0:
                        holder.ucFraRepBorLisItem_tv_repay_time.setText(String.format(mFragment.getString(R.string.repay_term_days_format), model.getRepay_time()));
                        break;
                    default:
                        holder.ucFraRepBorLisItem_tv_repay_time.setText(String.format(mFragment.getString(R.string.repay_term_months_format), model.getRepay_time()));
                        break;
                }
            }
            if (model.getTrue_month_repay_money() != null) {
                holder.ucFraRepBorLisItem_tv_true_month_repay_money.setText(model.getTrue_month_repay_money());
            }
            if (model.getNeed_money() != null) {
                holder.ucFraRepBorLisItem_tv_need_money.setText(model.getNeed_money());
            }
            if (model.getNext_repay_time_format() != null) {
                holder.ucFraRepBorLisItem_tv_next_repay_time.setText(model.getNext_repay_time_format());
            }
        }

        holder.ucFraRepBorLisItem_tv_repayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListModel.get(position).getId() != null) {
                    Intent intent = new Intent(mFragment.getActivity(), RepayBorrowRepayActivity.class);
                    intent.putExtra(RepayBorrowRepayActivity.EXTRA_DEAL_ID, mListModel.get(position).getId());
                    mFragment.startActivity(intent);
                }
            }
        });

        holder.ucFraRepBorLisItem_tv_prepayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListModel.get(position).getId() != null) {
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView ucFraRepBorLisItem_tv_subname = null;
        TextView ucFraRepBorLisItem_tv_repayment = null;
        TextView ucFraRepBorLisItem_tv_prepayment = null;
        TextView ucFraRepBorLisItem_tv_borrow_amount_format = null;
        TextView ucFraRepBorLisItem_tv_rate = null;
        TextView ucFraRepBorLisItem_tv_repay_time = null;
        TextView ucFraRepBorLisItem_tv_true_month_repay_money = null;
        TextView ucFraRepBorLisItem_tv_need_money = null;
        TextView ucFraRepBorLisItem_tv_next_repay_time = null;
    }

}
