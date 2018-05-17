package com.wavpayment.wavpay.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.library.bubbleview.BubbleTextView;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.ui.left.about.AboutDelegate;
import com.wavpayment.wavpay.ui.left.information.InfoDelegate;
import com.wavpayment.wavpay.ui.left.security.SecurityDelegate;
import com.wavpayment.wavpay.ui.left.setting.SettingDelegate;
import com.wavpayment.wavpay.ui.main.Index.IndexDelegate;
import com.wavpayment.wavpay.ui.main.message.MessageDelegate;
import com.wavpayment.wavpay.ui.main.tool.ToolDelegate;

import java.util.ArrayList;

import me.yokeyword.fragmentation.ISupportFragment;



public abstract class BaseBottomDelegate extends SpongeDelegate implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivHome;
    private TextView tvHome;
    private ImageView ivTool;
    private TextView tvTool;
    private NavigationView navView;
    private DrawerLayout mDrawer;
    private TextView tvTitle;
    private ImageView navImg;
    private TextView navName;
    private TextView navPhone;

    private int mCurrentDelegate = 0;
    private int mIndexDelegate = 0;
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();

    private BubbleTextView btMsg;

    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom;
    }

    protected abstract int setIndexDelegate();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        ITEM_DELEGATES.add(new IndexDelegate());
        ITEM_DELEGATES.add(new ToolDelegate());
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView();
        navView.setNavigationItemSelectedListener(this);
        final ISupportFragment[] delegateArray = ITEM_DELEGATES.toArray(new ISupportFragment[2]);
        getSupportDelegate().loadMultipleRootFragment(R.id.fl_container, mIndexDelegate, delegateArray);
        pitchOn(true, mIndexDelegate);
        //  未读消息
        CallbackManager.getInstance().addCallback(CallbackType.MESSAGE_PUSH, args -> {
            int count = Integer.parseInt(args.toString());
            if (count != 0) {
                btMsg.setVisibility(View.VISIBLE);
                if (count > 99) {
                    btMsg.setText("99+");
                } else {
                    btMsg.setText(args.toString());
                }
            }else {
                btMsg.setVisibility(View.GONE);
            }
        });
    }

    protected void setTitle(String title) {
        ((TextView) $(R.id.tv_title)).setText(title);
    }

    protected void setNavName(String nickname) {
        navName.setText(nickname);
    }

    protected void setNavImg(Object o) {
        if (o != null) {
            String url = Sponge.getConfiguration(ConfigKeys.API_HOST);
            url = url.substring(0, url.length() - 1) + o.toString();
            Glide.with(_mActivity)
                    .load(url)
                    .apply(RECYCLER_OPTIONS)
                    .into(navImg);
        }
    }

    protected void setNavPhone(String phone) {
        navPhone.setText(phone);
    }

    private void initView() {
        tvTitle = $(R.id.tv_title);
        ivHome = $(R.id.iv_home);
        tvHome = $(R.id.tv_home);
        ivTool = $(R.id.iv_tool);
        tvTool = $(R.id.tv_tool);
        navView = $(R.id.nav_view);
        mDrawer = $(R.id.drawer_layout);
        btMsg = $(R.id.bt_msg);

        LinearLayout llNavHeader = (LinearLayout) navView.getHeaderView(0);
        navImg = llNavHeader.findViewById(R.id.img_nav);
        navName = llNavHeader.findViewById(R.id.nv_name);
        navPhone = llNavHeader.findViewById(R.id.nv_phone);

        llNavHeader.setOnClickListener(v -> mDrawer.closeDrawer(GravityCompat.START));
        $(R.id.ll_home).setOnClickListener(v -> pitchOn(true, 0));
        $(R.id.ll_tool).setOnClickListener(v -> pitchOn(false, 1));
        $(R.id.ll_scan).setOnClickListener(v -> onScan());
        $(R.id.iv_user).setOnClickListener(v -> {
            mDrawer.openDrawer(GravityCompat.START);
        });
        $(R.id.iv_msg).setOnClickListener(v -> onMessage());
    }


    private void onMessage() {
        getSupportDelegate().start(new MessageDelegate());
    }

    protected abstract void onScan();

    protected abstract void onLoginOut();

    private void pitchOn(boolean check, int tag) {
        getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
        mCurrentDelegate = tag;
        if (check) {
            ivHome.setImageResource(R.mipmap.home_p);
            tvHome.setTextColor(getResources().getColor(R.color.app_bg));
            ivTool.setImageResource(R.mipmap.tool);
            tvTool.setTextColor(getResources().getColor(R.color.item));
        } else {
            ivHome.setImageResource(R.mipmap.home);
            tvHome.setTextColor(getResources().getColor(R.color.item));
            ivTool.setImageResource(R.mipmap.tool_p);
            tvTool.setTextColor(getResources().getColor(R.color.app_bg));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);
        mDrawer.postDelayed(() -> {
            final int id = item.getItemId();
            if (id == R.id.nav_information) {
                getSupportDelegate().start(new InfoDelegate());
            } else if (id == R.id.nav_security) {
                getSupportDelegate().start(new SecurityDelegate());
            } else if (id == R.id.nav_message) {
                onMessage();
            } else if (id == R.id.nav_setting) {
                getSupportDelegate().start(new SettingDelegate());
            } else if (id == R.id.nav_about_payment) {
                getSupportDelegate().start(new AboutDelegate());
            } else if (id == R.id.nav_logout) {
               onLoginOut();
            } else {
            }
        }, 300);
        return true;
    }

}
