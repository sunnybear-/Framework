package com.sunnybear.framework.tools.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志工具
 * Created by chenkai.gu on 2017/4/12.
 */
public class LoggerUtil {

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || TextUtils.isEmpty(line.trim())
                || TextUtils.equals(line, "\n") || TextUtils.equals(line, "\t");
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop)
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        else
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }
}
