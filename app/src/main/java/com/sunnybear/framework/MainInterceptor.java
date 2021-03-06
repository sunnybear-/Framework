package com.sunnybear.framework;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.sunnybear.framework.tools.log.Logger;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/16.
 */
@Interceptor(priority = 1, name = "主拦截器")
public class MainInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Logger.d("interceptor", postcard.getPath() + ".." + postcard.getGroup());
        callback.onContinue(postcard);//拦截器任务完成,继续路由
//        callback.onInterrupt(new RuntimeException("我觉得有点异常"));//终止路由
    }

    @Override
    public void init(Context context) {

    }
}
