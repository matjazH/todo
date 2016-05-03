package com.boss.android.xtodo.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/5/2.
 */
public class Settings {
    private static Settings INSATNCE;

    private Context mContext;

    private boolean isInited;

    private SharedPreferences mSharedPreferences;

    private Settings() {

    }

    public static Settings getInstance() {
        if (INSATNCE == null) {
            INSATNCE = new Settings();
        }
        return INSATNCE;
    }


    public void init(@NonNull Context context){
        checkNotNull(context);
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(Config.SP_CONFIG_NAME, Context.MODE_PRIVATE);
        isInited = true;
    }

    private void checkInit() {
        if (!isInited) {
            throw new ExceptionInInitializerError("StorageManager must inited.");
        }
    }

    public void setStorageDir(@NonNull String storageDir) {
        checkNotNull(storageDir);
        mSharedPreferences.edit().putString(Config.SP_STORAGE_DIR, storageDir).commit();
    }

    public String getStorageDir() {
        checkInit();
        return mSharedPreferences.getString(Config.SP_STORAGE_DIR, null);
    }
}
