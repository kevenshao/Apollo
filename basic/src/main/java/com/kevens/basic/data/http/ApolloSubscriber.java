package com.kevens.basic.data.http;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.kevens.basic.data.model.ApiException;
import com.kevens.basic.data.model.RespnonseException;

import org.json.JSONException;

import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by kevens on 2016/4/27.
 */

public abstract class ApolloSubscriber<T> extends Subscriber<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    //出错提示
    private String networkMsg;
    private String parseMsg;
    private String unknownMsg;

    protected ApolloSubscriber(String networkMsg, String parseMsg, String unknownMsg) {
        this.networkMsg = networkMsg;
        this.parseMsg = parseMsg;
        this.unknownMsg = unknownMsg;
    }

    protected ApolloSubscriber() {
    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        ApiException ex;
        if (e instanceof HttpException) { //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                //权限错误，需要实现
                case UNAUTHORIZED:
                case FORBIDDEN:
                    Log.e("kevens", "PermissionError:" + httpException.code());
                    onPermissionError(ex);
                    break;

                //网络错误
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    Log.e("kevens", "http error:" + httpException.code());
                    ex.setDisplayMessage(networkMsg);
                    onError(ex);
                    break;
            }
        } else if (e instanceof RespnonseException) { //服务器返回的自定义错误
            RespnonseException resultException = (RespnonseException) e;
            ex = new ApiException(resultException, resultException.getErrCode());
            Log.e("kevens", "server error:" + resultException.getErrCode());
            onResultError(ex);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) { //解析错误
            ex = new ApiException(e, ApiException.PARSE_ERROR);
            ex.setDisplayMessage(parseMsg);
            Log.e("kevens", "parse error:" + e.toString());
            onError(ex);
        } else { //其它错误
            //[无网络时] java.net.UnknownHostException
            //[timeout] java.net.SocketTimeoutException
            Log.e("kevens", "other error:" + e.toString());
            ex = new ApiException(e, ApiException.UNKNOWN);
            ex.setDisplayMessage(unknownMsg);
            onError(ex);
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);

    /**
     * 权限错误，需要实现重新登录操作
     */
    protected abstract void onPermissionError(ApiException ex);

    /**
     * 服务器返回的错误
     */
    protected abstract void onResultError(ApiException ex);

}