package com.hbyundu.shop.ui.credit.invest.confirm;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.R.color;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.invest.InvestProjectAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestResultModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.invest.list.DealsActItemModel;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.SDToast;
import com.hbyundu.shop.vendor.util.SDUIUtil;
import com.hbyundu.shop.vendor.util.SDViewUtil;
import com.hbyundu.shop.vendor.widget.SDSimpleProjectDetailItemView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

import java.util.Locale;

/**
 * 确认投标界面
 *
 * @author js02
 */
public class InvestConfirmBidActivity extends BaseActivity implements OnClickListener {
    public static final String EXTRA_DEALS_ITEM_DETAIL_MODEL = "extra_deals_item_detail_model";

//    @ViewInject(id = R.id.act_confirm_bid_tv_title)
    private TextView mTvTitle = null;

//    @ViewInject(id = R.id.act_confirm_bid_sdview_borrow_amount)
    private SDSimpleProjectDetailItemView mSdviewBorrowAmount = null;

//    @ViewInject(id = R.id.act_confirm_bid_sdview_progress)
    private SDSimpleProjectDetailItemView mSdviewProgress = null;

//    @ViewInject(id = R.id.act_confirm_bid_sdview_rate)
    private SDSimpleProjectDetailItemView mSdviewRate = null;

//    @ViewInject(id = R.id.act_confirm_bid_sdview_repay_time)
    private SDSimpleProjectDetailItemView mSdviewRepayTime = null;

//    @ViewInject(id = R.id.act_confirm_bid_sdview_loan_type)
    private SDSimpleProjectDetailItemView mSdviewLoanType = null;

//    @ViewInject(id = R.id.act_confirm_bid_sdview_need_money)
    private SDSimpleProjectDetailItemView mSdviewNeedMoney = null;

//    @ViewInject(id = R.id.act_confirm_bid_txt_money_can_use)
    private TextView mTxtMoneyCanUse = null;

//    @ViewInject(id = R.id.act_confirm_bid_txt_i_want_recharge)
    private TextView mTxtIWantRecharge = null;

//    @ViewInject(id = R.id.act_confirm_bid_edt_invest_money_amount)
    private EditText mEdtInvestMoneyAmount = null;

//    @ViewInject(id = R.id.act_confirm_bid_edt_pay_password)
    private EditText mEdtPayPassword = null;

//    @ViewInject(id = R.id.act_confirm_bid_btn_confirm_invest)
    private Button mBtnConfirmInvest = null;

    private DealsActItemModel mModel = null;

    private String mStrBidMoney = null;
    private String mStrBidPayPassword = null;
    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_confirm_bid);
