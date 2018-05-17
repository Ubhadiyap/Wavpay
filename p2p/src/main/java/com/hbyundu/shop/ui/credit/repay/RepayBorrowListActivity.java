package com.hbyundu.shop.ui.credit.repay;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


import com.hbyundu.shop.R;
import com.hbyundu.shop.R.color;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.vendor.widget.SDSimpleTabView;
import com.hbyundu.shop.vendor.widget.SDViewNavigatorManager;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

/**
 * 还款界面
 *
 * @author js02
 */
public class RepayBorrowListActivity extends BaseActivity {
    private static final int TEXT_SIZE_TAB = 18;

//    @ViewInject(id = R.id.act_repay_borrow_list_tab_repay_borrow_list)
    private SDSimpleTabView mTabRepayBorrowList = null;

//    @ViewInject(id = R.id.act_repay_borrow_list_tab_repay_borrow_list_finish)
    private SDSimpleTabView mTabRepayBorrowListFinish = null;

//    @ViewInject(id = R.id.act_repay_borrow_list_frame_container_repay_borrow_list)
    private FrameLayout mFrameContainerRepayBorrowList = null;

//    @ViewInject(id = R.id.act_repay_borrow_list_frame_container_repay_borrow_list_finish)
    private FrameLayout mFrameContainerRepayBorrowListFinish = null;

    private SDViewNavigatorManager mNavigator = new SDViewNavigatorManager();

    private RepayBorrowListFragment mFragRepayBorrowList = null;
    private RepayBorrowListFinishFragment mFragRepayBorrowListFinish = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay_borrow_list);
//        SDIoc.injectView(this);

        mTabRepayBorrowList = (SDSimpleTabView) findViewById(R.id.act_repay_borrow_list_tab_repay_borrow_list);
        mTabRepayBorrowListFinish = (SDSimpleTabView) findViewById(R.id.act_repay_borrow_list_tab_repay_borrow_list_finish);
        mFrameContainerRepayBorrowList = (FrameLayout) findViewById(R.id.act_repay_borrow_list_frame_container_repay_borrow_list);
        mFrameContainerRepayBorrowListFinish = (FrameLayout) findViewById(R.id.act_repay_borrow_list_frame_container_repay_borrow_list_finish);

        initToolbar();
        initTabs();
        addFragments();
    }

    private void addFragments() {
        mFragRepayBorrowList = new RepayBorrowListFragment();
        mFragRepayBorrowListFinish = new RepayBorrowListFinishFragment();

        addFragment(mFragRepayBorrowList, R.id.act_repay_borrow_list_frame_container_repay_borrow_list);
        addFragment(mFragRepayBorrowListFinish, R.id.act_repay_borrow_list_frame_container_repay_borrow_list_finish);

        mNavigator.setSelectIndex(0, mTabRepayBorrowList, true);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.repay);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTabs() {
        mTabRepayBorrowList.setmBackgroundImageNormal(R.drawable.rec_tab_left_normal);
        mTabRepayBorrowList.setmBackgroundImageSelect(R.drawable.rec_tab_left_press);
        mTabRepayBorrowList.setmTextColorNormal(getResources().getColor(color.bg_title));
        mTabRepayBorrowList.setmTextColorSelect(getResources().getColor(color.white));
        mTabRepayBorrowList.setTabName(getString(R.string.repay_list));
        mTabRepayBorrowList.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

        mTabRepayBorrowListFinish.setmBackgroundImageNormal(R.drawable.rec_tab_right_normal);
        mTabRepayBorrowListFinish.setmBackgroundImageSelect(R.drawable.rec_tab_right_press);
        mTabRepayBorrowListFinish.setmTextColorNormal(getResources().getColor(color.bg_title));
        mTabRepayBorrowListFinish.setmTextColorSelect(getResources().getColor(color.white));
        mTabRepayBorrowListFinish.setTabName(getString(R.string.repay_finish_list));
        mTabRepayBorrowListFinish.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

        SDSimpleTabView[] items = new SDSimpleTabView[]{mTabRepayBorrowList, mTabRepayBorrowListFinish};

        mNavigator.setItems(items);
        mNavigator.setmListener(new SDViewNavigatorManager.SDViewNavigatorManagerListener() {

            @Override
            public void onItemClick(View v, int index) {
                switch (index) {
                    case 0:
                        showFragment(mFragRepayBorrowList);
                        hideFragment(mFragRepayBorrowListFinish);
                        break;
                    case 1:
                        showFragment(mFragRepayBorrowListFinish);
                        hideFragment(mFragRepayBorrowList);
                        break;

                    default:
                        break;
                }
            }
        });

    }

}