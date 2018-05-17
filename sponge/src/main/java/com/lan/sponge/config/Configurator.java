package com.lan.sponge.config;

import android.app.Activity;
import android.os.Handler;

import com.blankj.utilcode.util.Utils;
import com.lan.sponge.delegate.SpongeDelegate;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;


public final class Configurator {

    private static final HashMap<Object, Object> SPONGE_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    final HashMap<Object, Object> getSpongesConfigs() {
        return SPONGE_CONFIGS;
    }

    private Configurator() {
        SPONGE_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        SPONGE_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }

    static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    public final void configure() {
        Logger.addLogAdapter(new AndroidLogAdapter());
        SPONGE_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
        Utils.init(Sponge.getAppContext());

    }

    public final Configurator withApiHost(String host) {
        SPONGE_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }

    public final Configurator withLoaderDelayed(long delayed) {
        SPONGE_CONFIGS.put(ConfigKeys.LOADER_DELAYED, delayed);
        return this;
    }

    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        SPONGE_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        SPONGE_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withActivity(Activity activity) {
        SPONGE_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }

    public final Configurator withDelegate(SpongeDelegate delegate){
        SPONGE_CONFIGS.put(ConfigKeys.DELEGADE,delegate);
        return this;
    }

    private void checkConfiguration() {
        final boolean isReady = (boolean) SPONGE_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = SPONGE_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) SPONGE_CONFIGS.get(key);
    }
}
