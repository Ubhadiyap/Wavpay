package com.wavpayment.wavpay.ui.left.information;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;
import com.lan.sponge.config.ConfigKeys;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.wavpayment.wavpay.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.wavpayment.wavpay.service.common.CommonHandler.PERSONAL;


/**
 *
 * Created by Administrator on 2017/12/13.
 */

public class QRInfoDelegate extends SpongeDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_info_usre_qr;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ((TextView) $(R.id.tv_title)).setText("QR Code");
        $(R.id.iv_back).setOnClickListener(v -> getSupportDelegate().pop());
        final String response = RxSPTool.readJSONCache(Sponge.getAppContext(), PERSONAL);
        final JSONObject data = JSON.parseObject(response).getJSONObject("user");
        final String baseUrl = Sponge.getConfiguration(ConfigKeys.API_HOST);
        final String photo = baseUrl.substring(0, baseUrl.length() - 1) + data.getString("headImg");
        final String nick = data.getString("nickName");

        final String qrCode = "https://github.com/";

        ((TextView) $(R.id.nickname)).setText(nick);
        ImageView logo = $(R.id.photo);
        Glide.with(_mActivity)
                .load(photo)
                .apply(RECYCLER_OPTIONS)
                .into(logo);

        Glide.with(_mActivity)
                .load(photo)
                .apply(RECYCLER_OPTIONS)
                .into((CircleImageView) $(R.id.qr_photo));

        Drawable drawable = getResources().getDrawable(R.mipmap.wavpay_logo);
        new AwesomeQRCode.Renderer()
                .contents(qrCode)
                .size(800).margin(10)
                .background(RxImageTool.drawable2Bitmap(drawable))
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, Bitmap bitmap) {
                        _mActivity.runOnUiThread(() -> ((ImageView) $(R.id.iv_qr_code)).setImageBitmap(bitmap));
                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {

                    }
                });

    }
}
