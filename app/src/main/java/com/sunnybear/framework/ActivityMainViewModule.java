package com.sunnybear.framework;

import android.view.View;

import com.sunnybear.framework.databinding.ActivityMainBinding;
import com.sunnybear.framework.library.base.BaseViewModule;

/**
 * <p>
 * Created by chenkai.gu on 2018/4/29.
 */
public class ActivityMainViewModule extends BaseViewModule<MainActivity, ActivityMainBinding>
        implements View.OnClickListener {

    public ActivityMainViewModule(MainActivity mainActivity, ActivityMainBinding viewDataBinding) {
        super(mainActivity, viewDataBinding);
    }

    @Override
    public void init() {
        mViewDataBinding.setVm(this);
    }

    @Override
    public void onClick(View v) {
        //发起路由跳转
//        ARouter.getInstance().build(Constant.ROUTER_QRCODE).navigation();
    }
}
