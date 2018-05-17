package com.hbyundu.shop.rest.model.category;

/**
 * Created by zhangyue on 2017-12-18.
 */

public class CategoryItemModel {
    public long id;
    public String name;
    public String img;

    public CategoryItemModel(long id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

}
