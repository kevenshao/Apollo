package com.kevens.basic.data.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.kevens.basic.data.model.RespnonseException;
import com.kevens.basic.data.model.ResponseData;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by kevens on 2016/4/27.
 * 此类拷贝retrofit GsonResponseBodyConverter, 并且重写了convert方法，
 * 对服务器返回的自定义错误进行处理。
 */

final class ApolloGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    ApolloGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        MediaType mt = value.contentType();
        String response = value.string();// 得到json串后，value资源自动关闭回收

        try {
            ResponseData<Object> responseData = gson.fromJson(response, ResponseData.class);
            if (responseData.errcode == 0){
                // errcode==0表示成功返回，直接返回继续用本来的Model类解析。
                // ResponseBody只允许消费一次，前面调用value.string()后会自动close回收，
                // 所以这里要重新创建一个相同的ResponseBody返回
                ResponseBody res = ResponseBody.create(mt, response);
                JsonReader jsonReader = gson.newJsonReader(res.charStream());
                return adapter.read(jsonReader);
            } else {
                // errcode!=0，服务返回异常信息，抛出
                throw new RespnonseException(responseData.errcode, responseData.errmsg);
            }

        } finally {
            value.close();
        }
    }
}
