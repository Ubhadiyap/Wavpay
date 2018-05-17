package com.hbyundu.shop.ui.credit.repay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.rest.api.repay.RepayAPI;
import com.hbyundu.shop.rest.api.repay.RepayDetailAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.RepayResultModel;
import com.hbyundu.shop.rest.model.repay.RepaySubDataModel;
import com.hbyundu.shop.rest.model.repay.RepaySubItemModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.util.MoneyUtils;
import com.hbyundu.shop.vendor.util.NumberUtils;
import com.hbyundu.shop.vendor.util.SDFormatUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 偿还贷款界面
 *
 * @author js02
 */
public class RepayBorrowRepayActivity extends BaseActivity implements OnClickListener {
    public static final String EXTRA_DEAL_ID = "extra_deal_id";

    public static final int RESULT_CODE_REPAY_SUCCESS = 1;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_name)
    private TextView mTxtName = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_borrow_amount)
    private TextView mTxtBorrowAmount = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_rate)
    private TextView mTxtRate = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_repay_time)
    private TextView mTxtRepayTime = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_repay_money)
    private TextView mTxtRepayMoney = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_need_remain_repay_money)
    private TextView mTxtNeedRemainRepayMoney = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_lsv_repay_record)
    private ListView mLsvRepayRecord = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_txt_total_repay_money)
    private TextView mTxtTotalRepayMoney = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_btn_recharge)
    private Button mBtnRecharge = null;

//    @ViewInject(id = R.id.act_repay_borrow_repay_btn_confirm_repay)
    private Button mBtnConfirmRepay = null;

    private int mDealId = -1;

    private Uc_Quick_RefundActDealModel mDealModel = null;

    private List<Uc_Quick_RefundActLoan_ListModel> mListLoanListModel = new ArrayList<Uc_Quick_RefundActLoan_ListModel>();
    private RepayBorrowRepayListAdapter mAdapter = null;
    private String mIds = null;
    private float mTotalMoneyNeedpay = 0;

    private boolean mIsRepaySuccess = false;

    private SVProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay_borrow_repay);
