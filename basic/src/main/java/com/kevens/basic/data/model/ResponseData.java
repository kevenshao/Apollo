package com.kevens.basic.data.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevens on 2016/4/21.
 *
 * 服务器返回给客户端的数据：
 *  errcode：0代表请求成功,非0代表请求出现问题
 *  msg：提示信息
 *  data：为返回的实际数据，可以是一个对象，也可是一个数组包含多个对象,如下
 *      {"errcode": 0, "msg": "success"}
 *      {"errcode": 0, "msg": "success", "data":{...}}
 *      {"errcode": 0, "msg": "success", "data":[{ }]
 *
 * <T>为具体的对象类型，如ResponseData<UserInfo> - 服务器返回的Json数据结构
 *
 * <T>如果没有定义具体的对象类型，输入Object，如ResponseData<Object>,
 *    data中数据按能以JSONObject返回，调用getJsonObject。
 */
public class ResponseData<T> {
    public int errcode;
    public String errmsg;
    public T data;

    public boolean isSuccess() {
        return errcode == 0;
    }

    /**
    * 只有T为Object时，才调用此方案把data转为JSONObject
    */
    public JSONObject getDataJsonObj() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
