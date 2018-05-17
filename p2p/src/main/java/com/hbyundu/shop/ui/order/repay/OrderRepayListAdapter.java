package com.hbyundu.shop.ui.order.repay;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.ui.credit.repay.Uc_Quick_RefundActLoan_ListModel;
import com.hbyundu.shop.vendor.widget.SDImageCheckBox;

import java.util.ArrayList;
import java.util.List;

public class OrderRepayListAdapter extends BaseAdapter {

    private List<Uc_Quick_RefundActLoan_ListModel> mListModel = null;
    private Activity mActivity = null;
    private RepayBorrowRepayListAdapterListener mListener = null;
    private int mFirstClickAblePos = -1;

    public OrderRepayListAdapter(List<Uc_Quick_RefundActLoan_ListModel> listModel, Activity activity, RepayBorrowRepayListAdapterListener listener) {
        this.mListModel = listModel;
        this.mActivity = activity;
        this.mListener = listener;
    }

    public void updateListViewData(List<Uc_Quick_RefundActLoan_ListModel> listModel) {
        if (listModel != null && listModel.size() > 0) {
            this.mListModel = listModel;
            for (int i = 0; i < mListModel.size(); i++) {
                if (mListModel.get(i).isCanSelect()) {
                    mFirstClickAblePos = i;
                    break;
                }
            }
            this.notifyDataSetChanged();
        }
    }

    public List<Uc_Quick_RefundActLoan_ListModel> getSelectItems() {
        List<Uc_Quick_RefundActLoan_ListModel> listModel = new ArrayList<Uc_Quick_RefundActLoan_ListModel>();
        if (mListModel != null && mListModel.size() > 0) {
            for (Uc_Quick_RefundActLoan_ListModel model : mListModel) {
                if (model.isCanSelect() && model.isSelect()) {
                    listModel.add(model);
                }
            }
        }
        return listModel;
    }

