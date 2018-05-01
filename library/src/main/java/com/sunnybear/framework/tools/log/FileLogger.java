package com.sunnybear.framework.tools.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * 文件日志
 * Created by chenkai.gu on 2017/4/12.
 */
class FileLogger {
    private static final String FILE_PREFIX = "Logger_";
    private static final String FILE_FORMAT = ".log";

    public static void printFile(String tag, File targetDirectory, @Nullable String fileName, String headString, String msg) {
        fileName = (fileName == null) ? getFileName() : fileName;
        if (save(targetDirectory, fileName, msg))
            Log.d(tag, headString + " save log success! location is >>>" + targetDirectory.getAbsolutePath() + "/" + fileName);
        else
            Log.e(tag, headString + " save log fails!");
    }

    private static boolean save(File dir, @NonNull String fileName, String msg) {
        File file = new File(dir, fileName);
        try {
            OutputStream os = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(msg);
            osw.flush();
            osw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileName() {
        Random random = new Random();
        return FILE_PREFIX + Long.toString(System.currentTimeMillis() + random.nextInt(10000)).substring(4) + FILE_FORMAT;
    }
}
