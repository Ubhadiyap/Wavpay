package com.wavpayment.wavpay.widgte;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.wavpayment.wavpay.R;

/**
 *
 * Created by Administrator on 2017/12/29.
 */

public class PopWinShare extends PopupWindow {
    private View mainView;

    public PopWinShare(Activity paramActivity) {
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_share, null);
        mainView.setFocusable(true);
        mainView.setFocusableInTouchMode(true);
        setContentView(mainView);
        RadioGroup group = mainView.findViewById(R.id.rg_root);
        group.setOnCheckedChangeListener((radioGroup, i) -> listener.onId(i));
        //设置宽度
        setWidth(420);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    private PopWinShareListener listener;

    public void setListener(PopWinShareListener listener) {
        this.listener = listener;
    }

    public interface PopWinShareListener {
        void onId(int id);
    }
}
