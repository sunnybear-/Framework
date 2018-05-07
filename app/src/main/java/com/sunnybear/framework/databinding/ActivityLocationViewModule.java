package com.sunnybear.framework.databinding;

import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.LocationActivity;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/6.
 */
public class ActivityLocationViewModule extends BaseViewModule<LocationActivity, ActivityLocationBinding> {

    public ActivityLocationViewModule(LocationActivity locationActivity, ActivityLocationBinding viewDataBinding) {
        super(locationActivity, viewDataBinding);
    }

    @Override
    public void init() {

    }
}
