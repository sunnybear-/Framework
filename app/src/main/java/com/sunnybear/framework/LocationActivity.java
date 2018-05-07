package com.sunnybear.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunnybear.framework.databinding.ActivityLocationBinding;
import com.sunnybear.framework.library.base.BaseActivity;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/6.
 */
public class LocationActivity extends BaseActivity<ActivityLocationBinding, ActivityLocationViewModule> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected ActivityLocationViewModule bindingViewModule(ActivityLocationBinding viewDataBinding) {
        return new ActivityLocationViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModule.init();
    }
}
