package com.sunnybear.framework.tools.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json日志
 * Created by chenkai.gu on 2017/4/12.
 */
class JsonLogger {

    public static void printJson(String tag, String msg, String headString) {
        String message = "";
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(Logger.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(Logger.JSON_INDENT);
            } else message = msg;
        } catch (JSONException e) {
            message = msg;
        }
        LoggerUtil.printLine(tag, true);
        message = headString + Logger.LINE_SEPARATOR + message;
        String[] lines = message.split(Logger.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        LoggerUtil.printLine(tag, false);
    }
}
