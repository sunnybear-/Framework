package com.sunnybear.framework.module;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;
import com.sunnybear.framework.R;
import com.sunnybear.framework.databinding.ActivityMainBinding;
import com.sunnybear.framework.databinding.ActivityMainViewModule;
import com.sunnybear.framework.library.base.BaseActivity;
import com.sunnybear.framework.tools.ResourcesUtils;

public class MainActivity extends BaseActivity<ActivityMainViewModule, ActivityMainBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected ActivityMainViewModule bindingViewModule(ActivityMainBinding viewDataBinding) {
        return new ActivityMainViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("主页");
        mViewModule.init();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ResourcesUtils.getColor(mContext, R.color.colorAccent), 0);
    }
}
