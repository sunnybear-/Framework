package com.sunnybear.framework.library.base;

import android.app.Application;

import com.sunnybear.framework.tools.PreferenceHelper;
import com.sunnybear.framework.tools.SDCardUtils;
import com.sunnybear.framework.tools.crash.CrashHandler;
import com.sunnybear.framework.tools.crash.LoggerExceptionHandler;

/**
 * application基类
 * Created by chenkai.gu on 2018/1/27.
 */
public abstract class BaseApplication extends Application {

    private static BaseApplication INSTANCE;

    public static BaseApplication getINSTANCE() {
        return INSTANCE;
    }

    static String pkgName;

    /**
     * 获取包名
     *
     * @return 包名
     */
    public static String getPkgName() {
        return pkgName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        pkgName = getPackageName();
        /*异常日志*/
        CrashHandler.install(LoggerExceptionHandler.getInstance(getApplicationContext(),
                SDCardUtils.getSDCardPath() + "/" + BaseApplication.getPkgName() + "/crash"));
        /*初始化文件储存*/
        PreferenceHelper.initialize(this);
    }
}
