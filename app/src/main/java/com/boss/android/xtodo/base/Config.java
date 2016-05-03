package com.boss.android.xtodo.base;

/**
 * Created by boss on 2016/4/24.
 */
public class Config {

    private final static String PACKAGE_NAME = "com.boss.android.xtodo";

    public final static String SP_CONFIG_NAME = "config";
    public final static String SP_STORAGE_DIR = "storage_dir";

    public enum Dir {
        LOG("log"),
        CRASH("crash");

        private String mName;

        Dir(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }
    }

    public enum Debug {
        LOG(true),
        WRITE_TO_FILE(true),
        EXCEPTION(true);

        private boolean mEnable;

        Debug(boolean enable) {
            mEnable = enable;
        }

        public boolean isEnable() {
            return mEnable;
        }
    }
}
