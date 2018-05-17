package com.wavpayment.wavpay.ui.main.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lan.sponge.util.log.SpongeLogger;
import com.lan.sponge.widget.BaseDecoration;
import com.wavpayment.wavpay.R;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2018/1/5.
 */

public class TypeFragment extends android.support.v4.app.DialogFragment {

    private final String []array;
    private RecyclerView recycle;

    public TypeFragment(String[] array) {
        this.array = array;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_type,null);
        recycle = view.findViewById(R.id.rv_type);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(manager);
        recycle.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), com.lan.sponge.R.color.app_background), 5));
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> list = new ArrayList<>();
        for (String s:array){
            list.add(s);
            SpongeLogger.e("s",s);
        }
        TypeDialogAdapter mAdapter = new TypeDialogAdapter(list);
        recycle.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String amount = (String) adapter.getData().get(position);
            listener.onAmount(amount);
            dismiss();
        });
    }

    private TypeListener listener;

    public void setListener(TypeListener listener) {
        this.listener = listener;
    }

    interface TypeListener {
        void onAmount(String amount);
    }
}
