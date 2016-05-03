package com.boss.android.xtodo.base;

import android.app.Application;

/**
 * Created by boss on 2016/4/24.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        init();
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

    private void init() {
        Settings.getInstance().init(this);
        StorageManager.getInstance().init(this);
        initCrashReport();
    }

    private void initCrashReport() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getApplicationContext()));
    }
}
