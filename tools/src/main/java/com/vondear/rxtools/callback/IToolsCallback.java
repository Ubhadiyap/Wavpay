package com.vondear.rxtools.callback;

import android.support.annotation.Nullable;

/**
 *
 * Created by Administrator on 2017/12/7.
 */

public interface IToolsCallback<T> {
    void executeCallback(@Nullable T args);
}
