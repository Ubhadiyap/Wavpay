package com.wavpayment.wavpay.service.common;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/22.
 */

public class CommonEntity {

    private int itemType;
    private String title;
    private ArrayList<CommonItemEntity> item;
    private ArrayList<String> banner;

    public CommonEntity(int itemType) {
        this.itemType = itemType;
    }

    public CommonEntity(int itemType, String title, ArrayList<CommonItemEntity>item) {
        this.itemType = itemType;
        this.title = title;
        this.item = item;
    }

    public CommonEntity(int itemType, ArrayList<String> banner) {
        this.itemType = itemType;
        this.banner = banner;
    }

    public CommonEntity(int itemType, ArrayList<CommonItemEntity>item, ArrayList<String> banner) {
        this.itemType = itemType;
        this.item = item;
        this.banner = banner;
    }

    public int getItemType() {
        return itemType;
    }

    public CommonEntity setItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

    public ArrayList<CommonItemEntity> getItem() {
        return item;
    }

    public CommonEntity setItem(ArrayList<CommonItemEntity>item) {
        this.item = item;
        return this;
    }

    public ArrayList<String> getBanner() {
        return banner;
    }

    public CommonEntity setBanner(ArrayList<String> banner) {
        this.banner = banner;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
