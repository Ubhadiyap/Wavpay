package com.wavpayment.wavpay.downline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.lan.sponge.activity.ProxyActivity;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonHandler;
import com.wavpayment.wavpay.test.Test;
import com.wavpayment.wavpay.utils.Utils;

import static com.jyn.vcview.R.styleable.AlertDialog;

/**
 * 强制下线
 * Created by lenovo on 2018/5/15.
 */

public class DownlineReciver extends BroadcastReceiver {
    private ProxyActivity mActivity;
    private Context mContext;
    public DownlineReciver() {

    }

    public void setmActivity(ProxyActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        CommonHandler.getInstance().downLineWithPop(mActivity);//当调用强制下线时就离线账号
        String account = intent.getStringExtra("account");//账号
        String current_date = intent.getStringExtra("current_date");//登录时间
        String phone_moudle = Utils.getPhoneModel();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        SpannableString message = new SpannableString("Your WavPay "+account+" is logged in at device  "+phone_moudle+"  at "+current_date+". If you do not do it yourself, your login password may have been compromised. Please change your password. The suggested password is different from your password on other websites.");
        message.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.app_bg)), 12, 12+account.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        message.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.app_bg)), 12+account.length()+25, 12+account.length()+25+phone_moudle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        message.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.app_bg)), 12+account.length()+25+5, 12+account.length()+25+5+current_date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(message);
        builder.setCancelable(false); // 将对话框设置为不可取消
        // 给按钮添加注册监听

        SpannableString relogin = new SpannableString(context.getString(R.string.downline_relogin));
        relogin.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.code)), 0,7 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString resetPassword = new SpannableString(context.getString(R.string.downline_reset_password));
        resetPassword.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.app_bg)), 0,14 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setPositiveButton(relogin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击按钮所调用的方法
                CommonHandler.getInstance().downLine(mActivity);
            }
        });
        builder.setNegativeButton(resetPassword, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CommonHandler.getInstance().downLineWithRestePassword(mActivity);
            }
        });
        builder.show();
    }
}