    @Override
    public int getCount() {
        if (mListModel != null) {
            return mListModel.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
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
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_repay_borrow_list_repay, null);
            holder.mTxtMonthNeedAllRepayMoney = (TextView) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_txt_month_need_all_repay_money);
            holder.mTxtRepayDay = (TextView) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_txt_repay_day);
            holder.mTxtMonthHasRepayMoney = (TextView) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_txt_month_has_repay_money);
            holder.mTxtMonthRepayMoney = (TextView) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_txt_month_repay_money);
            holder.mTxtMonthManageMoney = (TextView) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_txt_month_manage_money);
            holder.mTxtMonthOverdueFees = (TextView) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_txt_month_overdue_fees);
            holder.mCheckSelect = (SDImageCheckBox) convertView.findViewById(R.id.item_lsv_repay_borrow_list_repay_check_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Uc_Quick_RefundActLoan_ListModel model = (Uc_Quick_RefundActLoan_ListModel) getItem(position);
        if (model != null) {
            // 根据状态判断是否显示选择按钮
            if (model.isCanSelect()) {
                holder.mCheckSelect.setVisibility(View.VISIBLE);
            } else {
                holder.mCheckSelect.setVisibility(View.INVISIBLE);
            }
            holder.mCheckSelect.setCheckState(model.isSelect());
            holder.mCheckSelect.setOnClickListener(new ItemCheck_OnClickListener(position, holder));

            //待还金额
            if (model.getMonth_need_all_repay_money_format() != null) {
                holder.mTxtMonthNeedAllRepayMoney.setText(model.getMonth_need_all_repay_money_format());
            } else {
                holder.mTxtMonthNeedAllRepayMoney.setText(R.string.not_found);
            }

            //还款日
            if (model.getRepay_day_format() != null) {
                holder.mTxtRepayDay.setText(model.getRepay_day_format());
            } else {
                holder.mTxtRepayDay.setText(R.string.not_found);
            }

            //已还金额
            if (model.getMonth_has_repay_money_all_format() != null) {
                holder.mTxtMonthHasRepayMoney.setText(model.getMonth_has_repay_money_all_format());
            } else {
                holder.mTxtMonthHasRepayMoney.setText(R.string.not_found);
            }

            //管理费
            if (model.getMonth_manage_money_format() != null) {
                holder.mTxtMonthManageMoney.setText(model.getMonth_manage_money_format());
            } else {
                holder.mTxtMonthManageMoney.setText(R.string.not_found);
            }

            //待还本息
            if (model.getMonth_repay_money_format() != null) {
                holder.mTxtMonthRepayMoney.setText(model.getMonth_repay_money_format());
            } else {
                holder.mTxtMonthRepayMoney.setText(R.string.not_found);
            }

            //逾期费
            if (model.getImpose_money_format() != null) {
                holder.mTxtMonthOverdueFees.setText(model.getImpose_money_format());
            } else {
                holder.mTxtMonthOverdueFees.setText(R.string.not_found);
            }

        }
        return convertView;
    }

    class ItemCheck_OnClickListener implements OnClickListener {
        private int nPosition = -1;
        private ViewHolder nHolder = null;

        public ItemCheck_OnClickListener(int nPosition, ViewHolder holder) {
            super();
            this.nPosition = nPosition;
            this.nHolder = holder;
        }

        @Override
        public void onClick(View v) {

            if (nPosition != -1 && mListModel != null && mListModel.size() > 0 && nPosition < mListModel.size() && mFirstClickAblePos != -1) {
                if (nPosition == mFirstClickAblePos) {
                    if ((nPosition + 1) < mListModel.size()) {
                        if (!mListModel.get(nPosition + 1).isSelect()) {
                            doNormalToggle(nPosition, nHolder);
                        }
                    } else if ((nPosition + 1) == mListModel.size()) {
                        doNormalToggle(nPosition, nHolder);
                    }
                } else if (nPosition > mFirstClickAblePos) {
                    if (mListModel.get(nPosition - 1).isSelect()) {
                        if ((nPosition + 1) < mListModel.size()) {
                            if (!mListModel.get(nPosition + 1).isSelect()) {
                                doNormalToggle(nPosition, nHolder);
                            }
                        } else if ((nPosition + 1) == mListModel.size()) {
                            doNormalToggle(nPosition, nHolder);
                        }
                    }
                }
            }
        }
    }

    private void doNormalToggle(int pos, ViewHolder holder) {
        mListModel.get(pos).setSelect(!mListModel.get(pos).isSelect());
        holder.mCheckSelect.setCheckState(mListModel.get(pos).isSelect());
        if (mListener != null) {
            if (mListModel != null && mListModel.size() > 0) {
                StringBuilder builderIds = new StringBuilder("");
                float totalMoney = 0;
                for (int i = 0; i < mListModel.size(); i++) {
                    Uc_Quick_RefundActLoan_ListModel model = mListModel.get(i);
                    if (model.isCanSelect() && model.isSelect()) {
                        builderIds.append(i + ",");
                        try {
                            totalMoney += Float.parseFloat(model.getMonth_need_all_repay_money());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                String ids = builderIds.toString();
                if (ids.length() > 0) {
                    ids = ids.substring(0, ids.length() - 1);
                }
                mListener.onCheckedChange(ids, totalMoney);
            }

        }
    }

    static class ViewHolder {
        public TextView mTxtMonthNeedAllRepayMoney = null;
        public TextView mTxtRepayDay = null;
        public TextView mTxtMonthHasRepayMoney = null;
        public TextView mTxtMonthRepayMoney = null;
        public TextView mTxtMonthManageMoney = null;
        public TextView mTxtMonthOverdueFees = null;
        public SDImageCheckBox mCheckSelect = null;
    }

    public interface RepayBorrowRepayListAdapterListener {
        public void onCheckedChange(String ids, float totalMoneyNeedpay);
    }

}
