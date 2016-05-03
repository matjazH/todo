package com.boss.android.xtodo.base;

import android.content.Context;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/5/2.
 */
public class StorageManager {

    private final static String STORAGE_DIR_NAME = "storage";

    private static StorageManager INSTANCE;

    private Context mContext;

    private boolean isInited;

    private String mStorageDir;

    private StorageManager(){

    }

    public static StorageManager getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new StorageManager();
        }
        return INSTANCE;
    }

    public void init(@NonNull Context context){
        checkNotNull(context);
        mContext = context;
        isInited = true;
    }

    private void checkInit() {
        if (!isInited) {
            throw new ExceptionInInitializerError("StorageManager must inited.");
        }
    }

    public String getStorageDir() {
        checkInit();
        return mContext.getDir(STORAGE_DIR_NAME, Context.MODE_PRIVATE).getAbsolutePath();
    }

}
