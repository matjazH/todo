package com.boss.android.xtodo.util;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boss on 2016/4/24.
 */
public class FileUtils {

    /**
     * 创建一个文件
     */
    public static void createFile(@NonNull String dir, @NonNull String name, @NonNull String content){
        checkNotNull(dir);
        checkNotNull(name);
        checkNotNull(content);
        File crashFileDir = new File(dir);
        boolean isDirExist = crashFileDir.exists();
        if (!isDirExist || !crashFileDir.isDirectory()) {
            isDirExist = crashFileDir.mkdirs();
        }
        if (isDirExist) {
            return;
        }
        File crashFile = new File(dir, name);
        try {
            FileWriter fw = new FileWriter(crashFile);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
