package com.sunnybear.framework.library.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 路由暴露服务提供器
 * Created by chenkai.gu on 2018/5/17.
 */
public class RouterServiceProvider implements IProvider {
    //全生命周期的Context
    protected Context mApplicationContext;

    @Override
    public void init(Context context) {
        mApplicationContext = context;
    }
}
