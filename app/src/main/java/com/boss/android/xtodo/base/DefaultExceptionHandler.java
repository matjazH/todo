package com.boss.android.xtodo.base;

import android.content.Context;

import com.boss.android.xtodo.util.FileUtils;

import java.io.File;

/**
 * Created by boss on 2016/4/24.
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;

    public DefaultExceptionHandler(Context context) {
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        sendCrashReport(throwable);

        if (Config.Debug.EXCEPTION.isEnable()) {
            throwable.printStackTrace();
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            if (Config.Debug.EXCEPTION.isEnable()) {
                e.printStackTrace();
            }
        }
        handleException();
    }

    private void sendCrashReport(Throwable throwable) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(throwable.getMessage());

        StackTraceElement[] elements = throwable.getStackTrace();
        for (StackTraceElement element:elements) {
            buffer.append(element.toString());
        }

        // TODO 写日志，并发送相关日志到服务器
        String dir = new File(StorageManager.getInstance().getStorageDir(), Config.Dir.CRASH.getName()).getAbsolutePath();
        
        String name = "crash_" + System.currentTimeMillis();
        FileUtils.createFile(dir, name, buffer.toString());
    }

    private void handleException() {
        // TODO 处理异常
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
