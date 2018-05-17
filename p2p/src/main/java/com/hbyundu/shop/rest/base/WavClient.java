package com.hbyundu.shop.rest.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by apple on 16/11/15.
 */
public class WavClient {

    private static final int DEFAULT_TIMEOUT = 60;

    private static Retrofit sRetrofit;

    static {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().setLenient().create();

        sRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Config.WAV_SERVER)
                .build();
    }

    public static Retrofit getRetrofit() {
        return sRetrofit;
    }
}
