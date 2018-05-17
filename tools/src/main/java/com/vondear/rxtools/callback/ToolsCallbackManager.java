package com.vondear.rxtools.callback;

import java.util.WeakHashMap;

/**
 *
 * Created by Administrator on 2017/12/7.
 */

public class ToolsCallbackManager {
    private static final WeakHashMap<Object, IToolsCallback> CALLBACKS = new WeakHashMap<>();
    private static class Holder {
        private static final ToolsCallbackManager INSTANCE = new ToolsCallbackManager();
    }

    public static ToolsCallbackManager getInstance() {
        return Holder.INSTANCE;
    }

    public ToolsCallbackManager addCallback(Object tag, IToolsCallback callback) {
        CALLBACKS.put(tag, callback);
        return this;
    }

    public IToolsCallback getCallback(Object tag) {
        return CALLBACKS.get(tag);
    }
}
