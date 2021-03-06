package com.kevens.basic.data.model;

/**
 * Created by kevens on 2016/4/27.
 */
public class ApiException extends Exception {

    private final int code;
    private String displayMessage;

    public static final int UNKNOWN_ERROR = 1000;
    public static final int PARSE_ERROR = 1001;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public String getDisplayMessage() {
        return displayMessage;
    }
    public void setDisplayMessage(String msg) {
        this.displayMessage = msg + "(code:" + code + ")";
    }
}
