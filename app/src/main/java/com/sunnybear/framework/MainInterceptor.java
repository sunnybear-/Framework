package com.sunnybear.framework;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/16.
 */
@Interceptor(priority = 1)
public class MainInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Log.d("interceptor", postcard.getPath() + ".." + postcard.getGroup());
//        callback.onContinue(postcard);//拦截器任务完成,继续路由
        callback.onInterrupt(null);//终止路由
    }

    @Override
    public void init(Context context) {
        Log.d("init", MainInterceptor.class.getSimpleName() + " has been inited");
    }
}
