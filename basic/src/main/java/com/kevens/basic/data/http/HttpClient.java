package com.kevens.basic.data.http;

import com.kevens.basic.BasicApplication;
import com.kevens.basic.data.gson.ApolloGsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author kevens
 */
public class HttpClient {

    private static HttpClient mInstance;
    private Retrofit retrofit;

    public static HttpClient getIns(String base_url) {
        if (mInstance == null) {
            synchronized (HttpClient.class) {
                if (mInstance == null) mInstance = new HttpClient(base_url);
            }
        }
        return mInstance;
    }

    public HttpClient(String BASE_URL) {

        File cacheFile = new File(BasicApplication.getInstance().getCacheDir(), "android");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ApolloGsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public <T> T createService(Class<T> clz) {
        return retrofit.create(clz);
    }
}
