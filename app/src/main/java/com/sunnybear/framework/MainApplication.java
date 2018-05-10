package com.sunnybear.framework;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lzy.okgo.OkGo;
import com.sunnybear.framework.tools.log.Logger;

/**
 * <p>
 * Created by chenkai.gu on 2018/4/29.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.printStackTrace();
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        OkGo.getInstance().init(this);

        Logger.init(true, "framework");
    }
}
