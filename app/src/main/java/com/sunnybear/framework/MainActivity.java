package com.sunnybear.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.databinding.ActivityMainBinding;
import com.sunnybear.framework.library.base.BaseActivity;
import com.sunnybear.library.map.AMapFragment;
import com.sunnybear.library.map.Constant;

public class MainActivity extends BaseActivity<ActivityMainBinding, ActivityMainViewModule> {

    private AMapFragment mAMapFragment;

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
        mViewModule.init();

        mAMapFragment = (AMapFragment) ARouter.getInstance().build(Constant.ROUTER_MAP).navigation();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_map, mAMapFragment).commit();
    }
}
