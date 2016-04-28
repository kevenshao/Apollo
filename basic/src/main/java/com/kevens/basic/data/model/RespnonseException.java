package com.kevens.basic.data.model;

/**
 * Created by kevens on 2016/4/27.
 *
 * 这个类用于捕获服务器约定的错误类型，
 * 即捕获ResponseData中当errcode != 0时的情况。
 */
public class RespnonseException extends RuntimeException {

    private int errcode = 0;

    public RespnonseException(int errCode, String msg) {
        super(msg);
        this.errcode = errCode;
    }

    public int getErrCode() {
        return errcode;
    }
}
