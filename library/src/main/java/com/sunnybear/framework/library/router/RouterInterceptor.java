package com.sunnybear.framework.library.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * 路由拦截器
 * Created by chenkai.gu on 2018/5/17.
 */
public abstract class RouterInterceptor implements IInterceptor {
    //全生命周期的Context
    protected Context mApplicationContext;

    @Override
    public void init(Context context) {
        mApplicationContext = context;
    }
}
