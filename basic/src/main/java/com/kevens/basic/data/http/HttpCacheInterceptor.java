package com.kevens.basic.data.http;

import android.util.Log;

import com.kevens.basic.BasicApplication;
import com.kevens.basic.utils.NetWorkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author kevens
 *
 * Dangerous interceptor that rewrites the server's cache-control header.
 * https://github.com/square/okhttp/wiki/Interceptors
 */
public class HttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtil.isNetConnected(BasicApplication.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Log.d("Okhttp", "no network");
        }

        Response originalResponse = chain.proceed(request);
        if (NetWorkUtil.isNetConnected(BasicApplication.getContext())) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .build();
        }
    }
}