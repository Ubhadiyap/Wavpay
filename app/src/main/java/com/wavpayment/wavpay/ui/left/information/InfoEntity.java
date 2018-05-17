package com.wavpayment.wavpay.ui.left.information;


import com.lan.sponge.delegate.SpongeDelegate;

/**
 *
 * Created by Administrator on 2017/11/23.
 */

public class InfoEntity {
    private int type;
    private String name;
    private String content;
    private SpongeDelegate delegate;

    public InfoEntity(int type, String name, String content, SpongeDelegate delegate) {
        this.type = type;
        this.name = name;
        this.content = content;
        this.delegate = delegate;
    }

    public InfoEntity(int type) {
        this.type = type;
    }

    public InfoEntity(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public InfoEntity(int type, String name, String content) {
        this.type = type;
        this.name = name;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SpongeDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(SpongeDelegate delegate) {
        this.delegate = delegate;
    }
}
