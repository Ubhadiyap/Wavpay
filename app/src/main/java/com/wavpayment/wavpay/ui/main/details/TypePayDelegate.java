package com.wavpayment.wavpay.ui.main.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.widget.BaseDecoration;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.taobao.accs.utl.UT;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.utils.Utils;
import com.wavpayment.wavpay.widgte.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/5.
 */

public class TypePayDelegate extends SpongeDelegate {

    private int type = 1;//缴费类型
    private TypePayAdapter mAdapter;
    private RecyclerView rvCommon;

    @Override
    public Object setLayout() {
        return R.layout.delegate_common;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("Pay cost unit");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        rvCommon = $(R.id.rv_common);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        rvCommon.setLayoutManager(manager);
        rvCommon.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), com.lan.sponge.R.color.app_background), 5));
        network();
    }

    public void setType(int type) {
        this.type = type;
    }

    private void network() {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        Network.getInstance()
                .get(NetUrl.ORGLIST,map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (Utils.isJSONValid(response.body())){
                            final JSONObject data = JSON.parseObject(response.body());
                            final int code = data.getIntValue("statusCode");
                            if (code == 200) {
                                final JSONArray array = data.getJSONArray("orgNameList");
                                final int size = array.size();
                                final ArrayList<MultipleItemEntity> entities = new ArrayList<>();
                                for (int i = 0; i < size; i++) {
                                    final JSONObject item = array.getJSONObject(i);
                                    final String accountRequire = item.getString("accountRequire");
                                    final String amountArr = item.getString("amountArr");
                                    final String minMaxAmount = item.getString("minMaxAmount");
                                    final String orgCode = item.getString("orgCode");
                                    final String orgName = item.getString("orgName");
                                    final int type = item.getIntValue("type");
                                    final int id = item.getIntValue("id");
                                    final int isFixed = item.getIntValue("isFixed");
                                    MultipleItemEntity entity = MultipleItemEntity.builder()
                                            .setField(TypeType.accountRequire, accountRequire)
                                            .setField(TypeType.amountArr, amountArr)
                                            .setField(TypeType.minMaxAmount, minMaxAmount)
                                            .setField(TypeType.orgCode, orgCode)
                                            .setField(TypeType.orgName, orgName)
                                            .setField(TypeType.type, type)
                                            .setField(TypeType.id, id)
                                            .setField(TypeType.isFixed, isFixed)
                                            .build();
                                    entities.add(entity);
                                }
                                mAdapter = new TypePayAdapter(entities);
                                rvCommon.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener((adapter, view, position) -> {
                                    MultipleItemEntity entity = entities.get(position);
                                    listener.onType(entity);
                                    getSupportDelegate().pop();
                                });
                            }
                        }
                    }
                });
    }

    private TypePayListener listener;

    public void setListener(TypePayListener listener) {
        this.listener = listener;
    }

    public interface TypePayListener {
        void onType(MultipleItemEntity entity);
    }
}
