package com.sunnybear.framework.tools.crash;

import android.os.Handler;
import android.os.Looper;

/**
 * 永不闪退的crash捕捉器
 * Created by chenkai.gu on 2017/2/17.
 */
public final class CrashHandler {
    private static ExceptionHandler sExceptionHandler;
    private static Thread.UncaughtExceptionHandler sUncaughtExceptionHandler;
    //标记位,避免重复安装卸载
    private static boolean sInstalled = false;

    private CrashHandler() {
    }

    /**
     * 当主线程或子线程抛出异常时会调用exceptionHandler.handlerException(Thread thread, Throwable throwable)
     * <p>
     * exceptionHandler.handlerException可能运行在非UI线程中。
     * <p>
     * 若设置了Thread.setDefaultUncaughtExceptionHandler则可能无法捕获子线程异常。
     *
     * @param exceptionHandler 异常捕捉组件
     */
    public static synchronized void install(ExceptionHandler exceptionHandler) {
        if (sInstalled) return;
        sInstalled = true;
        sExceptionHandler = exceptionHandler;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        if (e instanceof QuitCrashException) return;
                        if (sExceptionHandler != null)
                            sExceptionHandler.handlerException(Looper.getMainLooper().getThread(), e);
                    }
                }
            }
        });
        sUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (sExceptionHandler != null)
                    sExceptionHandler.handlerException(t, e);
            }
        });
    }

    /**
     * 卸载Cockroach
     */
    public static synchronized void unInstall() {
        if (!sInstalled) return;
        sInstalled = false;
        sExceptionHandler = null;
        //卸载后恢复默认的异常处理逻辑，否则主线程再次抛出异常后将导致ANR，并且无法捕获到异常位置
        Thread.setDefaultUncaughtExceptionHandler(sUncaughtExceptionHandler);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //主线程抛出异常，迫使 while (true) {}结束
                throw new QuitCrashException("Quit CrashHandler.....");
            }
        });
    }

    /**
     * 异常捕捉组件
     */
    public interface ExceptionHandler {

        void handlerException(Thread thread, Throwable throwable);
    }
}
