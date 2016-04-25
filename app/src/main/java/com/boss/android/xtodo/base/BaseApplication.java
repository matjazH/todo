package com.boss.android.xtodo.base;

import android.app.Application;

/**
 * Created by boss on 2016/4/24.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashReport();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void initCrashReport() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getApplicationContext()));
    }
}