//        SDIoc.injectView(this);

        mModel = (DealsActItemModel) getIntent().getSerializableExtra(EXTRA_DEALS_ITEM_DETAIL_MODEL);

        initToolbar();
        initViews();
        initItems();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.confirm_tender);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mTvTitle = (TextView) findViewById(R.id.act_confirm_bid_tv_title);
        mSdviewBorrowAmount = (SDSimpleProjectDetailItemView) findViewById(R.id.act_confirm_bid_sdview_borrow_amount);
        mSdviewProgress = (SDSimpleProjectDetailItemView) findViewById(R.id.act_confirm_bid_sdview_progress);
        mSdviewRate = (SDSimpleProjectDetailItemView) findViewById(R.id.act_confirm_bid_sdview_rate);
        mSdviewRepayTime = (SDSimpleProjectDetailItemView) findViewById(R.id.act_confirm_bid_sdview_repay_time);
        mSdviewLoanType = (SDSimpleProjectDetailItemView) findViewById(R.id.act_confirm_bid_sdview_loan_type);
        mSdviewNeedMoney = (SDSimpleProjectDetailItemView) findViewById(R.id.act_confirm_bid_sdview_need_money);
        mTxtMoneyCanUse = (TextView) findViewById(R.id.act_confirm_bid_txt_money_can_use);
        mTxtIWantRecharge = (TextView) findViewById(R.id.act_confirm_bid_txt_i_want_recharge);
        mEdtInvestMoneyAmount = (EditText) findViewById(R.id.act_confirm_bid_edt_invest_money_amount);
        mEdtPayPassword = (EditText) findViewById(R.id.act_confirm_bid_edt_pay_password);
        mBtnConfirmInvest = (Button) findViewById(R.id.act_confirm_bid_btn_confirm_invest);
        mBtnConfirmInvest.setOnClickListener(this);
    }

    private void initItems() {
        if (mModel != null) {

            if (mModel.getName() != null) // 项目名称
            {
                mTvTitle.setText(mModel.getName());
            } else {
                mTvTitle.setText(R.string.data_not_found);
            }
            SDViewUtil.measureView(mTvTitle);
            int width = mTvTitle.getMeasuredWidth();
            if ((width + 10) > SDUIUtil.getScreenWidth(getApplicationContext())) {
                mTvTitle.setGravity(Gravity.LEFT);
            } else {
                mTvTitle.setGravity(Gravity.CENTER);
            }

            mSdviewBorrowAmount.setTV_Left(getString(R.string.loan_amount)); // 借款金额
            mSdviewBorrowAmount.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
            if (mModel.getBorrow_amount_format() != null) {
                mSdviewBorrowAmount.setTV_Right(mModel.getBorrow_amount_format());
            } else {
                mSdviewBorrowAmount.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewProgress.setTV_Left(getString(R.string.complete_schedule)); // 完成进度
            mSdviewProgress.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
            if (mModel.getProgress_point_format() != null) {
                mSdviewProgress.setTV_Right(mModel.getProgress_point_format());
            } else {
                mSdviewProgress.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewRate.setTV_Left(getString(R.string.interest_rate)); // 年利率
            mSdviewRate.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
            if (mModel.getRate() != null) {
                mSdviewRate.setTV_Right(mModel.getRate() + "%");
            } else {
                mSdviewRate.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewRepayTime.setTV_Left(getString(R.string.term) + "　　"); // 期限
            mSdviewRepayTime.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
            if (mModel.getRepay_time() != null && mModel.getRepay_time_type_format() != null) {
                mSdviewRepayTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
            } else {
                mSdviewRepayTime.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewLoanType.setTV_Left(getString(R.string.repayment_type)); // 还款方式
            if (mModel.getLoantype() != null && mModel.getLoantype_format() != null) {
                mSdviewLoanType.setTV_Right(mModel.getLoantype_format());
            } else {
                mSdviewLoanType.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewNeedMoney.setTV_Left(getString(R.string.available_amount)); // 可投金额
            mSdviewNeedMoney.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
            if (mModel.getNeed_money() != null) {
                mSdviewNeedMoney.setTV_Right(mModel.getNeed_money());
            } else {
                mSdviewNeedMoney.setTV_Right(getString(R.string.data_not_found));
            }

            mTxtMoneyCanUse.setText(MoneyUtils.formatMoney(Double.valueOf(mModel.getCanUse()), Locale.ENGLISH));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.act_confirm_bid_txt_i_want_recharge) {

        } else if (v.getId() == R.id.act_confirm_bid_btn_confirm_invest) {
            confirmInvestAction();
        }
    }

    private void confirmInvestAction() {
        if (validateParams()) {
            InvestProjectAPI.getInstance().invest(UserManager.getInstance(getApplicationContext()).getUid(), Long.valueOf(mModel.getDealId()),
                    mEdtPayPassword.getText().toString(), Double.valueOf(mStrBidMoney), new SubscriberOnListener<InvestResultModel>() {
                        @Override
                        public void onSucceed(InvestResultModel data) {
                            mProgressHUD.dismissImmediately();
                            if (data.state == 0) {
                                mProgressHUD.showSuccessWithStatus(getString(R.string.invest_success));
                                mProgressHUD.setOnDismissListener(new OnDismissListener() {
                                    @Override
                                    public void onDismiss(SVProgressHUD svProgressHUD) {
                                        finish();
                                    }
                                });
                            } else if (data.state == 1) {
                                mProgressHUD.showErrorWithStatus(getString(R.string.pay_password_error));
                            } else if (data.state == 2) {
                                mProgressHUD.showErrorWithStatus(getString(R.string.remainder_not_enough));
                            } else if (data.state == 3) {
                                mProgressHUD.showErrorWithStatus(getString(R.string.invest_failed));
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            mProgressHUD.dismissImmediately();
                            mProgressHUD.showErrorWithStatus(msg);
                        }
                    });

            mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
    }

    private boolean validateParams() {
        if (!UserManager.getInstance(getApplicationContext()).isAuth()) {
            return false;
        }
        mStrBidMoney = mEdtInvestMoneyAmount.getText().toString().trim();
        mStrBidPayPassword = mEdtPayPassword.getText().toString();

        if (TextUtils.isEmpty(mStrBidMoney)) {
            SDToast.showToast(getString(R.string.the_invest_amount_can_not_be_empty));
            SDUIUtil.showInputMethod(getApplicationContext(), mEdtInvestMoneyAmount, true);
            return false;
        }
        try {
//            double moneyCanUse = Double.parseDouble(App.getApplication().getmLocalUser().getUserMoney());
//            double moneyBid = Double.parseDouble(mStrBidMoney);
//            double moneyMinLoan = Double.parseDouble(mModel.getMin_loan_money());
//            if (moneyBid < moneyMinLoan) {
//                SDToast.showToast("您的投资金额小于最低起投金额:" + mModel.getMin_loan_money() + "元");
//                return false;
//            }
//
//            if (moneyBid > moneyCanUse) {
//                SDToast.showToast("您的可用余额不足!");
//                SDUIUtil.showInputMethod(getApplicationContext(), mEdtInvestMoneyAmount, true);
//                return false;
//            }
        } catch (Exception e) {
            return false;
        }

        if (TextUtils.isEmpty(mStrBidPayPassword)) {
            SDToast.showToast(getString(R.string.payment_password_can_not_be_empty));
            SDUIUtil.showInputMethod(getApplicationContext(), mEdtPayPassword, true);
            return false;
        }
        return true;
    }
}