//        SDIoc.injectView(this);
        init();
    }

    private void init() {
        initToolbar();
        initViews();
        initIntentData();
        bindDefaultData();
        registeClick();
        getData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.repay);
    }

    private void initViews() {
        mProgressHUD = new SVProgressHUD(this);

        mTxtName = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_name);

        mTxtBorrowAmount = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_borrow_amount);
        mTxtRate = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_rate);
        mTxtRepayTime = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_repay_time);
        mTxtRepayMoney = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_repay_money);
        mTxtNeedRemainRepayMoney = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_need_remain_repay_money);
        mLsvRepayRecord = (ListView) findViewById(R.id.act_repay_borrow_repay_lsv_repay_record);
        mTxtTotalRepayMoney = (TextView) findViewById(R.id.act_repay_borrow_repay_txt_total_repay_money);
        mBtnRecharge = (Button) findViewById(R.id.act_repay_borrow_repay_btn_recharge);
        mBtnConfirmRepay = (Button) findViewById(R.id.act_repay_borrow_repay_btn_confirm_repay);
    }

    private void bindDefaultData() {
        mAdapter = new RepayBorrowRepayListAdapter(mListLoanListModel, this, new RepayBorrowRepayActivity_RepayBorrowRepayListAdapterListener());
        mLsvRepayRecord.setAdapter(mAdapter);
    }

    private void initIntentData() {
        String strId = getIntent().getStringExtra(EXTRA_DEAL_ID);
        if (!TextUtils.isEmpty(strId)) {
            try {
                mDealId = Integer.parseInt(strId);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    protected void bindTopData(Uc_Quick_RefundActDealModel dealModel) {
        if (dealModel != null) {
            if (dealModel.getName() != null) {
                mTxtName.setText(dealModel.getName());
            } else {
                mTxtName.setText(R.string.not_found);
            }

            if (dealModel.getBorrow_amount_format() != null) {
                mTxtBorrowAmount.setText(dealModel.getBorrow_amount_format());
            } else {
                mTxtBorrowAmount.setText(R.string.not_found);
            }

            if (dealModel.getRate() != null) {
                mTxtRate.setText(dealModel.getRate() + "%");
            } else {
                mTxtRate.setText(R.string.not_found);
            }

            if (dealModel.getRepay_time_format() != null) {
                mTxtRepayTime.setText(dealModel.getRepay_time_format());
            } else {
                mTxtRepayTime.setText(R.string.not_found);
            }

            if (dealModel.getRepay_money_format() != null) {
                mTxtRepayMoney.setText(dealModel.getRepay_money_format());
            } else {
                mTxtRepayMoney.setText(R.string.not_found);
            }

            if (dealModel.getNeed_remain_repay_money_format() != null) {
                mTxtNeedRemainRepayMoney.setText(dealModel.getNeed_remain_repay_money_format());
            } else {
                mTxtNeedRemainRepayMoney.setText(R.string.not_found);
            }
        }
    }

    private void registeClick() {
        mBtnRecharge.setOnClickListener(this);
        mBtnConfirmRepay.setOnClickListener(this);

    }

    private void getData() {
        RepayDetailAPI.getInstance().repayDetail(mDealId, new SubscriberOnListener<RepaySubDataModel>() {
            @Override
            public void onSucceed(RepaySubDataModel data) {
                bindTopData(convertDealData(data));
                mAdapter.updateListViewData(convertDealSubData(data));
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private Uc_Quick_RefundActDealModel convertDealData(RepaySubDataModel data) {
        Uc_Quick_RefundActDealModel dealModel = new Uc_Quick_RefundActDealModel();
        dealModel.setName(data.title);
        dealModel.setBorrow_amount_format(MoneyUtils.formatMoney(data.borrowAmount, Locale.ENGLISH));
        dealModel.setRate(NumberUtils.formatRate(data.rate));
        if (!data.repayTimeType) {
            dealModel.setRepay_time_format(data.repayLimit + getString(R.string.days));
        } else {
            dealModel.setRepay_time_format(data.repayLimit + getString(R.string.months));
        }
        dealModel.setRepay_money_format(MoneyUtils.formatMoney(data.repayAmount, Locale.ENGLISH));
        dealModel.setNeed_remain_repay_money_format(MoneyUtils.formatMoney(data.remainAmount, Locale.ENGLISH));

        return dealModel;
    }

    private List<Uc_Quick_RefundActLoan_ListModel> convertDealSubData(RepaySubDataModel data) {
        List<Uc_Quick_RefundActLoan_ListModel> list = new ArrayList<>();
        for (RepaySubItemModel sub : data.list) {
            Uc_Quick_RefundActLoan_ListModel item = new Uc_Quick_RefundActLoan_ListModel();
            item.setId(String.valueOf(sub.repayId));
            item.setMonth_need_all_repay_money(String.valueOf(sub.remainCount));
            item.setMonth_need_all_repay_money_format(MoneyUtils.formatMoney(sub.remainCount, Locale.ENGLISH));
            try {
                item.setRepay_day_format(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sub.repayDate)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            item.setCanSelect(sub.hasRepay == 0);
            item.setMonth_has_repay_money_all_format(MoneyUtils.formatMoney(sub.repayCount, Locale.ENGLISH));
            item.setMonth_repay_money_format(MoneyUtils.formatMoney(sub.remainBenxi, Locale.ENGLISH));
            item.setMonth_manage_money_format(MoneyUtils.formatMoney(sub.manageMoney, Locale.ENGLISH));

            list.add(item);
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.act_repay_borrow_repay_btn_recharge) {

        } else if (v.getId() == R.id.act_repay_borrow_repay_btn_confirm_repay) {
            repayAction();
        }
    }

    private void repayAction() {
        if (mIds == null || mIds.length() == 0) {
            return;
        }

        showPasswordPopup();
    }

    private void showPasswordPopup() {
        final EditText editText = new EditText(this);
        editText.setHint(R.string.please_input_pay_password);
        editText.setTextColor(Color.BLACK);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this).setTitle(R.string.please_input_pay_password)
                .setView(editText)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editText.getText().length() == 0) {
                            return;
                        }

                        RepayAPI.getInstance().repay(Long.valueOf(mIds), editText.getText().toString(), new SubscriberOnListener<RepayResultModel>() {
                            @Override
                            public void onSucceed(RepayResultModel data) {
                                mProgressHUD.dismissImmediately();
                                if (data.state == 0) {
                                    mProgressHUD.showSuccessWithStatus(getString(R.string.repay_success));
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
                                } else {
                                    mProgressHUD.showErrorWithStatus(getString(R.string.repay_failure));
                                }
                            }

                            @Override
                            public void onError(String msg) {
                                mProgressHUD.dismissImmediately();
                                mProgressHUD.showErrorWithStatus(getString(R.string.repay_failure));
                            }
                        });

                        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }).show();
    }


    class RepayBorrowRepayActivity_RepayBorrowRepayListAdapterListener implements RepayBorrowRepayListAdapter.RepayBorrowRepayListAdapterListener {
        @Override
        public void onCheckedChange(String ids, float totalMoneyNeedpay) {
            mIds = ids;
            mTotalMoneyNeedpay = totalMoneyNeedpay;
            try {
                mTxtTotalRepayMoney.setText(SDFormatUtil.formatMoneyChina(String.valueOf(totalMoneyNeedpay)));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}