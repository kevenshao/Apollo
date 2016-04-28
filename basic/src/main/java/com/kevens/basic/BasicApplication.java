package com.kevens.basic;

import android.app.Application;
import android.content.Context;

/**
 * author kevens
 */
public class BasicApplication extends Application {

    private static BasicApplication ourInstance = new BasicApplication();
    private static Context mContext;

    public static BasicApplication getInstance() {
        return ourInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        mContext = getApplicationContext();
    }


}
