package com.hbyundu.shop.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;

import com.hbyundu.shop.R;
import com.hbyundu.shop.application.App;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.ui.BaseActivity;
import com.hbyundu.shop.ui.category.CategoryFragment;
import com.hbyundu.shop.ui.credit.CreditFragment;
import com.hbyundu.shop.ui.launcher.LoginActivity;
import com.hbyundu.shop.ui.personal.PersonalFragment;
import com.hbyundu.shop.ui.recommend.RecommendFragment;

import net.itgoo.tabbadgeview.TabBadgeView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

public class HomeActivity extends BaseActivity {

    private static final int[] sTabBarTextArray = {R.string.tab_recommend, R.string.tab_category, R.string.tab_credit, R.string.tab_personal};

    private static final int[] sTabBarImageArray = {R.drawable.home_web_tab, R.drawable.home_category_tab, R.drawable.home_bill_tab, R.drawable.home_personal_tab};

    private static final Class[] sFragmentArray = {RecommendFragment.class, CategoryFragment.class, CreditFragment.class, PersonalFragment.class};

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        App.init(getApplication());
        initTabBar();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initTabBar() {
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.activity_home_content_fragment);
        mTabHost.getTabWidget().setDividerDrawable(R.color.transparent);

        for (int i = 0; i < sFragmentArray.length; i++) {
            int drawableResId = sTabBarImageArray[i];
            int textResId = sTabBarTextArray[i];

            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(sTabBarTextArray[i])).setIndicator(new TabBadgeView(this, null, drawableResId, textResId));
            mTabHost.addTab(tabSpec, sFragmentArray[i], null);
    }

        mTabHost.getTabWidget().getChildTabViewAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserManager.getInstance(getApplicationContext()).isAuth()) {
                    mTabHost.setCurrentTab(3);
                    mTabHost.getTabWidget().requestFocus(View.FOCUS_FORWARD);
                } else {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void changeTab(int position) {
        mTabHost.setCurrentTab(position);
    }

    @Subscriber(tag = "home_change_tag_event")
    public void changeTagEvent(Integer position) {
        changeTab(position);
    }
}
