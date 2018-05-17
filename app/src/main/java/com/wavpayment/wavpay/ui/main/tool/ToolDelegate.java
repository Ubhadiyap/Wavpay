package com.wavpayment.wavpay.ui.main.tool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.ui.main.BottomItemDelegate;


/**
 *
 * Created by Administrator on 2017/11/23.
 */

public class ToolDelegate extends BottomItemDelegate {
    RecyclerView rvTool;
    private ToolHandler mToolHandler;

    @Override
    public Object setLayout() {
        return R.layout.delegate_tool;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        rvTool = $(R.id.rv_tool);
        mToolHandler = ToolHandler.create(rvTool, new ToolDataConverter());
        CallbackManager.getInstance()
                .addCallback(CallbackType.DIALOG, args -> dialog(args.toString()));
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView(rvTool);
        mToolHandler.refresh(this);
    }
}
