package com.sunnybear.framework.tools.crash;

/**
 * 卸载Cockroach异常
 * Created by chenkai.gu on 2017/2/17.
 */
final class QuitCrashException extends RuntimeException {

    public QuitCrashException(String message) {
        super(message);
    }
}
