package com.hbyundu.shop.ui.credit.apply;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hbyundu.shop.R;
import com.hbyundu.shop.R.color;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.invest.InvestProjectDetailAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.invest.InvestDetailModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.credit.constant.Constant;
import com.hbyundu.shop.ui.credit.invest.confirm.InvestConfirmBidActivity;
import com.hbyundu.shop.ui.credit.invest.list.DealsActItemModel;
import com.hbyundu.shop.ui.launcher.LoginActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.NumberUtils;
import com.hbyundu.shop.vendor.util.SDToast;
import com.hbyundu.shop.vendor.util.SDUIUtil;
import com.hbyundu.shop.vendor.util.SDViewUtil;
import com.hbyundu.shop.vendor.widget.SDSimpleProjectDetailItemView;
import com.hbyundu.shop.vendor.widget.SDSimpleProjectDetailMultiItemView;

import java.util.Locale;

/**
 * 项目详情界面
 *
 * @author js02
 */
public class ApplyLoanDetailActivity extends BaseActivity implements OnClickListener {

    private TextView mTvTitle = null;
    private TextView mTvNum = null;
    private SDSimpleProjectDetailItemView mSdviewBorrowAmount = null;
    private SDSimpleProjectDetailItemView mSdviewRate = null;
    private SDSimpleProjectDetailItemView mSdviewRepayTime = null;
    private SDSimpleProjectDetailItemView mSdviewRepayMethod = null;
    private SDSimpleProjectDetailItemView mSdviewStatus = null;
    private SDSimpleProjectDetailMultiItemView mSdviewDeleteMsg = null;
    private Button mBtnLookDetail = null;
    private Button mBtnInvest = null;
    private DealsActItemModel mModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        initToolbar();
        initViews();
        getData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.loan_detail);
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
        mTvTitle = (TextView) findViewById(R.id.act_project_detail_tv_title);
        mTvNum = (TextView) findViewById(R.id.act_project_detail_tv_num);
        mSdviewBorrowAmount = (SDSimpleProjectDetailItemView) findViewById(R.id.act_project_detail_sdview_borrow_amount);
        mSdviewRate = (SDSimpleProjectDetailItemView) findViewById(R.id.act_project_detail_sdview_rate);
        mSdviewRepayTime = (SDSimpleProjectDetailItemView) findViewById(R.id.act_project_detail_sdview_repay_time);
        mSdviewRepayMethod = (SDSimpleProjectDetailItemView) findViewById(R.id.act_project_detail_sdview_repay_method);
        mBtnLookDetail = (Button) findViewById(R.id.act_project_detail_btn_look_detail);
        mBtnInvest = (Button) findViewById(R.id.act_project_detail_btn_invest);
        mSdviewStatus = (SDSimpleProjectDetailItemView) findViewById(R.id.act_project_detail_sdview_status);
        mSdviewDeleteMsg = (SDSimpleProjectDetailMultiItemView) findViewById(R.id.act_project_detail_sdview_delete_msg);

        mBtnInvest.setOnClickListener(this);
    }

    private void getData() {
        long dealId = getIntent().getLongExtra("dealId", 0);
        InvestProjectDetailAPI.getInstance().investDetail(UserManager.getInstance(getApplicationContext()).getUid(), dealId, new SubscriberOnListener<InvestDetailModel>() {
            @Override
            public void onSucceed(InvestDetailModel data) {
                convertData(data);
                initItems();

                findViewById(R.id.activity_invest_project_detail_sv).setVisibility(View.VISIBLE);
//                findViewById(R.id.activity_invest_project_detail_action_ll).setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void convertData(InvestDetailModel data) {
        mModel = new DealsActItemModel();
        mModel.setName(data.name);
        mModel.setId(data.dealsn);
        mModel.setBorrow_amount_format(MoneyUtils.formatMoney(data.totalmoney, Locale.ENGLISH));
        mModel.setNeed_money(MoneyUtils.formatMoney(data.symoney, Locale.ENGLISH));
        mModel.setMin_loan_money_format(MoneyUtils.formatMoney(data.minloney, Locale.ENGLISH));
        mModel.setRate(String.valueOf(data.rate));
        mModel.setLoantype(this, data.method);
        mModel.setLoantype_format(data.method);
        mModel.setRemain_time_format(data.sytime);
        mModel.setRepay_time(String.valueOf(data.repay_time));
        mModel.setRepay_time_type(this, String.valueOf(data.repay_time_type));
        mModel.setDeal_status(this, data.deal_status);
        mModel.setUser_id(String.valueOf(data.user_id));
        mModel.setProgress_point_format(NumberUtils.formatRate(Double.valueOf(data.baifenbi)) + "%");
        mModel.setCanUse(String.valueOf(data.money_encrypt));
        mModel.setDealId(String.valueOf(getIntent().getLongExtra("dealId", 0)));
        mModel.publishWait = data.publishWait;
        mModel.isDelete = data.isDelete;
        mModel.deleteMsg = data.deleteMsg;
        mModel.customStatus = data.customStatus;
    }

    private void initItems() {
        if (mModel != null) {
            if (mModel.getName() != null) { // 项目名称
                mTvTitle.setText(mModel.getName());
            } else {
                mTvTitle.setText(getString(R.string.data_not_found));
            }
            SDViewUtil.measureView(mTvTitle);
            int width = mTvTitle.getMeasuredWidth();
            if ((width + 10) > SDUIUtil.getScreenWidth(getApplicationContext())) {
                mTvTitle.setGravity(Gravity.LEFT);
            } else {
                mTvTitle.setGravity(Gravity.CENTER);
            }

            if (mModel.getId() != null) { // 编号id
                mTvNum.setText(getString(R.string.loan_number) + " " + mModel.getId());
            } else {
                mTvNum.setText(getString(R.string.data_not_found));
            }

            mSdviewBorrowAmount.setTV_Left(getString(R.string.loan_amount)); // 借款金额
            mSdviewBorrowAmount.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
            if (mModel.getBorrow_amount_format() != null) {
                mSdviewBorrowAmount.setTV_Right(mModel.getBorrow_amount_format());
            } else {
                mSdviewBorrowAmount.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewRate.setTV_Left(getString(R.string.interest_rate)); // 年利率
            mSdviewRate.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
            if (mModel.getRate() != null) {
                mSdviewRate.setTV_Right(mModel.getRate() + "%");
            } else {
                mSdviewRate.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewRepayTime.setTV_Left(getString(R.string.term)); // 期限
            mSdviewRepayTime.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
            if (mModel.getRepay_time() != null && mModel.getRepay_time_type_format() != null) {
                mSdviewRepayTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
            } else {
                mSdviewRepayTime.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewRepayMethod.setTV_Left(getString(R.string.repayment_type)); // 还款方式
            if (mModel.getLoantype() != null && mModel.getLoantype_format() != null) {
                mSdviewRepayMethod.setTV_Right(mModel.getLoantype_format());
            } else {
                mSdviewRepayMethod.setTV_Right(getString(R.string.data_not_found));
            }

            mSdviewStatus.setTV_Left(getString(R.string.loan_status));
            if (mModel.customStatus == 0) {
                mSdviewStatus.setTV_Right(getString(R.string.loan_rejected));
            } else if (mModel.customStatus == 1) {
                mSdviewStatus.setTV_Right(getString(R.string.loan_in_review));
            } else {
                mSdviewStatus.setTV_Right(getString(R.string.loan_passed));
            }

            mSdviewDeleteMsg.setTV_Left(getString(R.string.loan_rejected_reason));
            mSdviewDeleteMsg.setTV_Right(mModel.deleteMsg);
            if (mModel.customStatus != 0) {
                findViewById(R.id.act_project_detail_sdview_delete_msg_divide).setVisibility(View.GONE);
                mSdviewDeleteMsg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.act_project_detail_btn_look_detail) {

        } else if (v.getId() == R.id.act_project_detail_btn_invest) {
            investAction();
        }
    }

    private void investAction() {
        if (!UserManager.getInstance(getApplicationContext()).isAuth()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            if (mModel != null && mModel.getDeal_status() != null && !mModel.getDeal_status().equals(Constant.DealStatus.LOANING)) {
                SDToast.showToast(getString(R.string.the_current_standard_can_not_invest));
                return;
            }
            if (String.valueOf(UserManager.getInstance(getApplicationContext()).getUid()).equals(mModel.getUser_id())) {
                SDToast.showToast(getString(R.string.you_can_not_invest_for_yourself));
            } else {
                Intent intent = new Intent(this, InvestConfirmBidActivity.class);
                intent.putExtra(InvestConfirmBidActivity.EXTRA_DEALS_ITEM_DETAIL_MODEL, mModel);
                startActivity(intent);
            }
        }
    }
}