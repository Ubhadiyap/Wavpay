package com.wavpayment.wavpay.service.main.index;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.util.log.SpongeLogger;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonEntity;
import com.wavpayment.wavpay.service.common.CommonItemEntity;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.service.net.NetUrl;
import com.wavpayment.wavpay.service.net.Network;
import com.wavpayment.wavpay.ui.main.details.BroadbandDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.CouncilDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.ElectricDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.MobileDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.PostpaidDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.WaterDetailsDelegate;

import java.util.ArrayList;

public class IndexPresenterImpl implements IIndexContract.Presenter {
    private final IIndexContract.View VIEW;
    private final Context context = Sponge.getAppContext();
    private final String TOP_UP = context.getString(R.string.top_up);
    private final String BOOKING = context.getString(R.string.booking);

    public IndexPresenterImpl(IIndexContract.View view) {
        VIEW = view;
    }

    @Override
    public void initView() {
        final ArrayList<CommonEntity> data = new ArrayList<>();
        final ArrayList<CommonItemEntity> TOP = new ArrayList<>();
        final ArrayList<CommonItemEntity> BOOK = new ArrayList<>();
        TOP.add(new CommonItemEntity(R.mipmap.mobile, context.getString(R.string.mobile), new MobileDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.electri, context.getString(R.string.electri), new ElectricDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.water, context.getString(R.string.water), new WaterDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.council, context.getString(R.string.council), new CouncilDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.postpaid, context.getString(R.string.postpaid), new PostpaidDetailsDelegate()));
        data.add(new CommonEntity(CommonItemType.MORE, TOP_UP, TOP));
        BOOK.add(new CommonItemEntity(R.mipmap.broadband, context.getString(R.string.broadband), new BroadbandDetailsDelegate()));
        BOOK.add(new CommonItemEntity(R.mipmap.game, context.getString(R.string.game), null));
        BOOK.add(new CommonItemEntity(R.mipmap.hotel, context.getString(R.string.hotel), null));
        BOOK.add(new CommonItemEntity(R.mipmap.bus, context.getString(R.string.bus), null));
        BOOK.add(new CommonItemEntity(R.mipmap.ktm, "KTM", null));
        BOOK.add(new CommonItemEntity(R.mipmap.flight, context.getString(R.string.flight), null));
        BOOK.add(new CommonItemEntity(R.mipmap.movie, context.getString(R.string.movie), null));
        data.add(new CommonEntity(CommonItemType.MORE, BOOKING, BOOK));
        VIEW.onInitView(data);
    }

    @Override
    public void banner() {
        Network.getInstance()
                .get("advertisImg/codes.html")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONArray pathList = JSON.parseObject(response.body()).getJSONArray("pathList");
                        final ArrayList<String> lunboOne = new ArrayList<>();
                        if (pathList != null) {
                            RxSPTool.putJSONCache(Sponge.getAppContext(), "banner", response.body());
                            for (int i = 0; i < pathList.size(); i++) {
                                final JSONArray it = pathList.getJSONArray(i);
                                String url = Sponge.getConfiguration(ConfigKeys.API_HOST);
                                url = url.substring(0, url.length() - 1);
                                if (it.getString(0).equals("lunbo1") || it.getString(0).equals("lunbo2")) {
                                    url += pathList.getJSONArray(i).getString(1);
                                    lunboOne.add(url);
                                }
                            }
                            VIEW.onBanner(lunboOne);
                        }
                    }
                });

    }
}
