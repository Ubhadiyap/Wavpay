package com.wavpayment.wavpay.widgte.recycler;

import java.util.ArrayList;
import java.util.List;

public abstract class DataConverter<T> {

    protected final ArrayList<MultipleItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;
    private List<T> data = null;
    public abstract ArrayList<MultipleItemEntity> convert();

    public DataConverter setJsonData(String json) {
        this.mJsonData = json;
        return this;
    }
    protected String getJsonData() {
        if (mJsonData == null) {
            throw new NullPointerException("DATA IS NULL!");
        }
        return mJsonData;
    }

    public DataConverter setData(List<T> data){
        this.data = data;
        return this;
    }
    protected List<T> getData(){
        return data;
    }
    public void clearData(){
        ENTITIES.clear();
    }
}
