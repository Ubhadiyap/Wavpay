package com.wavpayment.wavpay.service.common;


import com.wavpayment.wavpay.ui.main.details.BaseDetailsDelegate;

public class CommonItemEntity {
    private int imageId;
    private String itemName;
    private BaseDetailsDelegate delegate;

    public CommonItemEntity(int imageId, String itemName, BaseDetailsDelegate delegate) {
        this.imageId = imageId;
        this.itemName = itemName;
        this.delegate = delegate;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BaseDetailsDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(BaseDetailsDelegate delegate) {
        this.delegate = delegate;
    }
}
