package com.lan.sponge.util.callback;

import android.support.annotation.Nullable;


public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
}
