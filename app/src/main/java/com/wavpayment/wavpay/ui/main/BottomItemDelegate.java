package com.wavpayment.wavpay.ui.main;

import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;

public abstract class BottomItemDelegate extends SpongeDelegate {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, getString(R.string.exit) + Sponge.getAppContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    protected void dialog(String args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(args);
        builder.setMessage(Sponge.getAppContext().getString(R.string.d_content));
        builder.setPositiveButton(Sponge.getAppContext().getString(R.string.d_ok),
                (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
