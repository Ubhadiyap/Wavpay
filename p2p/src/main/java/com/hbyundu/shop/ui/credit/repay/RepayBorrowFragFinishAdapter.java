package com.hbyundu.shop.ui.credit.repay;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbyundu.shop.R;

import java.util.List;

/**
 * Title:个人中心偿还贷款
 *
 * @author: yhz CreateTime：2014-6-20 下午5:38:14
 */
public class RepayBorrowFragFinishAdapter extends BaseAdapter {
    private Activity mActivity = null;
    private List<Uc_RefundActItemModel> mListModel = null;

    public RepayBorrowFragFinishAdapter(Activity activity, List<Uc_RefundActItemModel> list) {
        this.mActivity = activity;
        this.mListModel = list;
    }

    public void updateListViewData(List<Uc_RefundActItemModel> listModel) {
        if (listModel != null && listModel.size() > 0) {
            this.mListModel = listModel;
            this.notifyDataSetChanged();
        }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.uc_frag_repay_borrow_finish_listitem, null);
            holder.subname = (TextView) convertView.findViewById(R.id.ucFraBowFinList_tv_subname);
            holder.publis_time_format = (TextView) convertView.findViewById(R.id.ucFraBowFinList_tv_publis_time_format);
            holder.rate = (TextView) convertView.findViewById(R.id.ucFraREpBorFinList_tv_rate);
            holder.repay_time = (TextView) convertView.findViewById(R.id.ucFraREpBorFinList_tv_repay_time);
            holder.borrow_amount_format = (TextView) convertView.findViewById(R.id.ucFraREpBorFinList_tv_borrow_amount_format);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Uc_RefundActItemModel model = (Uc_RefundActItemModel) getItem(position);
        if (model != null) {
            if (model.getSub_name() != null) {
                holder.subname.setText(model.getSub_name());
            }
            if (model.getPublis_time_format() != null) {
                holder.publis_time_format.setText(model.getPublis_time_format());
            }
            if (model.getRate() != null) {
                holder.rate.setText(model.getRate() + "%");
            }
            if (model.getRepay_time() != null && model.getRepay_time_type() != null) {
                switch (Integer.valueOf(model.getRepay_time_type())) {
                    case 0:
                        holder.repay_time.setText(model.getRepay_time() + " " + mActivity.getString(R.string.days));
                        break;
                    default:
                        holder.repay_time.setText(model.getRepay_time() + " " + mActivity.getString(R.string.months));
                        break;
                }
            }
            if (model.getBorrow_amount_format() != null) {
                holder.borrow_amount_format.setText(model.getBorrow_amount_format());
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView subname = null;
        TextView publis_time_format = null;
        TextView rate = null;
        TextView repay_time = null;
        TextView borrow_amount_format = null;
    }

}
