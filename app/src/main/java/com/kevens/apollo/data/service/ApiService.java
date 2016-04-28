package com.kevens.apollo.data.service;

import com.kevens.apollo.data.model.UserInfo;
import com.kevens.basic.data.model.ResponseData;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author kevens
 */
public interface ApiService {
    public static String WEB_API_BASE = "http://kevens.wicp.net/history/Home/";


    @GET("Mobile/getMethod")
    Observable<ResponseData<Object>> getMethod(@Query("name") String name);

    @GET("Mobile/getMethodMap")
    Call<ResponseBody> getMethodMap(@Header("Authorization") String authorization, @QueryMap Map<String, String> options);


    @FormUrlEncoded
    @POST("Mobile/postMethod")
    Call<ResponseBody> postMethod(@Field("first_name") String first, @Field("last_name") String last);

    @POST("Mobile/postMethodBody")
    Call<ResponseBody> postMethodBody(@Body UserInfo userInfo);

    @Multipart
    @POST("Mobile/uploadFile")
    Observable<ResponseData<Object>> uploadFile(
            @Part("file_name") String file_name,
            @Part("file") RequestBody description);
}
