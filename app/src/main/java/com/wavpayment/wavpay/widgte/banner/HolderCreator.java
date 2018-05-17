package com.wavpayment.wavpay.widgte.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;


public class HolderCreator  implements CBViewHolderCreator<ImageHolder>{
    @Override
    public ImageHolder createHolder() {
        return new ImageHolder();
    }
}
