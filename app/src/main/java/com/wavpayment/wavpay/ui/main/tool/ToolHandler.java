package com.wavpayment.wavpay.ui.main.tool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.service.common.CommonEntity;
import com.wavpayment.wavpay.service.common.CommonItemEntity;
import com.wavpayment.wavpay.service.common.CommonItemType;
import com.wavpayment.wavpay.ui.main.details.BroadbandDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.CouncilDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.ElectricDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.MobileDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.PostpaidDetailsDelegate;
import com.wavpayment.wavpay.ui.main.details.WaterDetailsDelegate;
import com.wavpayment.wavpay.widgte.recycler.DataConverter;
import com.wavpayment.wavpay.widgte.recycler.MultipleRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/11/22.
 */

public class ToolHandler {
    protected RecyclerView RECYCLERVIEW;
    protected DataConverter CONVERTER;
    protected MultipleRecyclerAdapter mAdapter;
    private final Context context = Sponge.getAppContext();
    private final String TOP_UP = context.getString(R.string.top_up);
    private final String BOOKING = context.getString(R.string.booking);
    private ToolHandler(RecyclerView recyclerview, DataConverter converter) {
        this.RECYCLERVIEW = recyclerview;
        this.CONVERTER = converter;
    }

    public static ToolHandler create(RecyclerView recyclerview, DataConverter converter) {
        return new ToolHandler(recyclerview, converter);
    }

    protected void refresh(SpongeDelegate delegate) {
        final List<CommonEntity> DATA = new ArrayList<>();
        final  ArrayList<CommonItemEntity> TOP = new ArrayList<>();
        final ArrayList<CommonItemEntity> BOOK = new ArrayList<>();
        TOP.add(new CommonItemEntity(R.mipmap.mobile, context.getString(R.string.mobile), new MobileDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.electri, context.getString(R.string.electri), new ElectricDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.water, context.getString(R.string.water), new WaterDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.council, context.getString(R.string.council), new CouncilDetailsDelegate()));
        TOP.add(new CommonItemEntity(R.mipmap.postpaid, context.getString(R.string.postpaid), new PostpaidDetailsDelegate()));
        DATA.add(new CommonEntity(CommonItemType.MORE, TOP_UP, TOP));
        BOOK.add(new CommonItemEntity(R.mipmap.broadband, context.getString(R.string.broadband), new BroadbandDetailsDelegate()));
        BOOK.add(new CommonItemEntity(R.mipmap.game, context.getString(R.string.game),null));
        BOOK.add(new CommonItemEntity(R.mipmap.hotel,context.getString(R.string.hotel),null));
        BOOK.add(new CommonItemEntity(R.mipmap.bus, context.getString(R.string.bus), null));
        BOOK.add(new CommonItemEntity(R.mipmap.ktm, "KTM", null));
        BOOK.add(new CommonItemEntity(R.mipmap.flight, context.getString(R.string.flight), null));
        BOOK.add(new CommonItemEntity(R.mipmap.movie, context.getString(R.string.movie), null));
        DATA.add(new CommonEntity(CommonItemType.MORE, BOOKING, BOOK));
        //设置Adapter
        mAdapter = ToolAdapter.create(CONVERTER.setData(DATA),delegate);
        RECYCLERVIEW.setAdapter(mAdapter);
    }
}
