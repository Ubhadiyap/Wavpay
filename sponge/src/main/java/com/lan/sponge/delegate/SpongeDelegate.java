package com.lan.sponge.delegate;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lan.sponge.R;
import com.lan.sponge.widget.BaseDecoration;

public abstract class SpongeDelegate extends BaseDelegate{

    @SuppressWarnings("unchecked")
    public <T extends SpongeDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }

    protected void show(String msg){
        Toast.makeText(_mActivity,msg,Toast.LENGTH_LONG).show();
    }

    protected void initRecyclerView(RecyclerView recyclerView) {
        final GridLayoutManager manager = new GridLayoutManager(_mActivity, 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 2));
    }
    //设置图片加载策略
    public static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();


    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void post(Runnable runnable) {

    }

}
