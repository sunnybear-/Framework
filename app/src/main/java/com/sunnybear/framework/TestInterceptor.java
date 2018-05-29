package com.sunnybear.framework;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.sunnybear.framework.tools.log.Logger;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/29.
 */
@Interceptor(priority = 10, name = "测试拦截器")
public class TestInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Logger.d("interceptor", postcard.getPath() + ".." + postcard.getGroup());
        callback.onContinue(postcard);//拦截器任务完成,继续路由
    }

    @Override
    public void init(Context context) {

    }
}
